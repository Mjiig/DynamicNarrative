package uk.ac.cam.echo2016.multinarrative.gui;

import java.util.HashSet;
import java.util.Set;

import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import uk.ac.cam.echo2016.multinarrative.gui.graph.Graph;
import uk.ac.cam.echo2016.multinarrative.gui.graph.GraphEdge;
import uk.ac.cam.echo2016.multinarrative.gui.graph.GraphNode;
import uk.ac.cam.echo2016.multinarrative.gui.graph.GraphTool;
import uk.ac.cam.echo2016.multinarrative.gui.operations.IllegalOperationException;

public class EditingTool implements GraphTool {

    public static final PseudoClass SELECTED = PseudoClass.getPseudoClass("selected");

    private boolean dragging = false;

    private GraphNode start;
    private GraphEdge press;
    private boolean selectMade;

    private double mouseX;
    private double mouseY;

    private double distX;
    private double distY;

    private Graph graph;

    private Set<GraphNode> selection = new HashSet<GraphNode>();

    public EditingTool(Graph graph) {
        this.graph = graph;
    }

    public void resetSelection() {
        for (GraphNode node : selection) {
            node.getContents().pseudoClassStateChanged(SELECTED, false);
        }
        selection.clear();
    }

    public void select(GraphNode node) {
        node.getContents().pseudoClassStateChanged(SELECTED, true);
        selection.add(node);
    }

    public Set<GraphNode> getSelection() {
        return selection;
    }

    public boolean isSelected(GraphNode node) {
        return selection.contains(node);
    }

    public void deselect(GraphNode node) {
        node.getContents().pseudoClassStateChanged(SELECTED, false);
        selection.remove(node);
    }

    public void setSelection(String s) {
        GraphNode node = graph.getNodes().get(s);
        if (node != null) {
            resetSelection();
            select(node);
        }
    }

    @Override
    public void mousePressed(MouseEvent event) {
        mouseX = event.getSceneX();
        mouseY = event.getSceneY();
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        if (!dragging) {
            if (event.isShiftDown()) {
                try {
                    if (press != null) {
                        String name = graph.getOperations().narrativeOperations().getUniqueNodeName();
                        graph.getOperations().doOp(graph.getOperations().generator().addChoiceNode(name, press));
                    } else if (start == null) {
                        String name = graph.getOperations().narrativeOperations().getUniqueNodeName();
                        graph.getOperations()
                                .doOp(graph.getOperations().generator().addSynchNode(name, event.getX(), event.getY()));
                    }
                } catch (IllegalOperationException ioe) {
                    graph.getController().setInfo(ioe.getMessage());
                }
            } else if (press == null && !selectMade) {
                resetSelection();
                graph.getController().deselect();
            }
        } else {
            for (GraphNode node : selection) {
                try {
                    node.translate(-distX, -distY);
                    graph.getOperations()
                            .doOp(graph.getOperations().generator().translateNode(node.getName(), distX, distY));
                    graph.getController().initSelect();
                } catch (IllegalOperationException e) {
                    graph.getController().setInfo(e.getMessage());
                }
            }
        }

        dragging = false;
        selectMade = false;
        start = null;
        press = null;
        mouseX = Double.NaN;
        mouseY = Double.NaN;
        distX = 0;
        distY = 0;
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        if (!selectMade) {
            resetSelection();
            selectMade = true;
        }
        if (!dragging) {
            dragging = true;
        } else if (!event.isShiftDown()) {
            double movementX = mouseX == mouseX ? event.getSceneX() - mouseX : 0.0;
            double movementY = mouseY == mouseY ? event.getSceneY() - mouseY : 0.0;
            distX += movementX;
            distY += movementY;
            if (press != null) {
                press.translate(movementX, movementY);
            } else if (!selection.isEmpty()) {
                for (GraphNode node : selection) {
                    node.translate(movementX, movementY);
                    graph.getController().initSelect();
                }
            }
        }

        mouseX = event.getSceneX();
        mouseY = event.getSceneY();
    }

    @Override
    public void mousePressedOnNode(MouseEvent event, GraphNode node) {
        if (!event.isShiftDown() && !isSelected(node)) {
            resetSelection();
            select(node);
        }
        start = node;
        selectMade = true;
        mousePressed(event);
        graph.getController().selectNode(node.getName());
    }

    @Override
    public void mousePressedOnEdge(MouseEvent event, GraphEdge edge) {
        press = edge;
        mousePressed(event);
        graph.getController().selectRoute(edge.getName());

    }

    @Override
    public void mouseReleasedOnNode(MouseEvent event, GraphNode node) {
        if (event.isShiftDown() && start != null && start != node) {
            String name = graph.getOperations().narrativeOperations().getUniqueRouteName();
            try {
                graph.getOperations()
                        .doOp(graph.getOperations().generator().addRoute(name, start.getName(), node.getName()));
            } catch (IllegalOperationException ioe) {
                graph.getController().setInfo(ioe.getMessage());
            }
        }
    }

    @Override
    public void mouseReleasedOnEdge(MouseEvent event, GraphEdge edge) {
    }

    @Override
    public void dragStart(MouseEvent event) {
        dragging = true;
        if (event.isShiftDown()) {
            if (event.getSource() instanceof Node) {
                ((Node) event.getSource()).startFullDrag();
            }
        }
    }

}
