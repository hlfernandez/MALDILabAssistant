package es.uvigo.ei.sing.mla.view.models.io;

import java.io.File;
import java.io.IOException;

public interface PathCreator {
	public void create(File baseDirectory, ExperimentListFilter filter) throws IOException;
	public void setChild(PathCreator child);
}
