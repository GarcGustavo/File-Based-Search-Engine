package menuClasses;

import generalClasses.rankComparer;
import ioManagementClasses.IOComponent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import dataManagement.MatchingSearchDocument;
import systemClasses.SystemController;

public class PerformSearchesAction implements Action {
	private static IOComponent io = IOComponent.getComponent(); 
	private int searchListSize = 0;

	@Override
	/**
	 * 
	 */
	public void execute(Object arg) {
		SystemController sc = (SystemController) arg; 
		String answer = "y"; 
		while (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes")) {
			io.output("\nSearching Based on Words:\n"); 
			String words = io.getInput("\nEnter words to search for (separate by spaces): "); 
			Map<Integer, MatchingSearchDocument> matchingDocuments = null;
			try {
				StringTokenizer wordsTokens = new StringTokenizer(words); 
				ArrayList<String> wordsList = constructListOfSearchWords(wordsTokens);
				searchListSize = wordsList.size(); //used for ranking formula
				matchingDocuments = sc.search(wordsList);
				if (matchingDocuments.isEmpty()) 
					io.output("No document matches this search.");
				else 
					processMatchingDocuments(matchingDocuments); 
			} catch (IOException e) {
				e.printStackTrace();
			}
			answer = io.getInput("\n\n*** Do you want to perform another search: (y/n)? "); 
		}
	}

	/**
	 * 
	 * @param matchingDocuments
	 * @throws IOException
	 */
	private void processMatchingDocuments(Map<Integer, MatchingSearchDocument> matchingDocuments) throws IOException {
		ArrayList<MatchingSearchDocument> rankedDocuments = rankMatchingDocuments(matchingDocuments); 
		displayHeaderLinesMatchingDocuments(rankedDocuments); 
		String answer = "y"; 
		while (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes")) { 
			int docIndex = io.getInputInteger("\n\n---Please, enter the number of document to see: "); 
			if (docIndex < 1 || docIndex > rankedDocuments.size()) 
				io.output("Invalid index number: " + docIndex);
			else {
				io.output("\n+++++Content of document ranked: " + docIndex + " +++++ \n\n");
				rankedDocuments.get(docIndex-1).displayDocument(0);
			}
			answer = io.getInput("\n\n*** Do you want to display another document: (y/n)? "); 
		}
	}

	/**
	 * 
	 * @param rankedDocuments
	 * @throws IOException 
	 */
	private void displayHeaderLinesMatchingDocuments(ArrayList<MatchingSearchDocument> rankedDocuments) throws IOException {
		
		for (int i=0; i<rankedDocuments.size(); i++) { 
			io.output("\n\n****DOCUMENT " + (i+1) + "****\n");
			rankedDocuments.get(i).displayDocument(3);
		}
		
	}

	/**
	 * Creates an array list sorted by calculated rank
	 * @param matchingDocuments
	 * @return List containing documents in non-decreasing order based on frequency rank
	 * @throws IOException
	 */
	private ArrayList<MatchingSearchDocument> rankMatchingDocuments(Map<Integer, MatchingSearchDocument> matchingDocuments) throws IOException {
		
		ArrayList<MatchingSearchDocument> rankedDocuments = new ArrayList<>(); 
		rankComparer cmpRank = new rankComparer(); //custom comparator that compares rank of matching documents
		
		//creates an array list with documents
		for (Entry<Integer, MatchingSearchDocument> e : matchingDocuments.entrySet()){
			e.getValue().setSearchSize(searchListSize); //sends size of search terms to document so it can formulate rank
			rankedDocuments.add(e.getValue());
		}
		rankedDocuments.sort(cmpRank); //sorts array list by rank
		return rankedDocuments; 
	}

	/**
	 * 
	 * @param wordsTokens
	 * @return
	 */
	private ArrayList<String> constructListOfSearchWords(StringTokenizer wordsTokens) {
		ArrayList<String> uniqueWordsList = new ArrayList<>(); 
		while (wordsTokens.hasMoreTokens()) { 
			String word = wordsTokens.nextToken(); 
			if (!uniqueWordsList.contains(word))
				uniqueWordsList.add(word); 
		} 

		return uniqueWordsList;
	}
}


