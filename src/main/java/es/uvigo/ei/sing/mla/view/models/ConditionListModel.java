package es.uvigo.ei.sing.mla.view.models;

import java.util.Observable;
import java.util.Observer;

import javax.swing.event.ListDataEvent;

import org.zkoss.zul.AbstractListModel;

import es.uvigo.ei.sing.mla.model.entities.ConditionGroup;
import es.uvigo.ei.sing.mla.model.entities.Sample;

public class ConditionListModel extends AbstractListModel<SampleListModel> implements Observer {
	private static final long serialVersionUID = 1L;
	
	private final ConditionGroup condition;
	
	public ConditionListModel(ConditionGroup condition) {
		this.condition = condition;
		this.condition.addObserver(this);
	}
	
	public ConditionGroup getCondition() {
		return condition;
	}

	@Override
	public SampleListModel getElementAt(int index) {
		return new SampleListModel(this.condition.getSamples().get(index));
	}

	@Override
	public int getSize() {
		return this.condition.getSamples().size();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof Sample) {
			final Sample condition = (Sample) arg;
			try {
				if (this.condition.getSamples().contains(condition)) {
					// Sample was added
					final int index = this.condition.getSamples().indexOf(condition);
					
					this.fireEvent(ListDataEvent.INTERVAL_ADDED, index, index);
				} else {
					// Sample was removed
					this.fireEvent(ListDataEvent.CONTENTS_CHANGED, -1, -1);
				}
			} catch (Exception e) {
				// List model is no longer associated with any desktop
				o.deleteObserver(this);
			}
		}
	}
}
