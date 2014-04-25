package es.uvigo.ei.sing.mla.view.models;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zss.api.Ranges;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zul.Messagebox;

import es.uvigo.ei.sing.mla.model.entities.ConditionGroup;
import es.uvigo.ei.sing.mla.model.entities.Experiment;
import es.uvigo.ei.sing.mla.model.entities.Replicate;
import es.uvigo.ei.sing.mla.model.entities.Sample;
import es.uvigo.ei.sing.mla.model.entities.User;
import es.uvigo.ei.sing.mla.services.ExperimentService;
import es.uvigo.ei.sing.mla.util.CellNameType;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ExperimentViewModel extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;

	@WireVariable
	private ExperimentService experimentService;

	private Experiment experiment;

	private ConditionGroup selectedCondition;
	private Sample selectedSample;
	private Replicate selectedReplicate;

	private int plate;

	@Wire
	private Spreadsheet ss;

	@Init
	public void init() {
		final Session session = Sessions.getCurrent();
		final String experimentId = Executions.getCurrent().getParameter("id");

		if (!session.hasAttribute("user")) {
			Executions.getCurrent().sendRedirect("index.zul");
		}

		if (experimentId != null) {
			final int id = Integer.parseInt(experimentId);

			this.experiment = experimentService.get(id);
		} else {
			this.experiment = new Experiment();
			this.experiment.setUser((User) Sessions.getCurrent().getAttribute(
					"user"));
		}
	}

	public ExperimentListModel getModel() {
		return new ExperimentListModel(this.experiment);
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public CellNameType[] getCellNameTypes() {
		return CellNameType.values();
	}

	public int getPlate() {
		return plate;
	}

	public void setPlate(int plate) {
		this.plate = plate;
	}

	public ConditionGroup getSelectedCondition() {
		return selectedCondition;
	}

	@Command("changeSelectedCondition")
	@NotifyChange({ "selectedCondition", "selectedSample", "selectedReplicate" })
	public void setSelectedCondition(
			@BindingParam("condition") ConditionGroup selectedCondition) {
		this.selectedCondition = selectedCondition;
		this.selectedSample = null;
		this.selectedReplicate = null;
	}

	public Sample getSelectedSample() {
		return selectedSample;
	}

	@Command("changeSelectedSample")
	@NotifyChange({ "selectedCondition", "selectedSample", "selectedReplicate" })
	public void setSelectedSample(@BindingParam("sample") Sample selectedSample) {
		this.selectedCondition = null;
		this.selectedSample = selectedSample;
		this.selectedReplicate = null;
	}

	public Replicate getSelectedReplicate() {
		return selectedReplicate;
	}

	@Command("changeSelectedReplicate")
	@NotifyChange({ "selectedCondition", "selectedSample", "selectedReplicate" })
	public void setSelectedReplicate(
			@BindingParam("replicate") Replicate selectedReplicate) {
		this.selectedCondition = null;
		this.selectedSample = null;
		this.selectedReplicate = selectedReplicate;
	}

	@Command
	public void drop(@BindingParam("object") Object object) {
		System.out.println(object);
	}

	@Command
	@NotifyChange({ "model", "experiment" })
	public void save() {
		if (experiment.getId() == null) {
			this.experiment = this.experimentService.add(this.experiment);
		} else {
			experimentService.update(experiment);
		}

		Messagebox.show("Data has been saved");
	}

	@Command
	@NotifyChange({ "model", "experiment" })
	public void reset() {
		if (experiment.getId() == null) {
			this.experiment = new Experiment();
		} else {
			this.experiment = experimentService.reload(this.experiment);
		}
	}

	@Command
	public void cancel() {
		if (experiment.getId() != null) {
			this.experimentService.reload(this.experiment);
		}

		Executions.sendRedirect("home.zul");
	}

	@Command
	public void addCondition() {
		final ConditionGroup condition = new ConditionGroup();
		condition.setName("Condition"
				+ (this.experiment.getConditions().size() + 1));

		this.experiment.addCondition(condition);
	}

	@Command
	public void removeCondition(
			@BindingParam("condition") ConditionGroup condition) {
		this.experiment.removeCondition(condition);
	}

	@Command
	public void addSample(@BindingParam("condition") ConditionGroup condition) {
		final Sample sample = new Sample();
		sample.setName("Sample" + (condition.getSamples().size() + 1));

		condition.addSample(sample);
	}

	@Command
	public void removeSample(
			@BindingParam("condition") ConditionGroup condition,
			@BindingParam("sample") Sample sample) {
		condition.removeSample(sample);
	}

	@Command
	public void addReplicate(@BindingParam("sample") Sample sample) {
		final Replicate replicate = new Replicate();
		replicate.setName("Replicate" + (sample.getReplicates().size() + 1));

		sample.addReplicate(replicate);
	}

	@Command
	public void removeReplicate(@BindingParam("sample") Sample sample,
			@BindingParam("replicate") Replicate replicate) {
		sample.removeReplicate(replicate);
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

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		Ranges.range(ss.getSelectedSheet()).protectSheet("password");
//	EXPERIMENT IS NULL
//		ss.setColumntitles(createTitles(experiment.getColNameType(),
//				experiment.getNumCols()));
//		ss.setRowtitles(createTitles(experiment.getRowNameType(),
//				experiment.getNumRows()));
	}
}
