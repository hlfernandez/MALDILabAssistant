package es.uvigo.ei.sing.mla.view.models.io;

public abstract class AbstractPathCreator implements PathCreator {
	protected PathCreator child;
	
	@Override
	public void setChild(PathCreator child) {
		this.child = child;
	}
}
