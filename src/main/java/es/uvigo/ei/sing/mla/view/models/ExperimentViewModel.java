package es.uvigo.ei.sing.mla.view.models;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

import es.uvigo.ei.sing.mla.model.entities.ConditionGroup;
import es.uvigo.ei.sing.mla.model.entities.Experiment;
import es.uvigo.ei.sing.mla.model.entities.Replicate;
import es.uvigo.ei.sing.mla.model.entities.Sample;
import es.uvigo.ei.sing.mla.model.entities.User;
import es.uvigo.ei.sing.mla.services.ExperimentService;
import es.uvigo.ei.sing.mla.util.CellNameType;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ExperimentViewModel {
	@WireVariable
	private ExperimentService experimentService;

	private Experiment experiment;
	
	private ConditionGroup selectedCondition;
	private Sample selectedSample;
	private Replicate selectedReplicate;
	
	private int plate;

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
	public void toTab0() {
		if (experiment.getId() == null) {
			Executions.getCurrent().sendRedirect("experimentData.zul");
		} else {
			Executions.getCurrent().sendRedirect(
					"experimentData.zul?id=" + experiment.getId());
		}
	}

	@Command
	public void toTab1() {
		if (experiment.getId() == null) {
			Executions.getCurrent().sendRedirect("experimentDesign.zul");
		} else {
			Executions.getCurrent().sendRedirect(
					"experimentDesign.zul?id=" + experiment.getId());
		}
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
}
