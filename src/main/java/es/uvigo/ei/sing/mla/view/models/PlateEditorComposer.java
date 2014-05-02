package es.uvigo.ei.sing.mla.view.models;

import java.util.Collections;

import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.DesktopUnavailableException;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zss.ui.event.CellMouseEvent;

import es.uvigo.ei.sing.mla.model.entities.Replicate;
import es.uvigo.ei.sing.mla.view.components.PlateEditor;

public class PlateEditorComposer extends SelectorComposer<PlateEditor> {
	private static final long serialVersionUID = 1L;

	@Listen("onCellClick = plateeditor")
	public void onCellClick(CellMouseEvent event)
			throws DesktopUnavailableException, InterruptedException {
		final Replicate selectedReplicate = this.getSelf()
				.getSelectedReplicate();

		if (selectedReplicate != null) {

			selectedReplicate.setPlateId(getSelf().getPlateId());
			selectedReplicate.setCol(event.getColumn());
			selectedReplicate.setRow(event.getRow());

			this.getSelf().updateHighlightedCells();

			BindUtils.postGlobalCommand("experiment", EventQueues.DESKTOP,
					"selectedReplicatePlaced", Collections.singletonMap(
							"replicate", (Object) selectedReplicate));
		}
	}
}
