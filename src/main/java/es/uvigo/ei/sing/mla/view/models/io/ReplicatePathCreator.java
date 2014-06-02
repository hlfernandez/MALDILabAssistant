package es.uvigo.ei.sing.mla.view.models.io;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import es.uvigo.ei.sing.mla.model.entities.Experiment;
import es.uvigo.ei.sing.mla.model.entities.Replicate;
import es.uvigo.ei.sing.mla.util.CellNameType;

public class ReplicatePathCreator extends AbstractPathCreator {
	private final Experiment experiment;
	private final File datasetDirectory;
	
	public ReplicatePathCreator(Experiment experiment, File datasetDirectory) {
		this.experiment = experiment;
		this.datasetDirectory = datasetDirectory;
	}

	@Override
	public void create(File baseDirectory, ExperimentListFilter filter) throws IOException {
		final CellNameType colNameType = this.experiment.getColNameType();
		final CellNameType rowNameType = this.experiment.getRowNameType();
		
		for (Replicate replicate : filter.listReplicates()) {
			final File plateDir = new File(this.datasetDirectory, replicate.getPlateId().toString());
			final String colLabel = colNameType.indexToLabel(replicate.getCol());
			final String rowLabel = rowNameType.indexToLabel(replicate.getRow());
			
			final File sourceFile = new File(plateDir, rowLabel + colLabel + ".csv");
			
			final File replicateFile = new File(baseDirectory, replicate.getName() + ".csv");
			
			FileUtils.copyFile(sourceFile, replicateFile);
		}
	}
}
