package menuClasses;

import java.io.File;
import java.io.IOException;

import systemClasses.SystemController;
import dataManagement.DocsIDManager;
import dataManagement.MainIndexManager;
import generalClasses.P3Utils;
import ioManagementClasses.IOComponent;
public class RemoveDocumentAction implements Action {
	private static IOComponent io = IOComponent.getComponent();

	@Override
	public void execute(Object args) throws IOException {
		DocsIDManager didm = DocsIDManager.getInstance();
		MainIndexManager mim = MainIndexManager.getInstance();
		String fName = io.getInput("\nPlease Enter File To Remove: \n");
		int id = didm.isRegisteredDoc(fName);
		
		if(id<=0)
			io.output("Index does not exist.");
		else{
			//id is a valid removable file
			String ntd = SystemController.makeIDXName(id); //name of idx file to delete
			File idxFile = new File(P3Utils.IndexDirectoryPath, ntd); 
			idxFile.delete(); // removes idx file
			didm.removeDocID(id); // removes id from map
			mim.removeIndex(id); // removes index
			io.output("\n~File has been removed!~\n");
			didm.close();
			mim.close();
		}
	}
}