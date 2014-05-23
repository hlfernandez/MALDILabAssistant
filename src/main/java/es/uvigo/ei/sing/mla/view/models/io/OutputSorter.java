package es.uvigo.ei.sing.mla.view.models.io;

import java.io.File;

import es.uvigo.ei.sing.mla.model.entities.Experiment;

public interface OutputSorter {
	public boolean checkPath(String pathRegex);
	public void sort(Experiment experiment, File datasetDirectory, String pathRegex, File outputDirectory);
}
