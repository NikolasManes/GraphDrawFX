package main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

public class Controller implements Initializable {

    private Graph graph = new Graph();
    private boolean nodeIsSelected;
    private GNode startNode;
    private GNode endNode;
    private int pathWeight;
    private DecimalFormat towDigitsFormat = new DecimalFormat("00");
    private Color strokeColorDefault = Color.RED;
    private Color fillColorDefault = Color.BLACK;
    private Color nodeColorDefault = Color.BLUE;
    private Color pathColorDefault = Color.PURPLE;
    @FXML
    private Canvas canvas;
    private GraphicsContext graphicsContext;
    @FXML
    private RadioButton addNodes;
    @FXML
    private RadioButton addPaths;
    @FXML
    private ChoiceBox choiceBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nodeIsSelected = false;
        graphicsContext = canvas.getGraphicsContext2D();
        setScene();
    }

    public void setScene(){
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        graphicsContext.setFill(Color.LIGHTGRAY);
        graphicsContext.fillRect(0,0, canvas.getWidth(), canvas.getHeight());
        graphicsContext.setFill(fillColorDefault);
        graphicsContext.setStroke(strokeColorDefault);    }

    public void canvasClick(MouseEvent mouseEvent) {
        if (addNodes.isSelected()){
            GNode node = new GNode(mouseEvent.getX(), mouseEvent.getY());
            graph.addNode(node);
            drawGraph();
        } else if (addPaths.isSelected()){
            System.out.println(" nodeIsSelected: " + nodeIsSelected);
            if(nodeIsSelected){
                for (GNode gNode: graph.getNodes()){
                    if (gNode.nodeSelected(mouseEvent.getX(), mouseEvent.getY())){
                        nodeIsSelected = false;
                        endNode = gNode;
                        TextInputDialog dialog = new TextInputDialog("0");
                        dialog.setTitle("Path Weight");
                        dialog.setContentText("Please enter path's weight: ");
                        Optional<String> result = dialog.showAndWait();
                        result.ifPresent(weight -> pathWeight = Integer.parseInt(weight));
                        GPath path = new GPath(startNode, endNode, pathWeight);
                        graph.addPathToGraph(path);
                        drawGraph();
                        break;
                    }
                }
            } else{
                for (GNode gNode: graph.getNodes()){
                    if (gNode.nodeSelected(mouseEvent.getX(), mouseEvent.getY())){
                        graphicsContext.strokeOval(gNode.getCordX()-15, gNode.getCordY()-15, 30, 30);
                        nodeIsSelected = true;
                        startNode = gNode;
                        break;
                    }
                }
            }
        }
        graph.printGraph();
    }

    private void drawGraph() {
        setScene();
        graphicsContext.setStroke(nodeColorDefault);
        for (GNode gNode: graph.getNodes()){
            graphicsContext.strokeOval(gNode.getCordX()-10, gNode.getCordY()-10, 20, 20);
            graphicsContext.strokeText(towDigitsFormat.format(gNode.getId()), gNode.getCordX()-6.5, gNode.getCordY()+4);
        }
        graphicsContext.setStroke(pathColorDefault);
        graphicsContext.setFill(pathColorDefault);
        for (GPath gPath: graph.getPaths()){
            drawPath(graphicsContext, gPath.getStart().getCordX(), gPath.getStart().getCordY(), gPath.getEnd().getCordX(), gPath.getEnd().getCordY(), gPath.getWeight());
        }
        graphicsContext.setStroke(strokeColorDefault);
        graphicsContext.setFill(fillColorDefault);
    }

    private void drawPath(GraphicsContext gc, double x1, double y1, double x2, double y2, int weight) {

        double dx = x2 - x1;
        double dy = y2 - y1;
        double midX = x1+(dx/2);
        double midY = y1+(dy/2);
        double angle = Math.atan2(dy, dx);
        double arrowAngle = Math.PI/6;
        double point1X = midX+10*Math.cos(Math.PI-arrowAngle+angle);
        double point1Y = midY+10*Math.sin(Math.PI-arrowAngle+angle);
        double point2X = midX+10*Math.cos(Math.PI+angle+arrowAngle);
        double point2Y = midY+10*Math.sin(Math.PI+angle+arrowAngle);

        gc.strokeLine(x1+10*Math.cos(angle), y1+10*Math.sin(angle), x2-10*Math.cos(angle), y2-10*Math.sin(angle));
        gc.fillPolygon(new double[]{midX, point1X, point2X, midX}, new double[]{midY, point1Y, point2Y, midY}, 4);
        gc.strokeText(String.valueOf(weight), midX-10*Math.cos(angle), midY+10*Math.sin(angle));
    }

    public void runAlgorithm(MouseEvent mouseEvent) {
        System.out.println(choiceBox.getValue().toString());

        switch (choiceBox.getValue().toString()){
            case "BFS":
                System.out.println("BFS "+ choiceBox.getValue().toString());
                break;
            case "DFS":
                System.out.println("DFS "+ choiceBox.getValue().toString());
                break;
            case "Prim":
                System.out.println("Prim "+ choiceBox.getValue().toString());
                break;
            case "Dijkstra":
                System.out.println("Dijkstra "+ choiceBox.getValue().toString());
                break;

        }
    }
    public static GRoute runBFS(Graph graph, int startID, int endID){
        List<GNode> frontier = new ArrayList<>();
        List<GNode> exploredNodes = new ArrayList<>();
        List<GRoute> routes = new ArrayList<>();
        List<GRoute> routesToAdd = new ArrayList<>();
        try {
            GRoute startRoute = new GRoute(graph.getSingleNode(startID), graph.getSingleNode(startID), new ArrayList<>());
            routes.add(startRoute);
        } catch (NodeNotInGraphException e) {
            e.printStackTrace();
        }
        try {
            frontier.add(graph.getSingleNode(startID));
        } catch (NodeNotInGraphException e) {
            System.out.println(e.getMessage());
        }
        while (!frontier.isEmpty()) {
            List<GNode> dummyFrontierList = new ArrayList<>(frontier);
            for (GNode node : dummyFrontierList) {
                GNode toCheck = node;
                frontier.remove(node);
                if (toCheck.getId() == endID) {
                    System.out.println("Route found!");
                    for (GRoute route : routes) {
                        if (route.getEndPoint().getId() == endID) {
                            route.printRoute();
                            return route;
                        }
                    }
                }
                System.out.println(node.getId());
                exploredNodes.add(toCheck);
                for (GPath path : graph.getPaths()) {
                    if (path.getStart().getId() == toCheck.getId()) {
                        if (frontier.contains(path.getEnd()) || exploredNodes.contains(path.getEnd())) {
                            continue;
                        }
                        frontier.add(path.getEnd());
                        for (GRoute r : routes) {
                            if (r.getEndPoint().getId() == toCheck.getId()) {
                                GRoute routetemp = new GRoute(r.getStartPoint(), r.getEndPoint(), new ArrayList<>(r.getPaths()));
                                try {
                                    routetemp.addPathToRoute(path);
                                    routetemp.printRoute();
                                } catch (PathCannotConnectToRouteException e) {
                                    e.printStackTrace();
                                }
                                routesToAdd.add(routetemp);
                            }
                        }
                        routes.addAll(routesToAdd);
                        routesToAdd.clear();
                    }
                }
            }
        }
        return null;
    }

    public static GRoute runDFS(Graph graph, int startID, int endID){
        Deque<GNode> nodesToCheck = new ArrayDeque<>();
        List<Integer> exploredNodes = new ArrayList<>();
        List<GRoute> routesCreated = new ArrayList<>();
        List<GRoute> routesToAdd = new ArrayList<>();
        try {
            GRoute dummyRoute = new GRoute(graph.getSingleNode(startID), graph.getSingleNode(startID), new ArrayList<>());
            routesCreated.add(dummyRoute);
            nodesToCheck.push(graph.getSingleNode(startID));
            while (!nodesToCheck.isEmpty()){
                GNode currentNode = nodesToCheck.pop();
                System.out.print("\nCurrent: ");
                System.out.print(currentNode.getId());
                if (exploredNodes.contains(currentNode.getId())){
                    System.out.print(" Explored!");
                    continue;
                }
                exploredNodes.add(currentNode.getId());
                for (GPath path: graph.getPaths()){
                    if (path.getStart().getId() == currentNode.getId()){
                        for (GRoute route: routesCreated){
                            if (route.getEndPoint().getId() == currentNode.getId()){
                                GRoute routeCreated = new GRoute(route.getStartPoint(), route.getEndPoint(), new ArrayList<>(route.getPaths()));
                                routeCreated.addPathToRoute(path);
                                if (routeCreated.getEndPoint().getId() == endID){
                                    return routeCreated;
                                }
                                routesToAdd.add(routeCreated);
                            }
                        }
                        routesCreated.addAll(routesToAdd);
                        routesToAdd.clear();
                        nodesToCheck.push(path.getEnd());
                        System.out.print("\nStack: ");
                        for (GNode n: nodesToCheck){
                            System.out.print(" " + n.getId());
                        }
                    }
                }
            }
        } catch (NodeNotInGraphException e) {
            e.printStackTrace();
        } catch (PathCannotConnectToRouteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void runPrim(Graph graph){
        System.out.println("Not implemented yet!");
    }

    public static List<GRoute> runDijkstra(Graph graph) {
        // Declare a list to store the routes
        List<GRoute> routes = new ArrayList<>();
        // Now add all the others the weight is MAX
        for (GNode node: graph.getNodes()){
            GRoute dummy = new GRoute(node, node, new ArrayList<>());
            routes.add(dummy);
        }
        // Set the first route for the first node weight is 0
        // - all others had been set to MAX-int value
        routes.get(0).setTotalWeight(0);
        // Declare a list to store the best routes
        List<GRoute> bestRoutes = new ArrayList<>();
        // Run Dijkstra
        int step = 0;
        while(bestRoutes.size()<graph.getNodeNumber()){
            step++;
            int pathCount = 0;
            System.out.println("\n ---- STEP: " + step + " ----\n");
            /* Get the shortest route of all to stabilize it */
            // Declare a route to store the temporary value
            GRoute shortestRoute;
            // Sort the routes according their weight
            routes.sort(Comparator.comparingInt(GRoute::getTotalWeight));
            // Remove the shortest route and add it to bestRoutes
            shortestRoute = routes.remove(0);
            bestRoutes.add(shortestRoute);
            System.out.println("The shortest route: ");
            shortestRoute.printRoute();
            // Find all paths starting from the end of that route
            for (GPath path: graph.getPaths()){
                if (shortestRoute.getEndPoint().getId() == path.getStart().getId()){
                    pathCount++;
                    System.out.println("*** The " + pathCount + " path starting from the end of that route:");
                    path.printPath();
                    // For every path we find check if there is a shorter route to the Node
                    for (GRoute route: routes){
                        if(route.getEndPoint().getId() == path.getEnd().getId()){
                            System.out.println("* Route with the same end as the path: ");
                            route.printRoute();
                            // Check if there is a shortest way to go to that node(end)
                            if(route.getTotalWeight() > shortestRoute.getTotalWeight() + path.getWeight()){
                                // Create a dummy route to store the route
                                GRoute dummyRoute = new GRoute(shortestRoute.getStartPoint(), shortestRoute.getEndPoint(), new ArrayList<>(shortestRoute.getPaths()));
                                try {
                                    dummyRoute.addPathToRoute(path);
                                } catch (PathCannotConnectToRouteException e) {
                                    System.out.println(e.getMessage());
                                }
                                routes.set(routes.indexOf(route), dummyRoute);
                                System.out.println("<<<New Route>>>");
                                route.printRoute();
                            }
                        }
                    }
                }
            }
        }
        System.out.println("\n\n!!!!! THE SHORTEST ROUTES !!!!!\n");
        for(GRoute r : bestRoutes){
            r.printRoute();
        }
        return bestRoutes;
    }
}
