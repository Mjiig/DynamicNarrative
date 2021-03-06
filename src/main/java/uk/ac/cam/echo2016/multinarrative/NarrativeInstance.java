package uk.ac.cam.echo2016.multinarrative;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.BaseBundle;
import uk.ac.cam.echo2016.multinarrative.dev.Debug;

/**
 * 
 * Represents an actual play through of the story. Instantiated from the template.
 * 
 * @author tr39
 * @author rjm232
 * @author jr650
 * @version 1.0
 *
 */
public class NarrativeInstance extends MultiNarrative { // TODO Documentation
    private static final long serialVersionUID = 1;
    protected ArrayList<Node> activeNodes = new ArrayList<Node>();

    public NarrativeInstance(HashMap<String, Route> routes, HashMap<String, Node> nodes, SynchronizationNode start, BaseBundle properties) {
        this.routes = routes;
        this.nodes = nodes;
        this.start = start;
        this.properties = properties;
    }

    public NarrativeInstance() { }
    
    public BaseBundle startRoute(String id) throws GraphElementNotFoundException {
        Route route = getRoute(id);
        if (route == null) throw new GraphElementNotFoundException(id);
        Node startNode = route.getStart();
        if (startNode instanceof ChoiceNode) {
        	activeNodes.remove(startNode);
        	for (Route deadRoute : startNode.getExiting()) {
        		if (deadRoute != route) {
        			kill(deadRoute); 
        		}
        	}
        } else {
        	if (startNode.getExiting().size() == 1) {
        		activeNodes.remove(startNode);
        	}
        }
        return startNode.startRoute(route);
    }

    public GameChoice endRoute(String id) throws GraphElementNotFoundException {
        Route route = getRoute(id);
        if (route == null) throw new GraphElementNotFoundException(id);
        Node endNode = route.getEnd();
        if (endNode instanceof ChoiceNode || ((SynchronizationNode)endNode).isCompleted()) {
        	setActive(endNode);
        }
        route.getProperties().putBoolean("System.isCompleted", true);
        return endNode.onEntry(route, this);
    }

    /**
     * Recursively deletes an item from the graph according to the instance this method is called from. Only nodes and
     * routes further down the tree are deleted, so nodes must have no entering routes and routes must start
     * from a node with other exiting routes available.
     * 
     * @param id
     *            string identifier for the item to be deleted
     * @return
     */
    public boolean kill(String id) { // TODO More Documentation, including overloaded methods
        Route route = getRoute(id);
        if (route != null) {
            kill(route);
            return true; // TODO change to throw GraphElementNotFoundException?
        } else {
            Node node = getNode(id);
            if (node != null) {
                kill(node);
                return true;
            }
            return false; // TODO change to throw GraphElementNotFoundException?
        }
    }

    /**
     * {@link NarrativeInstance#kill(String)}
     * 
     * @see NarrativeInstance#kill(String)
     */
    public boolean kill(Route route) {
        if (route == null)
            return false;
        Node nEnd = route.getEnd();
        
        Debug.logInfo("Killing " + route.getId(), 4, Debug.SYSTEM_ALL);

        nEnd.getEntering().remove(route);
        // If there are now no routes entering the node, kill it
        if (nEnd.getEntering().size() == 0) {
            kill(nEnd);
        } else if (route.getProperties() != null) {
//            // Kills all methods leaving the end node if they have the same type and no entering routes also
//            // have that property TODO specify in documentation
            for (String key : route.getProperties().keySet()) {
                if (this.getGlobalProperties().getStringArrayList("System.Types").contains(key)) { // TODO error if no global "Types" property
                    Object type = route.getProperties().get(key);
                    
                    boolean similarRouteExists = false;
                    for(Route entry : nEnd.getEntering()) {
                        if (entry.getProperties().containsKey(key) && entry.getProperties().get(key).equals(type)) {
                            similarRouteExists = true;
                        }
                    }
                    if (!similarRouteExists) {
                        for(Route option : new ArrayList<Route>(nEnd.getExiting())) {
                            if (option.getProperties().containsKey(key) && option.getProperties().get(key).equals(type)) {
                                kill(option);
                            }
                        }
                    }
                }
            }
        }
        // Remove the route from the exiting routes of the node it comes from
        Node nStart = route.getStart();
        nStart.getExiting().remove(route); // Should return true, otherwise something's broken

        routes.remove(route.getId());
        return true;
    }

    /**
     * {@link NarrativeInstance#kill(String)}
     * 
     * @see NarrativeInstance#kill(String)
     */
    public boolean kill(Node node) {
        if (node == null)
            return false;
        for (Route route : new ArrayList<Route>(node.getExiting())) {
            kill(route); // Copy of ArrayList used to allow deletion of nodes within the function
        }

        // As specified in the javadoc
        assert node.getEntering().size() == 0;
        
        nodes.remove(node.getId());
        return true;
    }

    public ArrayList<Route> getPlayableRoutes() {
        ArrayList<Route> r_routes = new ArrayList<Route>();
        for (Node node : activeNodes) {
            for (Route route : node.getExiting()) {
                if (!route.getProperties().getBoolean("System.isCompleted")) {
                    r_routes.add(route);
                }
            }
        }
        return r_routes;
    }

    public void setActive(Node node) {
        if (!activeNodes.contains(node))
            activeNodes.add(node);
    }
}
