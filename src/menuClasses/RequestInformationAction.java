package menuClasses;

import generalClasses.P3Utils;
import ioManagementClasses.IOComponent;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Map;

import dataManagement.DocsIDManager;
import dataManagement.Document;
import dataManagement.DocumentIDX;
import dataManagement.WordInDocument;

public class RequestInformationAction implements Action {
	private static IOComponent io = IOComponent.getComponent(); 

	@Override
	public void execute(Object args) throws IOException {

		String fName = io.getInput("\nPlease input name of the file you wish to learn about: \n(Input * to learn about every file) \n");
		if(fName.equalsIgnoreCase("*")){
			//Analyzing every file in the docs directory
			File folder = P3Utils.DocsDirectoryPath;
			File[] fList = folder.listFiles();
			//Loop through every file in directory
			for (int i = 0; i < fList.length; i++) {
				if (fList[i].isFile()) {
					io.output("File: " + fList[i].getName() + "\nStatus: "+ analizeFile(fList[i].getName()) + "\n\n");
				}
			}
		}
		//just analyzes the given file name
		else
			io.output("File: " + fName + "\nStatus: "+ analizeFile(fName) + "\n\n");
	}

	/**
	 * Validates given file name, checks if its added.
	 * If it is it checks if the file content has changed since it was registered
	 * @param fName string representing name of desired registered file
	 * @return string representing status of file, UpToDate/OutOfDate/NotAdded
	 * @throws IOException
	 */
	private static String analizeFile(String fName) throws IOException{
		File fPath = new File(P3Utils.DocsDirectoryPath, fName);
		if (!fPath.exists()) //Check if file exists before validating so program doesn't throw an exception.
			return "File does not exist! Please input an existing file name.\n";
		
		String status = "";
		File docFPath = P3Utils.validateDocumentFile(fName); 
		RandomAccessFile docFile = new RandomAccessFile(docFPath, "r");	
		DocsIDManager didm = DocsIDManager.getInstance();
		int id = didm.isRegisteredDoc(fName);
		didm.close();

		if(id>0){
			Document currentDoc = new Document(docFile);
			DocumentIDX idx = new DocumentIDX(id);
			Boolean upToDate = true;
			Map<String, ArrayList<Integer>> index = idx.getWLM();

			for(WordInDocument w : currentDoc){
				if(!upToDate)
					return "Out Of Date";
				if(index.get(w.getWord())==null)
					upToDate = false;
				else
					upToDate = index.get(w.getWord()).contains((int)w.getLocation());
			}
			status = "Up To Date";
		}
		else
			status = "Not Added";
		return status;
	}
}
