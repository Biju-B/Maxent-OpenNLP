package sample.classifier.maxent;

import java.io.File;
import java.io.IOException;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.util.InvalidFormatException;

public class Classifier {
	private DocumentCategorizerME categorizer=null; 
	public Classifier(File modelFile) throws InvalidFormatException, IOException{
		this(Util.loadModel(modelFile));
	}
	public Classifier(DoccatModel model){
		categorizer=new DocumentCategorizerME(model);
	}
	public String classify(String text){
		double[] outcome = categorizer.categorize(text);
		return categorizer.getBestCategory(outcome);
	}
}
