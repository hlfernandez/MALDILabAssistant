package es.uvigo.ei.sing.mla.view.models.io;

public abstract class AbstractPathCreator implements PathCreator {
	protected PathCreator child;
	
	public AbstractPathCreator() {
		this(null);
	}
	
	public AbstractPathCreator(PathCreator child) {
		this.child = child;
	}

	@Override
	public void setChild(PathCreator child) {
		this.child = child;
	}
}
