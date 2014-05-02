package es.uvigo.ei.sing.mla.view.components;

import org.zkoss.composite.Composite;
import org.zkoss.zk.ui.annotation.ComponentAnnotation;
import org.zkoss.zss.api.Range;
import org.zkoss.zss.api.Ranges;
import org.zkoss.zss.api.model.CellStyle;
import org.zkoss.zss.api.model.CellStyle.FillPattern;
import org.zkoss.zss.api.model.EditableCellStyle;
import org.zkoss.zss.ui.Spreadsheet;

import es.uvigo.ei.sing.mla.model.entities.ConditionGroup;
import es.uvigo.ei.sing.mla.model.entities.Experiment;
import es.uvigo.ei.sing.mla.model.entities.Replicate;
import es.uvigo.ei.sing.mla.model.entities.Sample;
import es.uvigo.ei.sing.mla.util.CellNameType;

@Composite(name = "plateeditor")
@ComponentAnnotation({ "plateId:@ZKBIND(ACCESS=both)",
		"experiment:@ZKBIND(ACCESS=both)",
		"selectedCondition:@ZKBIND(ACCESS=both)",
		"selectedSample:@ZKBIND(ACCESS=both)",
		"selectedReplicate:@ZKBIND(ACCESS=both)" })
public class PlateEditor extends Spreadsheet {
	private static final long serialVersionUID = 1L;

	private Integer plateId = null;
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
		this.selectedCondition = selectedCondition;
	}

	public Sample getSelectedSample() {
		return selectedSample;
	}

	public void setSelectedSample(Sample selectedSample) {
		this.selectedSample = selectedSample;
	}

	public Replicate getSelectedReplicate() {
		return selectedReplicate;
	}

	public void setSelectedReplicate(Replicate selectedReplicate) {
		this.selectedReplicate = selectedReplicate;
	}

	private void updateLabels() {
		Ranges.range(this.getSelectedSheet()).protectSheet("password");

		this.setColumntitles(createTitles(experiment.getColNameType(),
				experiment.getNumCols()));
		this.setRowtitles(createTitles(experiment.getRowNameType(),
				experiment.getNumRows()));
	}

	public void updateHighlightedCells() {
		if (this.experiment == null || this.plateId == null) {
			// Clean plate highlights
		} else {
			for (Replicate replicate : this.experiment
					.getReplicates(this.plateId)) {
				highlightCells(replicate.getRow(), replicate.getCol(),
						replicate.getSample().getCondition().getColor());
			}
		}
	}

	private void highlightCells(int row, int col, String color) {
		Range selection = Ranges.range(this.getSelectedSheet(), row, col);
		CellStyle oldStyle = selection.getCellStyle();
		EditableCellStyle newStyle = selection.getCellStyleHelper()
				.createCellStyle(oldStyle);
		newStyle.setFillColor(selection.getCellStyleHelper()
				.createColorFromHtmlColor(color == null ? "#000000" : color));
		newStyle.setFillPattern(FillPattern.SOLID_FOREGROUND);
		selection.setCellStyle(newStyle);
	}

	private StringBuilder inflate(CellNameType type, StringBuilder str) {
		char a = (type == CellNameType.LOWERCASE) ? 'a' : 'A';
		char z = (type == CellNameType.LOWERCASE) ? 'z' : 'Z';

		int end = str.length() - 1;

		if (str.charAt(0) == z) {
			for (int i = 0; i < str.length(); ++i) {
				str.setCharAt(i, a);
			}

			return new StringBuilder(str + Character.toString(a));
		}

		if (str.charAt(end) == z) {
			String inflated = inflate(type,
					new StringBuilder(str.substring(0, end)))
					+ Character.toString(a);

			return new StringBuilder(inflated);
		}

		char next = str.charAt(end);
		++next;
		str.setCharAt(end, next);

		return str;
	}

	private String createTitles(CellNameType type, int num) {
		StringBuilder titles = new StringBuilder();

		switch (type) {
		case LOWERCASE:
			char c = 'a';
			StringBuilder chars = new StringBuilder();
			chars.append(c);

			for (int i = 1; i <= num; ++i) {
				chars.setCharAt(chars.length() - 1, c);
				titles.append(chars + ",");

				if (c == 'z') {
					chars = inflate(type, chars);
					c = 'a';
				} else {
					++c;
				}
			}
			break;
		case UPPERCASE:
			char C = 'A';
			StringBuilder CHARS = new StringBuilder();
			CHARS.append(C);

			for (int i = 1; i <= num; ++i) {
				CHARS.setCharAt(CHARS.length() - 1, C);
				titles.append(CHARS + ",");

				if (C == 'Z') {
					CHARS = inflate(type, CHARS);
					C = 'A';
				} else {
					++C;
				}
			}
			break;
		default:
			for (int i = 1; i <= num; ++i) {
				titles.append(i + ",");
			}
		}

		return titles.substring(0, titles.length() - 1);
	}
}
