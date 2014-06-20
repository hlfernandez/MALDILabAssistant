package es.uvigo.ei.sing.mla.daos;

import es.uvigo.ei.sing.mla.model.entities.ConditionGroup;

public interface ConditionGroupDAO {
	public ConditionGroup add(ConditionGroup condition);

	public ConditionGroup get(Integer id);

	public ConditionGroup reload(ConditionGroup condition);

	public ConditionGroup update(ConditionGroup condition);

	public void delete(ConditionGroup condition);
}
