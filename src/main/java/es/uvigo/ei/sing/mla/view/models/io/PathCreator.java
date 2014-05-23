package es.uvigo.ei.sing.mla.view.models.io;

import java.io.File;

public interface PathCreator {
	public void create(File baseDirectory, ExperimentFilter filter);
	public void setChild(PathCreator child);
}
