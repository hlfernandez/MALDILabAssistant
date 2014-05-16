package es.uvigo.ei.sing.mla.view.models;

import java.util.Collections;

import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.DesktopUnavailableException;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zss.ui.event.CellMouseEvent;

import es.uvigo.ei.sing.mla.model.entities.Experiment;
import es.uvigo.ei.sing.mla.model.entities.Replicate;
import es.uvigo.ei.sing.mla.view.components.PlateEditor;

public class PlateEditorComposer extends SelectorComposer<PlateEditor> {
	private static final long serialVersionUID = 1L;

	@Listen("onCellClick = plateeditor")
	public void onCellClick(CellMouseEvent event)
	throws DesktopUnavailableException, InterruptedException {
		final Experiment experiment = this.getSelf().getExperiment();
		final Integer plateId = getSelf().getPlateId();
		final int row = event.getRow();
		final int column = event.getColumn();

		final Replicate selectedReplicate = this.getSelf().getSelectedReplicate();
		final Replicate replicateAtTarget = experiment.getReplicateAt(plateId, row, column);
		
		if (selectedReplicate != null &&
			(isCtrlPressed(event) || replicateAtTarget == null)
		) {
			experiment.placeReplicateAt(
				selectedReplicate, plateId, row, column
			);

			this.getSelf().updateHighlightedCells();

			BindUtils.postGlobalCommand(
				"experiment", EventQueues.DESKTOP,
				"selectedReplicatePlaced",
				Collections.singletonMap("replicate", (Object) selectedReplicate)
			);
//		} else if (selectedSample != null) {
//		} else if (selectedCondition != null) {
		} else {
			if (replicateAtTarget != null) {
				BindUtils.postGlobalCommand(
					"experiment", EventQueues.DESKTOP,
					"selectedReplicateChanged",
					Collections.singletonMap("replicate", (Object) replicateAtTarget)
				);
			}
		}
	}

	private static boolean isCtrlPressed(CellMouseEvent event) {
		return (event.getKeys() & CellMouseEvent.CTRL_KEY) == CellMouseEvent.CTRL_KEY;
	}
}
