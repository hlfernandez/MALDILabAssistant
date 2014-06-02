package es.uvigo.ei.sing.mla.view.models.io;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import es.uvigo.ei.sing.mla.model.entities.ConditionGroup;
import es.uvigo.ei.sing.mla.model.entities.Replicate;
import es.uvigo.ei.sing.mla.model.entities.Sample;

public class ConditionGroupPathCreator extends AbstractPathCreator {
	public ConditionGroupPathCreator() {
		super();
	}

	public ConditionGroupPathCreator(PathCreator child) {
		super(child);
	}

	@Override
	public void create(File baseDirectory, ExperimentListFilter filter) throws IOException {
		for (ConditionGroup condition : filter.listConditions()) {
			final File sampleDir = new File(baseDirectory, condition.getName());
			sampleDir.mkdir();

			if (this.child != null) {
				this.child.create(sampleDir, createFilter(condition));
			}
		}
	}

	private static ExperimentListFilter createFilter(
			final ConditionGroup condition) {
		return new ExperimentListFilter() {
			@Override
			public List<ConditionGroup> listConditions() {
				return Collections.singletonList(condition);
			}

			@Override
			public List<Sample> listSamples() {
				return condition.getSamples();
			}

			@Override
			public List<Replicate> listReplicates() {
				return condition.getReplicates();
			}
		};
	}
}
