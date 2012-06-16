package sample.classifier.maxent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestMaxent extends TestCase {
	
	public TestMaxent (String name)
	{
		super (name);
	}

	public void testPreProcessing(){
		Properties properties=new Properties();
		try{
			properties.load((Reader)new FileReader(new File("Properties.properties")));
		}catch(Exception ex){
			
		}
		System.out.println("PreProcessing");
		PreProcessData preProcess = new PreProcessData();
		File filTrain=null;
		try {
			filTrain = preProcess.iterOverFoldersandPreProcess(properties.getProperty("trainDataFolder"));//"/home/biju/Installers/apache-opennlp-1.5.2-incubating/20news-bydate/20news-bydate-train");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue (filTrain!=null);
		System.out.println("PreProcessing Complete");
		System.out.println("Training Model");
		Trainer train = new Trainer();
		DoccatModel model = train.train(filTrain);
		assertTrue(model!=null);
		System.out.println("Training Complete");
		
		File fileTrainedModel=new File(filTrain.getAbsolutePath().replaceAll("\\.train$",".bin"));
		Util.writeModelToFile(model,fileTrainedModel);
		
		System.out.println("Model Written To File");
		Classifier classify=new Classifier(model);
		System.out.println("Classifying Single Trained Document");
		String classification=classify.classify("from dericks plains nodak edu dale erickson subject telix problem nntp posting host plains nodak edu organization north dakota higher education computing network lines when i use telix or kermit in win or use telix after exiting windows to dos telix can not find the serial port if you have some ideas on how to solve this problem or where i can find further information send me email or send it to the news group thanks dale erickson dericks plains nodak edu");
		System.out.println("Classified as:"+classification);
		assertTrue("comp.os.ms-windows.misc".equals(classification));
		
		System.out.println("Calculating Accuracy with Trained Documents");
		calcAccuracy(classify,filTrain);//new File("/home/biju/Installers/apache-opennlp-1.5.2-incubating/20news-bydate/20news-bydate-train/20news-bydate-train.train"));
		
		System.out.println("Calculating Accuracy with Non Trained Documents");
		File filTestDocs=null;
		try {
			filTestDocs = preProcess.iterOverFoldersandPreProcess(properties.getProperty("testDataFolder"));//"/home/biju/Installers/apache-opennlp-1.5.2-incubating/20news-bydate/20news-bydate-test");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		calcAccuracy(classify,filTestDocs);
		
	}
	
	public static void calcAccuracy(Classifier classify,File filPreprocessedTrainFile){
		InputStream dataIn = null;
	    //Train Model
		int iDocCount=0;
		int iClassifiedExpect=0;
		int iClassifiedWrong=0;
	    try {
	      dataIn = new FileInputStream(filPreprocessedTrainFile);
	      ObjectStream<String> lineStream =
	    		new PlainTextByLineStream(dataIn, "UTF-8");
	      ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);
	      DocumentSample sample=null;
	      while((sample=sampleStream.read())!=null){
	    	  iDocCount++;
	    	  String strExpected=sample.getCategory();
	    	  String strContent=StringUtils.join(sample.getText(), " ");
	    	  String strClassified=classify.classify(strContent);
	    	  if(strClassified.equals(strExpected)){
	    		  iClassifiedExpect++;
	    	  }else{
	    		  iClassifiedWrong++;
	    	  }
	    	  
	      }
	    }
	    catch (IOException e) {

	      e.printStackTrace();
	    }
	    finally {
	      if (dataIn != null) {
	        try {
	          dataIn.close();
	        }
	        catch (IOException e)
	        {
	          e.printStackTrace();
	        }
	      }
	    }
	    System.out.println("Accuracy on "+filPreprocessedTrainFile.getName());
	    System.out.println("Total Document:"+iDocCount);
	    System.out.println("Correctly Classified:"+iClassifiedExpect);
	    System.out.println("Wrongly Classified:"+iClassifiedWrong);
	    System.out.println("Accuracy:"+((iClassifiedExpect*100)/iDocCount));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		junit.textui.TestRunner.run (suite());
	}
	
	static Test suite ()
	{
		return new TestSuite (TestMaxent.class);
		//TestSuite suite= new TestSuite();
		//   //suite.addTest(new TestNaiveBayes("testIncrementallyTrained"));
		// suite.addTest(new TestNaiveBayes("testEmptyStringBug"));

		// return suite;
	}
	protected void setUp ()
	{
	}

}
