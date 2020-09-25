package main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private Graph graph = new Graph();
    @FXML
    private Canvas canvas;
    private GraphicsContext graphicsContext;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setFill(Color.LIGHTGRAY);
        graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void canvasClick(MouseEvent mouseEvent) {
        GNode node = new GNode();
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillOval(mouseEvent.getX(), mouseEvent.getY(), 10, 10);
        graphicsContext.fillText(String.valueOf(node.getId()),mouseEvent.getX()+10, mouseEvent.getY()-5);

        graph.addNode(node);
        graph.printGraph();
    }


}
