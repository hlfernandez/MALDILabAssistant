package es.uvigo.ei.sing.mla.view.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.GlobalCommandEvent;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.UploadEvent;
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
import es.uvigo.ei.sing.mla.util.Configuration;
import es.uvigo.ei.sing.mla.util.DataPackager;
import es.uvigo.ei.sing.mla.view.converters.ColorUtils;
import es.uvigo.ei.sing.mla.view.models.io.OutputSorter;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ExperimentViewModel {
	@Autowired
	@WireVariable
	private ExperimentService experimentService;

	@Autowired
	@WireVariable
	private OutputSorter outputSorter;

	private Experiment experiment;

	private ConditionGroup selectedCondition;
	private Sample selectedSample;
	private Replicate selectedReplicate;

	private String uploadStatus = UploadStatusType.STOPPED.toString();
	private boolean conditionChecked;
	private boolean sampleChecked;
	
	private File experimentFile;

	private final EventListener<Event> globalCommandListener = new EventListener<Event>() {
		@Override
		public void onEvent(Event evt) {
			if (evt instanceof GlobalCommandEvent) {
				final GlobalCommandEvent globalEvent = (GlobalCommandEvent) evt;
				final Map<String, Object> args = globalEvent.getArgs();

				switch (globalEvent.getCommand()) {
				case "selectedReplicateChanged":
					ExperimentViewModel.this.selectedReplicateChanged((Replicate) args.get("replicate"));
					
					break;
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
			this.experiment.setUser((User) Sessions.getCurrent().getAttribute(
					"user"));
		}
		
		this.experimentFile = this.experiment.getFile();

		EventQueues.lookup("experiment", true).subscribe(
				this.globalCommandListener);
	}

	@GlobalCommand("selectedReplicateChanged")
	@NotifyChange("selectedReplicate")
	public void selectedReplicateChanged(Replicate replicate) {
		if (this.selectedReplicate != replicate) {
			this.selectedReplicate = replicate;
			BindUtils.postNotifyChange(null, null, this, "selectedReplicate");
		}
	}

	public boolean isMetadataCompleted() {
		return this.experiment.isMetadataComplete();
	}

	public boolean isOnPlate() {
		return this.experiment.isOnPlate();
	}

	public ExperimentListModel getModel() {
		return new ExperimentListModel(this.experiment);
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}

	public CellNameType[] getCellNameTypes() {
		return CellNameType.values();
	}

	public List<Integer> getPlateIds() {
		final int replicates = this.experiment.countReplicates();

		if (replicates <= 1) {
			return Collections.singletonList(1);
		} else {
			final int cols = this.experiment.getNumCols();
			final int rows = this.experiment.getNumRows();

			final int numPlates = (int) Math.ceil((double) replicates
					/ (double) (cols * rows));

			final List<Integer> plateIds = new ArrayList<>(numPlates);
			for (int i = 1; i <= numPlates; i++) {
				plateIds.add(i);
			}

			return plateIds;
		}
	}

	public List<String> getPlateNames() {
		final List<String> plateNames = new ArrayList<>();

		for (Integer i : this.getPlateIds()) {
			plateNames.add("Plate " + i);
		}

		return plateNames;
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

	public String getUploadStatus() {
		return uploadStatus;
	}

	public void setUploadStatus(String uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

	public boolean isConditionChecked() {
		return conditionChecked;
	}

	public void setConditionChecked(boolean conditionChecked) {
		this.conditionChecked = conditionChecked;
	}

	public boolean isSampleChecked() {
		return sampleChecked;
	}

	public void setSampleChecked(boolean sampleChecked) {
		this.sampleChecked = sampleChecked;
	}
	
	public File getExperimentFile() {
		return experimentFile;
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
		EventQueues.lookup("experiment", true).unsubscribe(
				this.globalCommandListener);
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
		final int conditionIndex = this.experiment.getConditions().size() + 1;

		condition.setName("Condition " + conditionIndex);
		condition.setColor(ColorUtils.getThirtyColorHex(conditionIndex - 1));

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

	@Command
	@NotifyChange("uploadStatus")
	public void uploadFile(@BindingParam("event") UploadEvent event) {
		this.uploadStatus = UploadStatusType.IN_PROGRESS.toString();

		try {
			final File userDir = this.experiment.getUser().getDirectory();

			FileUtils.cleanDirectory(userDir);

			DataPackager.unpackageData(event.getMedia(), userDir);

			this.uploadStatus = UploadStatusType.FINISHED.toString();
		} catch (Exception e) {
			this.uploadStatus = UploadStatusType.ERROR.toString();
		}
	}

	@Command
	public void downloadFile() {
		File tmpDir = null;
		File zipFile = null;

		try {
			tmpDir = createTmpDirectory();

			zipFile = File.createTempFile("mla", "zip");

			final File userDir = this.experiment.getUser().getDirectory();

			System.out.println(this.getPathRegex());
			this.outputSorter.sort(this.experiment, userDir,
					this.getPathRegex(), tmpDir);

			File zip = DataPackager.zipData(tmpDir, zipFile);

			String fileExtension = FilenameUtils.getExtension(zip.getName());

			try (FileInputStream zipFIS = new FileInputStream(zip)) {
				Filedownload.save(IOUtils.toByteArray(zipFIS), "application/"
						+ fileExtension, this.experiment.getName() + ".zip");
			}
		} catch (IOException io) {
			io.printStackTrace();
			Messagebox.show("Error compressing data", "Download Error",
					Messagebox.OK, Messagebox.EXCLAMATION);
		} finally {
			if (tmpDir != null && tmpDir.exists())
				try {
					FileUtils.deleteDirectory(tmpDir);
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException("Could not delete temp directory");
				}

			if (zipFile != null && zipFile.exists())
				zipFile.delete();
		}
	}

	private File createTmpDirectory() throws IOException {
		File tmpDir;
		Configuration.getInstance().getTmpDirectory().mkdirs();
		tmpDir = File.createTempFile(this.getExperiment().getUser()
				.getLogin(), "down", Configuration.getInstance()
				.getTmpDirectory());
		tmpDir.delete();
		tmpDir.mkdir();
		return tmpDir;
	}

	public boolean isDirectoryStructureOk() {
		return this.outputSorter.checkPath(this.getPathRegex());
	}

	private String getPathRegex() {
		String pathRegex = "";

		if (this.conditionChecked) {
			pathRegex += "[Condition]/";
		}

		if (this.sampleChecked) {
			pathRegex += "[Sample]/";
		}

		pathRegex += "[Replicate]";

		return pathRegex;
	}
}
