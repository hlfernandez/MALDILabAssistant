package es.uvigo.ei.sing.mla.view.models.io;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import es.uvigo.ei.sing.mla.model.entities.ConditionGroup;
import es.uvigo.ei.sing.mla.model.entities.Replicate;
import es.uvigo.ei.sing.mla.model.entities.Sample;

public class SamplePathCreator extends AbstractPathCreator {
	public SamplePathCreator() {
		super();
	}

	public SamplePathCreator(PathCreator child) {
		super(child);
	}

	@Override
	public void create(File baseDirectory, ExperimentListFilter filter) throws IOException {
		for (Sample sample : filter.listSamples()) {
			final File sampleDir = new File(baseDirectory, sample.getName());
			sampleDir.mkdir();

			if (this.child != null) {
				this.child.create(sampleDir, createFilter(sample));
			}
		}
	}

	private static ExperimentListFilter createFilter(final Sample sample) {
		return new ExperimentListFilter() {
			@Override
			public List<ConditionGroup> listConditions() {
				return Collections.singletonList(sample.getCondition());
			}

			@Override
			public List<Sample> listSamples() {
				return Collections.singletonList(sample);
			}

			@Override
			public List<Replicate> listReplicates() {
				return sample.getReplicates();
			}
		};
	}
}
