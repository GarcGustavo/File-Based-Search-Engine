package menuClasses;

import java.io.IOException;

/**
 * Any object of this type must have a method that executes 
 * necessary algorithm to carry-out the action it represents. 
 * @author pedroirivera-vega
 *
 */
public interface Action {
	void execute(Object args) throws IOException; 
}
