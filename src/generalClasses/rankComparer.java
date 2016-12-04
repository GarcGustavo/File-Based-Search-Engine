package generalClasses;

import java.util.Comparator;
import dataManagement.MatchingSearchDocument;

public class rankComparer implements Comparator<MatchingSearchDocument>{

	@Override
	//Comparator used to sort documents by calculated frequency rank
	public int compare(MatchingSearchDocument doc1, MatchingSearchDocument doc2) {
		if(doc1.getRank()<doc2.getRank())
			return 1;
		else if(doc1.getRank()>doc2.getRank())
			return -1;
		else
			return 0;
	}
	
}
