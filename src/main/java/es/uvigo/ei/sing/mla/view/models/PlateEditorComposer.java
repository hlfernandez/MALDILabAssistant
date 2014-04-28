package es.uvigo.ei.sing.mla.view.models;

import org.zkoss.bind.GlobalCommandEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zkmax.ui.select.annotation.Subscribe;
import org.zkoss.zss.api.Range;
import org.zkoss.zss.api.Ranges;
import org.zkoss.zss.api.model.CellStyle;
import org.zkoss.zss.api.model.CellStyle.FillPattern;
import org.zkoss.zss.api.model.EditableCellStyle;
import org.zkoss.zss.ui.event.CellMouseEvent;

import es.uvigo.ei.sing.mla.model.entities.ConditionGroup;
import es.uvigo.ei.sing.mla.model.entities.Replicate;
import es.uvigo.ei.sing.mla.model.entities.Sample;
import es.uvigo.ei.sing.mla.view.components.PlateEditor;

public class PlateEditorComposer extends SelectorComposer<PlateEditor> {
	private static final long serialVersionUID = 1L;

	private ConditionGroup selectedCondition;
	private Sample selectedSample;
	private Replicate selectedReplicate;

	@Override
	public void doAfterCompose(PlateEditor comp) throws Exception {
		for (Replicate replicate : getSelf().getExperiment().getReplicates()) {
			Range selection = Ranges.range(getSelf().getSelectedSheet(),
					replicate.getRow(), replicate.getCol());
			CellStyle oldStyle = selection.getCellStyle();
			EditableCellStyle newStyle = selection.getCellStyleHelper()
					.createCellStyle(oldStyle);
			newStyle.setFillColor(selection.getCellStyleHelper()
					.createColorFromHtmlColor(replicate.getColor()));
			newStyle.setFillPattern(FillPattern.SOLID_FOREGROUND);
			selection.setCellStyle(newStyle);
		}
	}

	@Subscribe("experiment")
	public void updateShoppingCart(Event evt) {
		if (evt instanceof GlobalCommandEvent) {
			final GlobalCommandEvent globalEvent = (GlobalCommandEvent) evt;

			switch (globalEvent.getCommand()) {
			case "onReplicateSelected":
				this.selectedReplicate = (Replicate) globalEvent.getArgs().get(
						"replicate");
				break;
			case "onSampleSelected":
				this.selectedSample = (Sample) globalEvent.getArgs().get(
						"sample");
				break;
			case "onConditionSelected":
				this.selectedCondition = (ConditionGroup) globalEvent.getArgs()
						.get("condition");
				break;
			}
		}
	}

	@Listen("onCellClick = plateeditor")
	public void onCellClick(CellMouseEvent event) {
		if (this.selectedReplicate != null) {
			if (this.selectedReplicate.getColor() == null) {
				this.selectedReplicate.setColor("#000000");
			}

			Range selection = Ranges.range(getSelf().getSelectedSheet(),
					getSelf().getSelection());
			CellStyle oldStyle = selection.getCellStyle();
			EditableCellStyle newStyle = selection.getCellStyleHelper()
					.createCellStyle(oldStyle);
			newStyle.setFillColor(selection
					.getCellStyleHelper()
					.createColorFromHtmlColor(this.selectedReplicate.getColor()));
			newStyle.setFillPattern(FillPattern.SOLID_FOREGROUND);
			selection.setCellStyle(newStyle);

			this.selectedReplicate.setPlateId(getSelf().getPlateId());
			this.selectedReplicate.setCol(selection.getColumn());
			this.selectedReplicate.setRow(selection.getRow());
		}
	}
}
