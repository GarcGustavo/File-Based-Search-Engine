package menuClasses;

import generalClasses.P3Utils;
import ioManagementClasses.IOComponent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import dataManagement.DocsIDManager;
import dataManagement.Document;
import dataManagement.DocumentIDX;
import dataManagement.WordInDocument;

public class RequestInformationAction implements Action {
	private static IOComponent io = IOComponent.getComponent(); 
	
	@Override
	public void execute(Object args) throws IOException {
		
		String fName = ""; 
		fName = io.getInput("\nPlease input name of the file you wish to learn about: \n(Input * to learn about every file) \n");
		if(fName.equalsIgnoreCase("*")){
			DocsIDManager didm = DocsIDManager.getInstance();
			ArrayList<String> names = didm.getDocNamesList();
			//Analyzing every registered file name
			for(String s : names){
				io.output("File: " + s + "\nStatus: "+ analizeFile(s) + "\n\n");
			}
		}
		//else is just analyzes the given file name
		else
			io.output("File: " + fName + "\nStatus: "+ analizeFile(fName) + "\n\n");
	}
	
	/**
	 * @param fName string representing name of desired registered file
	 * @return string representing status of file, UpToDate/OutOfDate/NotAdded
	 * @throws IOException
	 */
	private static String analizeFile(String fName) throws IOException{
		String result = "yo";
		DocsIDManager didm = null;
		File docFilePath;
		
		docFilePath = P3Utils.validateDocumentFile(fName); 
		RandomAccessFile docFile = new RandomAccessFile(docFilePath, "r");	
		didm = DocsIDManager.getInstance();
		int id = didm.isRegisteredDoc(fName);
		
		if(id>0){
			Document currentDoc = new Document(docFile);
			DocumentIDX indexTester = new DocumentIDX(id);
			Map<String, ArrayList<Integer>> indexWLM = indexTester.getWLM();
			Boolean upToDate = true;
			
			for(WordInDocument wid : currentDoc){
				if(!upToDate)
					return "Out Of Date";
				if(indexWLM.get(wid.getWord())==null)
					upToDate = false;
				else
					upToDate = indexWLM.get(wid.getWord()).contains((int)wid.getLocation());
			}
			result = "Up To Date";
		}
		else
			result = "Not Added";
		return result;
	}
}
