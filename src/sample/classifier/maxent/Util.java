package sample.classifier.maxent;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.util.InvalidFormatException;

public class Util {
	public static String readFile(File file) throws IOException {
		FileInputStream stream = new FileInputStream(file);
		try {
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			/* Instead of using default, pass in a decoder. */
			return Charset.defaultCharset().decode(bb).toString();
		}
		finally {
			stream.close();
		}
	}
	public static void writeModelToFile(DoccatModel model,File fileToWrite){
		OutputStream modelOut = null;
	    try {
	    	modelOut = new BufferedOutputStream(new FileOutputStream(fileToWrite));
	    	model.serialize(modelOut);
	    }
	    catch (IOException e) {
	    	// Failed to save model
	    	e.printStackTrace();
	    }
	    finally {
	    	if (modelOut != null) {
	    		try {
	    			modelOut.close();
	    		}
	    		catch (IOException e) {
	    			// Failed to correctly save model.
	    			// Written model might be invalid.
	    			e.printStackTrace();
	    		}
	    	}
	    }
	}
	public static DoccatModel loadModel(File fileModel) throws InvalidFormatException, IOException{
		InputStream is =new FileInputStream(fileModel);
		return new DoccatModel(is);
	}
}
