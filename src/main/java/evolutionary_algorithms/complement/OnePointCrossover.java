package evolutionary_algorithms.complement;

import java.util.ArrayList;
import java.util.List;

import config.SecureRandomGenerator;
import metaheurictics.strategy.Strategy;
import problem.definition.State;

public class OnePointCrossover extends Crossover {

	@Override
	public State crossover(State father1, State father2, double PC) {
		State newInd = (State) father1.getCopy();

		List<Object> ind1 = new ArrayList<Object>();
		List<Object> ind2 = new ArrayList<Object>();

		double number = SecureRandomGenerator.nextDouble();
		if (number <= PC) {
			// seleccionar posición de corte de forma segura y correcta
			int maxIndex = Strategy.getStrategy().getProblem().getCodification().getVariableCount() - 1;
			int pos = SecureRandomGenerator.nextInt(Math.max(1, maxIndex + 1));

			for (int i = 0; i < father1.getCode().size(); i++) {
				if (i <= pos) {
					ind1.add(father1.getCode().get(i));
					ind2.add(father2.getCode().get(i));
				} else {
					ind1.add(father2.getCode().get(i));
					ind2.add(father1.getCode().get(i));
				}
			}

			// generar un número aleatorio 0 o 1; si es 0 me quedo con ind1, si es 1 con ind2.
			int random = SecureRandomGenerator.nextInt(2);
			if (random == 0) {
				newInd.setCode((ArrayList<Object>) ind1);
			} else {
				newInd.setCode((ArrayList<Object>) ind2);
			}
		}
		return newInd;
	}
}
