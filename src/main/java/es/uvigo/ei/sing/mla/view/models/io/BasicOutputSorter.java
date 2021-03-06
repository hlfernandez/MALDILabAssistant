package es.uvigo.ei.sing.mla.view.models.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import es.uvigo.ei.sing.mla.model.entities.ConditionGroup;
import es.uvigo.ei.sing.mla.model.entities.Experiment;
import es.uvigo.ei.sing.mla.model.entities.Replicate;
import es.uvigo.ei.sing.mla.model.entities.Sample;

@Service("outputSorter")
public class BasicOutputSorter implements OutputSorter {
	private static final List<String> directories = Arrays.asList("Condition", "Sample", "Replicate");

	@Override
	public void sort(Experiment experiment, File datasetDirectory, String pathRegex, File outputDirectory) throws IOException {
		PathCreator pathCreator = createPathCreatorFromRegex(pathRegex, experiment, datasetDirectory);
		
		pathCreator.create(outputDirectory, createFilterFromExperiment(experiment));
	}
	
	private PathCreator createPathCreatorFromRegex(String pathRegex, Experiment experiment, File outputDirectory) {
		PathCreator pathCreator = new ReplicatePathCreator(experiment, outputDirectory);
		
		boolean hasSample = pathRegex.contains("Sample");
		boolean hasCondition = pathRegex.contains("Condition");
		
		if (hasSample) {
			pathCreator = new SamplePathCreator(pathCreator);
		}
		
		if (hasCondition) {
			pathCreator = new ConditionGroupPathCreator(pathCreator);
		}
		
		return pathCreator;
	}

	private ExperimentListFilter createFilterFromExperiment(final Experiment experiment) {
		return new ExperimentListFilter() {
			@Override
			public List<ConditionGroup> listConditions() {
				return experiment.getConditions();
			}
			
			@Override
			public List<Sample> listSamples() {
				return experiment.getSamples();
			}
			
			@Override
			public List<Replicate> listReplicates() {
				return experiment.getReplicates();
			}
		};
	}

	@Override
	public boolean checkPath(String pathRegex) {
		for (String match : generateMatches()) {
			if (Pattern.matches(match, pathRegex)) {
				return true;
			}
		}
	
		return false;
	}

	private static List<String> generateMatches() {
		List<String> matches = new ArrayList<String>();
		
		matches.add("");

		for (String directory : directories) {
			List<String> newMatches = new ArrayList<String>();

			for (String subset : matches) {
				newMatches.add(subset);

				String newSubset = subset;
				newSubset += "\\/\\[" + directory + "\\]";
				newMatches.add(newSubset);
			}

			matches = newMatches;
		}
		
		matches.remove(0);
		
		for (int i = 0; i < matches.size(); i++) {
			matches.set(i, matches.get(i).substring(2));
		}
		
		List<String> matchesWithFile = new ArrayList<String>();
		
		for (int i = 0; i < matches.size(); i++) {
			String current = matches.get(i);
			
			if(current.contains(directories.get(directories.size() - 1))) {
				matchesWithFile.add(current);
			}
		}
		
		return matchesWithFile;
	}
}
