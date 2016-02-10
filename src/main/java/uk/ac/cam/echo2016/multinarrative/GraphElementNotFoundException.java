package uk.ac.cam.echo2016.multinarrative;

/**
 * This is thrown if an id to an expected {@code Route} or {@code Node} in the {@code MultiNarrative} 
 * 
 * @author rjm232
 * @author tr393
 *
 */

public class GraphElementNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	
    public GraphElementNotFoundException() {
        super();
    }

    public GraphElementNotFoundException(String message) {
        super(message);
    }

    public GraphElementNotFoundException(Throwable cause) {
        super(cause);
    }

    public GraphElementNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}

