package sample.classifier.maxent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

public class PreProcessData {
	public File iterOverFoldersandPreProcess(String strFolderPath) throws IOException{
		FilenameFilter filterTxtFile=new FilenameFilter() { 
            public boolean accept(File dir, String filename)
            { return filename.endsWith(".txt"); }
		} ;
		File filTrainDataDir=new File(strFolderPath);
		FileOutputStream trainFilestream=null;
		File filTrain=new File(strFolderPath+File.separatorChar+filTrainDataDir.getName()+".train");
		trainFilestream = new FileOutputStream(filTrain);
		OutputStreamWriter trainDataWrite = new OutputStreamWriter(trainFilestream,"UTF-8");
        
		for(File filClassDataDir:filTrainDataDir.listFiles()){
			if(filClassDataDir.isDirectory()){
				String strClassName=filClassDataDir.getName();
				for(File filTxtData:filClassDataDir.listFiles()){
					String strTrainData=Util.readFile(filTxtData);
					strTrainData=preprocessData(strTrainData);
					trainDataWrite.write(strClassName+"\t"+strTrainData+"\n");
				}
					
			}
		}
       	trainDataWrite.close();
		return filTrain;
	}
	private String preprocessData(String strTrainData) throws IOException {
		// TODO Auto-generated method stub
		Analyzer analyzer = new SimpleAnalyzer();
		StringReader stringReader = new StringReader(strTrainData);
		//String formattedDocument = readerToDoc(analyzer, stringReader);
		return readerToDoc(analyzer, stringReader);
	}
	
	private static String readerToDoc(Analyzer analyzer, Reader reader)
			throws IOException {

		TokenStream ts = analyzer.tokenStream("", reader);
		List<String> coll = new ArrayList<String>();

		TermAttribute termAtt = ts.addAttribute(TermAttribute.class);
		while (ts.incrementToken()) {
			char[] termBuffer = termAtt.termBuffer();
			int termLen = termAtt.termLength();
			String val = new String(termBuffer, 0, termLen);
			coll.add(val);
		}

		StringBuffer strbuffer = new StringBuffer();
		for (String word : coll) {
			strbuffer.append(word + " ");

		}
		// return coll.toString();
		String text = null;
		text = strbuffer.toString();
		return text;
	}
}
