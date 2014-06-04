package es.uvigo.ei.sing.mla.view.components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.composite.Composite;
import org.zkoss.zk.ui.annotation.ComponentAnnotation;
import org.zkoss.zss.api.Range;
import org.zkoss.zss.api.Range.CellStyleHelper;
import org.zkoss.zss.api.Ranges;
import org.zkoss.zss.api.model.CellStyle;
import org.zkoss.zss.api.model.CellStyle.BorderType;
import org.zkoss.zss.api.model.CellStyle.FillPattern;
import org.zkoss.zss.api.model.Color;
import org.zkoss.zss.api.model.EditableCellStyle;
import org.zkoss.zss.ui.Spreadsheet;

import es.uvigo.ei.sing.mla.model.entities.ConditionGroup;
import es.uvigo.ei.sing.mla.model.entities.Experiment;
import es.uvigo.ei.sing.mla.model.entities.Replicate;
import es.uvigo.ei.sing.mla.model.entities.Sample;
import es.uvigo.ei.sing.mla.util.CellNameType;
import es.uvigo.ei.sing.mla.view.converters.ColorUtils;

@Composite(name = "plateeditor")
@ComponentAnnotation({
	"plateId:@ZKBIND(ACCESS=both)",
	"experiment:@ZKBIND(ACCESS=both)",
	"selectedCondition:@ZKBIND(ACCESS=both)",
	"selectedSample:@ZKBIND(ACCESS=both)",
	"selectedReplicate:@ZKBIND(ACCESS=both)"
})
public class PlateEditor extends Spreadsheet {
	private static final long serialVersionUID = 1L;
	
	private static final String DEFAULT_SELECTED_CELL_COLOR = "#000000";

	private Integer plateId;
	private Experiment experiment;
	private ConditionGroup selectedCondition;
	private Sample selectedSample;
	private Replicate selectedReplicate;

	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;

		this.setMaxVisibleColumns(this.experiment.getNumCols());
		this.setMaxVisibleRows(this.experiment.getNumRows());

		this.updateLabels();
		this.updateHighlightedCells();
	}

	public Integer getPlateId() {
		return plateId;
	}

	public void setPlateId(Integer plateId) {
		this.plateId = plateId;
		this.updateHighlightedCells();
	}

	public ConditionGroup getSelectedCondition() {
		return selectedCondition;
	}

	public void setSelectedCondition(ConditionGroup selectedCondition) {
		if (this.selectedCondition != selectedCondition) {
			this.selectedCondition = selectedCondition;
			this.selectedSample = null;
			this.selectedReplicate = null;
			
			this.updateSelection();
		}
	}

	public Sample getSelectedSample() {
		return selectedSample;
	}

	public void setSelectedSample(Sample selectedSample) {
		if (this.selectedSample != selectedSample) {
			this.selectedSample = selectedSample;
			this.selectedCondition = null;
			this.selectedReplicate = null;
			
			this.updateSelection();
		}
	}

	public Replicate getSelectedReplicate() {
		return selectedReplicate;
	}

	public void setSelectedReplicate(Replicate selectedReplicate) {
		if (this.selectedReplicate != selectedReplicate) {
			this.selectedReplicate = selectedReplicate;
			this.selectedCondition = null;
			this.selectedSample = null;
			
			this.updateSelection();
		}
	}
	
	private void updateSelection() {
		this.updateHighlightedCells();
		
//		final List<Replicate> selectedReplicates = this.getSelectedReplicates();
//
//		if (selectedReplicates.isEmpty()) {
//			this.focusTo(0, 0);
//		} else {
//			final Replicate firstSelected = selectedReplicates.get(0);
//			
//			this.focusTo(firstSelected.getRow(), firstSelected.getCol());
//		}
	}

	private void updateLabels() {
		Ranges.range(this.getSelectedSheet()).protectSheet("password");

		this.setColumntitles(createTitles(
			experiment.getColNameType(), experiment.getNumCols())
		);
		this.setRowtitles(createTitles(
			experiment.getRowNameType(), experiment.getNumRows())
		);
	}

	public void updateHighlightedCells() {
		this.cleanPlate();
		
		if (this.experiment != null && this.plateId != null) {
			for (Replicate replicate : this.experiment.getReplicates(this.plateId)) {
				highlightCells(
					replicate.getRow(), replicate.getCol(), replicate.getColor(), 
					this.isSelected(replicate)
				);
			}
		}
	}
	
	private List<Replicate> getSelectedReplicates() {
		final List<Replicate> replicates = new ArrayList<>();
		
		if (this.getPlateId() != null && this.getExperiment() != null) {
			if (this.selectedReplicate != null) {
				replicates.add(this.selectedReplicate);
			} else if (this.selectedSample != null) {
				replicates.addAll(this.selectedSample.getReplicates());
			} else if (this.selectedCondition != null) {
				for (Sample sample : this.selectedCondition.getSamples()) {
					replicates.addAll(sample.getReplicates());
				}
			}
			
			final Iterator<Replicate> itReplicates = replicates.iterator();
			while (itReplicates.hasNext()) {
				if (!this.getPlateId().equals(itReplicates.next().getPlateId())) {
					itReplicates.remove();
				}
			}
		}
		
		return replicates;
	}
	
	private boolean isSelected(Replicate replicate) {
		return this.getSelectedReplicates().contains(replicate);
	}

	private void cleanPlate() {
		final Range selection = Ranges.range(
			this.getSelectedSheet(), 0, 0, this.getMaxVisibleRows(), this.getMaxVisibleColumns()
		);
		selection.clearStyles();
	}

	private void highlightCells(int tRow, int lCol, int bRow, int rCol, String color, boolean selected) {
		final Range selection = Ranges.range(this.getSelectedSheet(), tRow, lCol, bRow, rCol);
		changeRangeBackground(selection, color == null ? DEFAULT_SELECTED_CELL_COLOR : color, selected);
	}
	
	private void highlightCells(int row, int col, String color, boolean selected) {
		this.highlightCells(row, col, row, col, color, selected);
	}
	
	private final static void changeRangeBackground(Range range, String color, boolean selected) {
		final CellStyle oldStyle = range.getCellStyle();
		final CellStyleHelper styleHelper = range.getCellStyleHelper();
		
		final EditableCellStyle newStyle = styleHelper.createCellStyle(oldStyle);
		final Color fillColor = styleHelper.createColorFromHtmlColor(color);
		newStyle.setFillColor(fillColor);
		newStyle.setFillPattern(FillPattern.SOLID_FOREGROUND);
		
		if (selected) {
			final Color borderColor = styleHelper.createColorFromHtmlColor(
				ColorUtils.getBestContrast(color)
			);
			
			newStyle.setBorderBottom(BorderType.THIN);
			newStyle.setBorderTop(BorderType.THIN);
			newStyle.setBorderLeft(BorderType.THIN);
			newStyle.setBorderRight(BorderType.THIN);
			newStyle.setBorderBottomColor(borderColor);
			newStyle.setBorderTopColor(borderColor);
			newStyle.setBorderLeftColor(borderColor);
			newStyle.setBorderRightColor(borderColor);
		}
		
		range.setCellStyle(newStyle);	
	}

	private String createTitles(CellNameType type, int num) {
		final String[] titles = type.createLabels(num);
		final StringBuilder sb = new StringBuilder();
		
		for (String title : titles) {
			if (sb.length() > 0) sb.append(',');
			
			sb.append(title);
		}
		
		return sb.toString();
	}
}
