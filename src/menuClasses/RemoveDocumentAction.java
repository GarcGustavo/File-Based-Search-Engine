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
	public void execute(Object args) {
		DocsIDManager didm = null;
		MainIndexManager mim = null;
		String fName = ""; 
		fName = io.getInput("\nPlease Enter File To Remove: \n");

		try {
			didm = DocsIDManager.getInstance();
			mim = MainIndexManager.getInstance();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int id = didm.isRegisteredDoc(fName);

		if(id<=0){
			io.output("Index does not exist.");

		}else{
			String dName = SystemController.makeIDXName(id);
			File idxFilePath = new File(P3Utils.IndexDirectoryPath, dName); 
			idxFilePath.delete();
			didm.removeDocID(id);
			mim.removeIndex(id);
			didm.close();
			mim.close();
		}
	}
	public void removeIdx(String fName){	
	}
}