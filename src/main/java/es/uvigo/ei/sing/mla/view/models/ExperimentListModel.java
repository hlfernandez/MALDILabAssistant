package es.uvigo.ei.sing.mla.view.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.event.ListDataEvent;

import org.zkoss.zul.AbstractListModel;

import es.uvigo.ei.sing.mla.model.entities.ConditionGroup;
import es.uvigo.ei.sing.mla.model.entities.Experiment;

public class ExperimentListModel extends AbstractListModel<ConditionListModel> implements Observer {
	private static final long serialVersionUID = 1L;
	
	private final Experiment experiment;
	private List<ConditionGroup> conditions;
	
	public ExperimentListModel(Experiment experiment) {
		this.experiment = experiment;
		this.experiment.addObserver(this);
		
		this.conditions = new ArrayList<>(this.experiment.getConditions());
	}
	
	public Experiment getExperiment() {
		return experiment;
	}

	@Override
	public ConditionListModel getElementAt(int index) {
		return new ConditionListModel(this.experiment.getConditions().get(index));
	}

	@Override
	public int getSize() {
		return this.experiment.getConditions().size();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof ConditionGroup) {
			final ConditionGroup condition = (ConditionGroup) arg;

			try {
				if (this.experiment.getConditions().contains(condition)) {
					// Condition was added
					final int index = this.experiment.getConditions().indexOf(condition);
					
					this.fireEvent(ListDataEvent.INTERVAL_ADDED, index, index);
				} else {
					// Condition was removed
					final int index = this.conditions.indexOf(condition);
					
					this.fireEvent(ListDataEvent.INTERVAL_REMOVED, index, index);
				}
				
				this.conditions = new ArrayList<>(this.experiment.getConditions());
			} catch (Exception e) {
				// List model is no longer associated with any desktop
				o.deleteObserver(this);
			}
		}
	}
}
