#!/usr/bin/env python3
import xml.etree.ElementTree as ET
import json
import csv
import argparse
from pathlib import Path
import sys

def parse_jacoco(path: Path):
    tree = ET.parse(path)
    root = tree.getroot()

    # Try to find a top-level counter element (direct child of report)
    missed = covered = None
    for c in root.findall('counter'):
        if c.get('type') == 'LINE':
            missed = int(c.get('missed'))
            covered = int(c.get('covered'))
            break

    # Fallback: take the last LINE counter found anywhere in the file
    if missed is None:
        all_lines = root.findall('.//counter[@type="LINE"]')
        if all_lines:
            c = all_lines[-1]
            missed = int(c.get('missed'))
            covered = int(c.get('covered'))

    if missed is None:
        raise RuntimeError('No LINE counter found in jacoco XML')

    lines_to_cover = missed + covered
    uncovered_lines = missed
    coverage = round((covered / lines_to_cover) * 100, 2) if lines_to_cover > 0 else 0.0

    return {
        'lines_to_cover': lines_to_cover,
        'uncovered_lines': uncovered_lines,
        'coverage': coverage,
    }

def write_outputs(metrics: dict, out_json: Path, out_csv: Path, project_key: str | None):
    measures = [
        { 'metric': 'lines_to_cover', 'value': str(metrics['lines_to_cover']) },
        { 'metric': 'uncovered_lines', 'value': str(metrics['uncovered_lines']) },
        { 'metric': 'coverage', 'value': str(metrics['coverage']) },
    ]

    component = { 'id': None, 'key': project_key, 'measures': measures }
    payload = { 'component': component }

    out_json.parent.mkdir(parents=True, exist_ok=True)
    with out_json.open('w', encoding='utf-8') as f:
        json.dump(payload, f, indent=2, ensure_ascii=False)

    with out_csv.open('w', encoding='utf-8', newline='') as f:
        writer = csv.writer(f)
        writer.writerow(['metric', 'value'])
        for m in measures:
            writer.writerow([m['metric'], m['value']])


def write_per_file(xml_root: ET.Element, per_file_csv: Path):
    rows = []
    # Each <sourcefile> contains either <counter type="LINE"> or multiple <line> elements
    for sf in xml_root.findall('.//sourcefile'):
        name = sf.get('name')
        line_counter = None
        for c in sf.findall('counter'):
            if c.get('type') == 'LINE':
                line_counter = c
                break
        if line_counter is not None:
            missed = int(line_counter.get('missed'))
            covered = int(line_counter.get('covered'))
            total = missed + covered
        else:
            missed = covered = 0
            for line in sf.findall('line'):
                mi = int(line.get('mi', '0'))
                cb = int(line.get('cb', '0'))
                if mi > 0:
                    missed += 1
                if cb > 0:
                    covered += 1
            total = missed + covered

        coverage = round((covered / total) * 100, 2) if total > 0 else 0.0
        rows.append((name, total, missed, covered, coverage))

    per_file_csv.parent.mkdir(parents=True, exist_ok=True)
    with per_file_csv.open('w', encoding='utf-8', newline='') as f:
        writer = csv.writer(f)
        writer.writerow(['file', 'total_lines', 'missed', 'covered', 'coverage_pct'])
        for r in sorted(rows, key=lambda x: x[2], reverse=True):
            writer.writerow(r)

def main():
    parser = argparse.ArgumentParser(description='Parse jacoco XML and export lines_to_cover metrics')
    parser.add_argument('--input', '-i', default='target/site/jacoco/jacoco.xml', help='Path to jacoco.xml')
    parser.add_argument('--out-json', default='lines_to_cover.json', help='Output JSON filename')
    parser.add_argument('--out-csv', default='lines_to_cover.csv', help='Output CSV filename')
    parser.add_argument('--per-file-out', default=None, help='Optional per-file CSV output (e.g. per_file_coverage.csv)')
    parser.add_argument('--project-key', default=None, help='Optional Sonar project key to include in JSON')
    args = parser.parse_args()

    p = Path(args.input)
    if not p.exists():
        print(f'ERROR: input file not found: {p}', file=sys.stderr)
        sys.exit(2)

    try:
        metrics = parse_jacoco(p)
    except Exception as e:
        print('ERROR parsing jacoco XML:', e, file=sys.stderr)
        sys.exit(1)

    out_json = Path(args.out_json)
    out_csv = Path(args.out_csv)
    write_outputs(metrics, out_json, out_csv, args.project_key)

    if args.per_file_out:
        try:
            tree = ET.parse(p)
            root = tree.getroot()
            write_per_file(root, Path(args.per_file_out))
            print(f'Per-file CSV written to: {args.per_file_out}')
        except Exception as e:
            print('ERROR generating per-file CSV:', e, file=sys.stderr)

    print(json.dumps(metrics, indent=2, ensure_ascii=False))

if __name__ == '__main__':
    main()
