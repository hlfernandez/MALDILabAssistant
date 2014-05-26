package es.uvigo.ei.sing.mla.view.models.io;

import java.util.List;

import es.uvigo.ei.sing.mla.model.entities.ConditionGroup;
import es.uvigo.ei.sing.mla.model.entities.Replicate;
import es.uvigo.ei.sing.mla.model.entities.Sample;

public interface ExperimentListFilter {
	public List<ConditionGroup> listConditions();
	public List<Sample> listSamples();
	public List<Replicate> listReplicates();
}
