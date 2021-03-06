package es.uvigo.ei.sing.mla.view.models;

import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;

import es.uvigo.ei.sing.mla.model.entities.Experiment;
import es.uvigo.ei.sing.mla.model.entities.User;
import es.uvigo.ei.sing.mla.services.ExperimentSearchFilter;
import es.uvigo.ei.sing.mla.services.ExperimentService;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class HomeViewModel {
	@WireVariable
	private ExperimentService experimentService;

	private List<Experiment> experiments = null;
	private ExperimentSearchFilter filter = new ExperimentSearchFilter(
			this.getUser());

	private User getUser() {
		final Session session = Sessions.getCurrent(false);

		if (session != null && session.hasAttribute("user")) {
			return (User) session.getAttribute("user");
		} else {
			Executions.sendRedirect("logout.zul");
			throw new IllegalStateException("Missing user in session");
		}
	}

	public List<Experiment> getExperimentModel() {
		if (experiments == null) {
			return getUser().getExperiments();
		}

		return new ListModelList<Experiment>(experiments);
	}

	public ExperimentSearchFilter getFilter() {
		return this.filter;
	}
	
	@Command
	public void logout() {
		final Session session = Sessions.getCurrent(false);
		
		if(session != null && session.hasAttribute("user")) {
			session.setAttribute("user", null);
		}
		
		Executions.sendRedirect("logout.zul");
	}

	@Command
	public void edit(@BindingParam("experiment") Experiment experiment) {
		Executions.getCurrent().sendRedirect(
				"experiment.zul?id=" + experiment.getId());
	}

	@Command
	public void delete(@BindingParam("experiment") final Experiment experiment) {
		EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {
			public void onEvent(ClickEvent event) throws Exception {
				if (Messagebox.Button.YES.equals(event.getButton())) {
					HomeViewModel.this.experimentService.delete(experiment);
					HomeViewModel.this.changeFilter();
					BindUtils.postNotifyChange(null, null, HomeViewModel.this, "experimentModel");
				}
			}
		};

		Messagebox.show("Are you sure you want to delete that experiment?",
				"Delete Experiment", new Messagebox.Button[] {
						Messagebox.Button.YES, Messagebox.Button.NO },
				Messagebox.QUESTION, clickListener);
	}

	@Command
	public void add() {
		Executions.getCurrent().sendRedirect("experiment.zul");
	}

	@Command
	@NotifyChange("experimentModel")
	public void changeFilter() {
		experiments = this.experimentService.listFilter(filter);
	}
}
