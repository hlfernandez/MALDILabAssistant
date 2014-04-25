package es.uvigo.ei.sing.mla.view.models;

import org.zkoss.bind.GlobalCommandEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zkmax.ui.select.annotation.Subscribe;

import es.uvigo.ei.sing.mla.view.components.PlateEditor;

public class PlateEditorComposer extends SelectorComposer<PlateEditor> {
	private static final long serialVersionUID = 1L;

	@Subscribe("experiment")
	public void updateShoppingCart(Event evt) {
		if (evt instanceof GlobalCommandEvent) {
			final GlobalCommandEvent globalEvent = (GlobalCommandEvent) evt;
			
			if ("onSampleSelected".equals(globalEvent.getCommand())) {
				System.out.println(this.getSelf().getPlateId() + ": " + globalEvent.getArgs().get("sample"));
			}
		}
	}
}
