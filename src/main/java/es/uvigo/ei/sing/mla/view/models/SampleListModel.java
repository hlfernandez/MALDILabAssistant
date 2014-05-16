package es.uvigo.ei.sing.mla.view.models;

import java.util.Observable;
import java.util.Observer;

import javax.swing.event.ListDataEvent;

import org.zkoss.zul.AbstractListModel;

import es.uvigo.ei.sing.mla.model.entities.Replicate;
import es.uvigo.ei.sing.mla.model.entities.Sample;

public class SampleListModel extends AbstractListModel<Replicate> implements Observer {
	private static final long serialVersionUID = 1L;
	
	private final Sample sample;
	
	public SampleListModel(Sample sample) {
		this.sample = sample;
		this.sample.addObserver(this);
		
		for (Replicate replicate : this.sample.getReplicates()) {
			replicate.addObserver(this);
		}
	}
	
	public Sample getSample() {
		return sample;
	}

	@Override
	public Replicate getElementAt(int index) {
		return this.sample.getReplicates().get(index);
	}

	@Override
	public int getSize() {
		return this.sample.getReplicates().size();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof Replicate) {
			final Replicate replicate = (Replicate) arg;
			
			try {
				if (this.sample.getReplicates().contains(replicate)) {
					// Replicate was added
					final int index = this.sample.getReplicates().indexOf(replicate);
					
					this.fireEvent(ListDataEvent.INTERVAL_ADDED, index, index);
					replicate.addObserver(this);
				} else {
					// Replicate was removed
					this.fireEvent(ListDataEvent.CONTENTS_CHANGED, -1, -1);
					replicate.deleteObserver(this);
				}
			} catch (Exception e) {
				// List model is no longer associated with any desktop
				this.removeFromObserved();
			}
		} else if (o instanceof Replicate) {
			try {
				// Replicate was added
				final int index = this.sample.getReplicates().indexOf((Replicate) o);
				
				this.fireEvent(ListDataEvent.CONTENTS_CHANGED, index, index);
			} catch (Exception e) {
				// List model is no longer associated with any desktop
				this.removeFromObserved();
			}
		}
	}

	private void removeFromObserved() {
		// List model is not associated with any desktop
		this.sample.deleteObserver(this);
		
		for (Replicate replicate : this.sample.getReplicates()) {
			replicate.deleteObserver(this);
		}
	}
}
