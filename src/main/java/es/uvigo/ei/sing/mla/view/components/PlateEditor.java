package es.uvigo.ei.sing.mla.view.components;

import org.zkoss.composite.Composite;
import org.zkoss.zk.ui.annotation.ComponentAnnotation;
import org.zkoss.zss.api.Ranges;
import org.zkoss.zss.ui.Spreadsheet;

import es.uvigo.ei.sing.mla.model.entities.Experiment;
import es.uvigo.ei.sing.mla.util.CellNameType;

@Composite(name="plateeditor")
@ComponentAnnotation({ "plateId:@ZKBIND(ACCESS=both)", "experiment:@ZKBIND(ACCESS=both)" })
public class PlateEditor extends Spreadsheet {
	private static final long serialVersionUID = 1L;
	
	private int plateId;
	private Experiment experiment;

	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
		
		this.setMaxVisibleColumns(this.experiment.getNumCols());
		this.setMaxVisibleRows(this.experiment.getNumRows());
		
		this.updateLabels();
	}

	public int getPlateId() {
		return plateId;
	}

	public void setPlateId(int plateId) {
		this.plateId = plateId;
	}
	
	private void updateLabels() {
		Ranges.range(this.getSelectedSheet()).protectSheet("password");
		
		this.setColumntitles(createTitles(
			experiment.getColNameType(),
			experiment.getNumCols())
		);
		this.setRowtitles(createTitles(
			experiment.getRowNameType(),
			experiment.getNumRows())
		);
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
					chars = inflate(type, CHARS);
					c = 'A';
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
