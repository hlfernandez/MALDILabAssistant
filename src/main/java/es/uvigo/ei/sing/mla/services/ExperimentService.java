package es.uvigo.ei.sing.mla.services;

import java.util.List;

import es.uvigo.ei.sing.mla.model.entities.Experiment;

public interface ExperimentService {
	public Experiment add(Experiment experiment);

	public Experiment get(Integer id);

	public Experiment update(Experiment experiment);

	public void delete(Experiment experiment);

	public List<Experiment> listFilter(ExperimentSearchFilter filter);

	public Experiment reload(Experiment experiment);
}
