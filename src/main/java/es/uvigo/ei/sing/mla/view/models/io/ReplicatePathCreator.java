package es.uvigo.ei.sing.mla.view.models.io;

import java.io.File;

import es.uvigo.ei.sing.mla.model.entities.Experiment;
import es.uvigo.ei.sing.mla.model.entities.Replicate;

public class ReplicatePathCreator extends AbstractPathCreator {
	private Experiment experiment;
	
	public ReplicatePathCreator(Experiment experiment) {
		this.experiment = experiment;
	}

	@Override
	public void create(File baseDirectory, ExperimentListFilter filter) {
		for (Replicate replicate : filter.listReplicates()) {
			final File replicateFile = new File(baseDirectory, replicate.getName());
		}
	}
}
