package es.uvigo.ei.sing.mla.view.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.GlobalCommandEvent;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
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

	private final EventListener<Event> globalCommandListener = new EventListener<Event>() {
		@Override
		public void onEvent(Event evt) {
			if (evt instanceof GlobalCommandEvent) {
				final GlobalCommandEvent globalEvent = (GlobalCommandEvent) evt;
				final Map<String, Object> args = globalEvent.getArgs();

				switch (globalEvent.getCommand()) {
				case "selectedReplicatePlaced":
					ExperimentViewModel.this.selectedReplicatePlaced((Replicate) args.get("replicate"));
					break;
					//					case "onSampleSelected":
					//						this.selectedSample = (Sample) args.get("sample");
					//						break;
					//					case "onConditionSelected":
					//						this.selectedCondition = (ConditionGroup) args.get("condition");
					//						break;
				}
			}
		}
	};

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
			this.experiment.setUser((User) Sessions.getCurrent().getAttribute("user"));
		}

		EventQueues.lookup("experiment", true).subscribe(this.globalCommandListener);
	}

	@GlobalCommand("selectedReplicatePlaced")
	public void selectedReplicatePlaced(
			@BindingParam("replicate") Replicate replicate
			) {
		if (this.selectedReplicate == replicate) {
			BindUtils.postNotifyChange(null, null, this, "selectedReplicate");
		}
	}

	public boolean isMetadataCompleted() {
		return this.experiment.isMetadataComplete();
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

	public List<String> getPlateNames() {
		final List<String> plateNames = new ArrayList<>();

		for (Integer i : this.getPlateIds()) {
			plateNames.add("Plate " + i);
		}

		return plateNames;
	}

	public List<Integer> getPlateIds() {
		final int cols = this.experiment.getNumCols();
		final int rows = this.experiment.getNumRows();
		final int replicates = this.experiment.countReplicates();

		final int numPlates = (int) Math.ceil((double) replicates / (double) (cols * rows));

		final List<Integer> plateIds = new ArrayList<>(numPlates);
		for (int i = 1; i <= numPlates; i++) {
			plateIds.add(i);
		}

		return plateIds;
	}

	public ConditionGroup getSelectedCondition() {
		return selectedCondition;
	}

	@Command("changeSelectedCondition")
	@NotifyChange({ "selectedCondition", "selectedSample", "selectedReplicate" })
	public void setSelectedCondition(
			@BindingParam("condition") ConditionGroup selectedCondition
			) {
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

		//		BindUtils.postGlobalCommand("experiment", null, "onSampleSelected", Collections.singletonMap("sample", (Object) selectedSample));
	}

	public Replicate getSelectedReplicate() {
		return selectedReplicate;
	}

	@Command("changeSelectedReplicate")
	@NotifyChange({ "selectedCondition", "selectedSample", "selectedReplicate" })
	public void setSelectedReplicate(
			@BindingParam("replicate") Replicate selectedReplicate
			) {
		this.selectedCondition = null;
		this.selectedSample = null;
		this.selectedReplicate = selectedReplicate;
	}

	@Command
	@NotifyChange({ "model", "experiment", "plateNames", "metadataCompleted" })
	public void save() {
		if (experiment.getId() == null) {
			this.experiment = this.experimentService.add(this.experiment);
		} else {
			experimentService.update(experiment);
		}

		Messagebox.show("Data has been saved");
	}

	@Command
	@NotifyChange({ "model", "experiment", "plateNames" })
	public void reset() {
		if (experiment.getId() == null) {
			this.experiment = new Experiment();
		} else {
			this.experiment = experimentService.reload(this.experiment);
		}
	}

	@Command
	public void exit() {
		EventQueues.lookup("experiment", true).unsubscribe(this.globalCommandListener);
		this.cancel();
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
		condition.setName("Condition" + (this.experiment.getConditions().size() + 1));
		condition.setColor("#000000");

		this.experiment.addCondition(condition);
	}

	@Command
	public void removeCondition(
			@BindingParam("condition") ConditionGroup condition
			) {
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
			@BindingParam("sample") Sample sample
			) {
		condition.removeSample(sample);
	}

	@Command
	public void addReplicate(@BindingParam("sample") Sample sample) {
		final Replicate replicate = new Replicate();
		replicate.setName("Replicate" + (sample.getReplicates().size() + 1));

		sample.addReplicate(replicate);
	}

	@Command
	public void removeReplicate(
			@BindingParam("sample") Sample sample,
			@BindingParam("replicate") Replicate replicate
			) {
		sample.removeReplicate(replicate);
	}
}
