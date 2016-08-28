/**
 *
 */
package org.perfcake.ide.core.model;

import org.perfcake.model.Scenario;

/**
 *
 * Model converter converts between perfcake model and pc4ide model
 *
 * @author jknetl
 *
 */
final public class ModelConverter {

	private ModelConverter() {
	}

	public static ScenarioModel getPc4ideModel(Scenario perfcakeModel) {
		if (perfcakeModel == null) {
			throw new IllegalArgumentException("Model must not be null.");
		}

		final ScenarioModel model = new ScenarioModel(perfcakeModel);

		return model;

	}

	public static Scenario getPerfcakeModel(ScenarioModel pc4ideModel) {
		if (pc4ideModel == null) {
			throw new IllegalArgumentException("Model must not be null.");
		}

		return pc4ideModel.getScenario();

	}

}
