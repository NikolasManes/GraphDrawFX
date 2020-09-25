package main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private int newGPathStartID = -1;
    private int newGPathEndID = -1;
    private Line linePath;

    private Graph graph = new Graph();
    @FXML
    private Pane pane;
    @FXML
    private RadioButton addNodes;
    @FXML
    private RadioButton addPaths;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void rectClick(MouseEvent mouseEvent) {
        if (addNodes.isSelected()){
            GNode node = new GNode();
            Circle circle = new Circle(4);
            Text idTXT = new Text(String.valueOf(node.getId()));
            circle.setFill(Color.BLACK);
            circle.relocate(mouseEvent.getX(), mouseEvent.getY());
            circle.setOnMouseClicked(this::rectClick);
            circle.setId(String.valueOf(node.getId()));
            idTXT.relocate(mouseEvent.getX() + 10, mouseEvent.getY() - 15);
            pane.getChildren().addAll(circle, idTXT);
            graph.addNode(node);
        } else if (addPaths.isSelected()){
            System.out.println(mouseEvent.getPickResult().getIntersectedNode().getId());
            if(newGPathStartID == -1){
                newGPathStartID = Integer.valueOf(mouseEvent.getPickResult().getIntersectedNode().getId());
                linePath = new Line();
                linePath.setFill(Color.BLACK);
                linePath.setStartX(mouseEvent.getX());
                linePath.setStartY(mouseEvent.getY());
            } else if(newGPathStartID != -1 && newGPathEndID == -1) {
                newGPathEndID = Integer.valueOf(mouseEvent.getPickResult().getIntersectedNode().getId());
                try {
                    GPath path = new GPath(graph.getSingleNode(newGPathStartID), graph.getSingleNode(newGPathEndID), 1);
                    graph.addPathToGraph(path);
                } catch (NodeNotInGraphException e) {
                    e.printStackTrace();
                }
                linePath.setEndX(mouseEvent.getX());
                linePath.setEndY(mouseEvent.getY());
                pane.getChildren().addAll(linePath);
                newGPathStartID = -1;
                newGPathEndID = -1;
            }
        }
        graph.printGraph();
    }


}
