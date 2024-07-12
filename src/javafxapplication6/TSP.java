/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication6; //FINAL ALGO PROJECT WITH GRAPGH

import java.util.Arrays;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.TRANSPARENT;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author pc
 */
public class TSP extends Application { //WITH GRAPH

    private static final int ROWS = 4; // Number of rows for the 2D array (aka cities)
    private static final int COLS = 4; // Number of columns for the 2D array (aka cities)
    String minpath1;
    String minpath2;
    String minpath3;

    //2d array shayla el matrix value
    int[][] array = new int[ROWS][COLS];

    String path; //store final min path cost

    int minDistance;

    @Override
    public void start(Stage primaryStage) {
        // Create a GridPane for the 2D array input
        GridPane gridPane = new GridPane();
        gridPane.setHgap(8); 
        gridPane.setVgap(8); 
        gridPane.setAlignment(Pos.CENTER); 

        // Create and add TextFields to the GridPane
        TextField[][] textFields = new TextField[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                textFields[i][j] = new TextField();
                textFields[i][j].setPrefWidth(50);
                gridPane.add(textFields[i][j], j, i);
            }
        }

        // Create a button to process the input and to show graph
        Button btn = new Button();
        btn.setText("Solve");
        Button Graph = new Button("Show Graph");

        // Create a TextArea to display the output
        TextArea outputArea = new TextArea();
        outputArea.setEditable(false); 
        outputArea.setPrefHeight(600);

//=======================================================================================================        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try {
                    //convert the text input from the user into integer values
                    for (int i = 0; i < ROWS; i++) {
                        for (int j = 0; j < COLS; j++) {
                            array[i][j] = Integer.parseInt(textFields[i][j].getText());
                        }
                    }
                    // via 0
                    int[] extractedArray = extractColumnValues(array);
                    // via 1
                    int[] secondStep = calculateSecondStep(array, extractedArray);

                    // via 2
                    int[] thirdStep = calculateThirdStep(array, secondStep);

                    // via 3
                    int[] lastStep = calculateLastStep(array, thirdStep);

                    // OUTPUT
                    StringBuilder output = new StringBuilder();
                    output.append("***TSP PROBLEM WITH COMPLEXITY O(2^N N^2) USING DYNAMIC PROGRAMMING***: \n");
                    output.append("\n");
                    output.append("*Cost Via 0 Node: \n");
                    output.append("C(2,1): ").append(extractedArray[0]).append("\n");
                    output.append("C(3,1): ").append(extractedArray[1]).append("\n");
                    output.append("C(4,1): ").append(extractedArray[2]).append("\n");
                    output.append("---------------------------------- \n");
                    output.append("*Cost Via 1 Node: \n");
                    output.append("C(2,{3}): ").append(secondStep[0]).append("\n");
                    output.append("C(2,{4}): ").append(secondStep[1]).append("\n");
                    output.append("C(3,{2}): ").append(secondStep[2]).append("\n");
                    output.append("C(3,{4}): ").append(secondStep[3]).append("\n");
                    output.append("C(4,{2}): ").append(secondStep[4]).append("\n");
                    output.append("C(4,{3}): ").append(secondStep[5]).append("\n");
                    output.append("---------------------------------- \n");
                    output.append("*Cost Via 2 Nodes:\n");
                    output.append("Min C(2,{3,4}): ").append(thirdStep[0]).append("\n");
                    output.append("Min C(3,{2,4}): ").append(thirdStep[1]).append("\n");
                    output.append("Min C(4,{3,2}): ").append(thirdStep[2]).append("\n");
                    output.append("---------------------------------- \n");
                    output.append("*Cost Via 3 Nodes:\n");
                    output.append("path 1: ").append(lastStep[0]).append("\n");
                    output.append("path 2: ").append(lastStep[1]).append("\n");
                    output.append("path 3: ").append(lastStep[2]).append("\n");
                    output.append("---------------------------------- \n");
                    output.append("# Minimum Cost = " + minDistance + "\n");
                    output.append("# Minimum Path: " + path);

                    // Set the output to the TextArea
                    outputArea.setText(output.toString());
                } catch (NumberFormatException e) {
                    outputArea.setText("Please enter valid integers.");
                }
            }
        });
//======================================================================================================= 
        Graph.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Stage graph = new Stage(); //new window for the graph only
                Pane pane = new Pane();

                try {
                    //convert the text input from the user into integer values
                    for (int i = 0; i < ROWS; i++) {
                        for (int j = 0; j < COLS; j++) {
                            array[i][j] = Integer.parseInt(textFields[i][j].getText());
                        }
                    }

                    //barg3 el path cost value mn el matrix ba3d ma et3mlha parse
                    String d12 = String.valueOf(array[0][1]);  // path cost from 1 to 2
                    String d21 = String.valueOf(array[1][0]);  // path cost from 2 to 1
                    String d13 = String.valueOf(array[0][2]);  // path cost from 1 to 3
                    String d31 = String.valueOf(array[2][0]);  // path cost from 3 to 1 
                    String d34 = String.valueOf(array[2][3]);  // path cost from 3 to 4
                    String d43 = String.valueOf(array[3][2]);  // path cost from 4 to 3
                    String d24 = String.valueOf(array[1][3]);  // path cost from 2 to 4
                    String d42 = String.valueOf(array[3][1]);  // path cost from 4 to 2
                    String d14 = String.valueOf(array[0][3]);  // path cost from 1 to 4
                    String d41 = String.valueOf(array[3][0]);  // path cost from 4 to 1
                    String d32 = String.valueOf(array[2][1]);  // path cost from 3 to 2
                    String d23 = String.valueOf(array[1][2]);  // path cost from 2 to 3
                    //b3ml circles for each city node
                    Circle circle1 = new Circle(400, 200, 20); //x,y,radius   1st
                    Circle circle2 = new Circle(600, 200, 20); //x,y,radius   2nd
                    Circle circle3 = new Circle(400, 400, 20); //x,y,radius   3rd
                    Circle circle4 = new Circle(600, 400, 20); //x,y,radius   4th
                    //inner circle color
                    circle1.setFill(TRANSPARENT);
                    circle2.setFill(TRANSPARENT);
                    circle3.setFill(TRANSPARENT);
                    circle4.setFill(TRANSPARENT);
                    //outline color
                    circle1.setStroke(BLACK);
                    circle2.setStroke(BLACK);
                    circle3.setStroke(BLACK);
                    circle4.setStroke(BLACK);
                    //add circles to the screen pane
                    pane.getChildren().addAll(circle1, circle2, circle3, circle4);

                    //Line(x start,y start,x end,y end)  startpoint(x,y) endpoint(x,y)
                    Line line1 = new Line(circle1.getCenterX() + 20, circle1.getCenterY() - 10, circle2.getCenterX() - 25, circle2.getCenterY() - 10); //d12
                    Line line2 = new Line(circle2.getCenterX() - 20, circle2.getCenterY() + 10, circle1.getCenterX() + 25, circle1.getCenterY() + 10); //d21
                    Line line3 = new Line(circle1.getCenterX() - 10, circle1.getCenterY() + 25, circle3.getCenterX() - 10, circle3.getCenterY() - 25); //d13
                    Line line4 = new Line(circle3.getCenterX() + 10, circle3.getCenterY() - 25, circle1.getCenterX() + 10, circle1.getCenterY() + 25); //d31
                    Line line5 = new Line(circle3.getCenterX() + 20, circle3.getCenterY() - 10, circle4.getCenterX() - 25, circle4.getCenterY() - 10); //d34
                    Line line6 = new Line(circle4.getCenterX() - 25, circle4.getCenterY() + 10, circle3.getCenterX() + 25, circle3.getCenterY() + 10); //d43
                    Line line7 = new Line(circle2.getCenterX() + 10, circle2.getCenterY() + 25, circle4.getCenterX() + 10, circle4.getCenterY() - 25); //d24
                    Line line8 = new Line(circle4.getCenterX() - 10, circle4.getCenterY() - 25, circle2.getCenterX() - 10, circle2.getCenterY() + 25); //d42
                    Line line9 = new Line(line2.getEndX(), line2.getEndY(), line8.getStartX(), line8.getStartY()); //d14
                    Line line10 = new Line(line5.getEndX(), line5.getEndY(), line4.getEndX(), line4.getEndY()); //d41
                    Line line11 = new Line(line4.getStartX(), line4.getStartY(), line2.getStartX(), line2.getStartY()); //d32
                    Line line12 = new Line(line8.getEndX(), line8.getEndY(), line5.getStartX(), line5.getStartY()); //d23
                    //Text(start,end,content)
                    Text arrow1 = new Text(circle2.getCenterX() - 28, circle1.getCenterY() - 5, ">");//d12
                    Text arrow2 = new Text(circle1.getCenterX() + 20, circle2.getCenterY() + 14, "<");//d21
                    Text arrow3 = new Text(circle3.getCenterX() - 14, circle3.getCenterY() - 20, "v");//d13
                    Text arrow4 = new Text(circle1.getCenterX() + 5, circle1.getCenterY() + 30, "^");//d31
                    Text arrow5 = new Text(circle4.getCenterX() - 30, circle4.getCenterY() - 5, ">");//d34
                    Text arrow6 = new Text(circle3.getCenterX() + 20, circle3.getCenterY() + 15, "<");//d43
                    Text arrow7 = new Text(circle4.getCenterX() + 6, circle4.getCenterY() - 20, "v");//d24
                    Text arrow8 = new Text(circle2.getCenterX() - 15, circle2.getCenterY() + 30, "^");//d42
                    Text arrow9 = new Text(line9.getEndX() - 8, line9.getEndY(), ">");//d14
                    Text arrow10 = new Text(line10.getEndX() + 3, line10.getEndY() + 10, "<");//d41
                    arrow9.setRotate(45);
                    arrow10.setRotate(45);
                    Text arrow11 = new Text(line11.getEndX(), line11.getEndY() + 5, "<");//d32
                    Text arrow12 = new Text(line12.getEndX() + 5, line12.getEndY(), ">");//d23
                    arrow11.setRotate(135);
                    arrow12.setRotate(135);

                    pane.getChildren().addAll(line1, line2, line3, line4, line5, line6, line7, line8, line9, line10, line11, line12, arrow1, arrow2, arrow3, arrow4, arrow5, arrow6, arrow7, arrow8, arrow9, arrow10, arrow11, arrow12);
                    //number each circle node
                    Text c1 = new Text(circle1.getCenterX(), circle1.getCenterY(), "1");
                    Text c2 = new Text(circle2.getCenterX(), circle2.getCenterY(), "2");
                    Text c3 = new Text(circle3.getCenterX(), circle3.getCenterY(), "3");
                    Text c4 = new Text(circle4.getCenterX(), circle4.getCenterY(), "4");

                    pane.getChildren().addAll(c1, c2, c3, c4);

                    //a7ot label fo2 kol path be cost value (c1+c2/2 = mid)(-/+ for extra positioning)
                    Text D12 = new Text((circle1.getCenterX() + circle2.getCenterX()) / 2,
                            (circle1.getCenterY() + circle2.getCenterY()) / 2 - 10,
                            d12);

                    Text D21 = new Text((circle2.getCenterX() + circle1.getCenterX()) / 2,
                            (circle2.getCenterY() + circle1.getCenterY()) / 2 + 10,
                            d21);

                    Text D13 = new Text((circle1.getCenterX() + circle3.getCenterX()) / 2 - 35,
                            (circle1.getCenterY() + circle3.getCenterY()) / 2,
                            d13);

                    Text D31 = new Text((circle3.getCenterX() + circle1.getCenterX()) / 2 + 10,
                            (circle3.getCenterY() + circle1.getCenterY()) / 2,
                            d31);

                    Text D34 = new Text((circle3.getCenterX() + circle4.getCenterX()) / 2,
                            (circle3.getCenterY() + circle4.getCenterY()) / 2 - 10,
                            d34);

                    Text D43 = new Text((circle4.getCenterX() + circle3.getCenterX()) / 2,
                            (circle4.getCenterY() + circle3.getCenterY()) / 2 + 10,
                            d43);

                    Text D24 = new Text((circle2.getCenterX() + circle4.getCenterX()) / 2 + 20,
                            (circle2.getCenterY() + circle4.getCenterY()) / 2,
                            d24);

                    Text D42 = new Text((circle4.getCenterX() + circle2.getCenterX()) / 2 - 30,
                            (circle4.getCenterY() + circle2.getCenterY()) / 2,
                            d42);

                    Text D14 = new Text((circle1.getCenterX() + circle4.getCenterX()) / 2 - 30,
                            (circle4.getCenterY() + circle2.getCenterY()) / 2 - 40,
                            d14);

                    D14.setRotate(45); //3shan mayla fe el nos

                    Text D41 = new Text((circle1.getCenterX() + circle4.getCenterX()) / 2 - 15,
                            (circle4.getCenterY() + circle2.getCenterY()) / 2 + 40,
                            d41);

                    D41.setRotate(45); //3shan mayla fe el nos

                    Text D32 = new Text((circle3.getCenterX() + circle2.getCenterX()) / 2 + 15,
                            (circle3.getCenterY() + circle2.getCenterY()) / 2 - 50,
                            d32);

                    D32.setRotate(-45); //3shan mayla fe el nos

                    Text D23 = new Text((circle2.getCenterX() + circle3.getCenterX()) / 2 + 25,
                            (circle2.getCenterY() + circle3.getCenterY()) / 2 + 10,
                            d23);

                    D23.setRotate(-45); //3shan mayla fe el nos

                    pane.getChildren().addAll(D12, D21, D13, D31, D34, D43, D24, D42, D14, D41, D32, D23);

                    Scene scene2 = new Scene(pane, 1000, 550);

                    graph.setScene(scene2);
                    graph.setTitle("Graph");
                    graph.show();

                } catch (NumberFormatException e) {
                    outputArea.setText("Please enter valid integers.");
                }

            }
        });

//=======================================================================================================
        // Create a VBox to hold the components
        VBox root = new VBox(20); // 20 is the spacing between elements
        root.setAlignment(Pos.CENTER);
       root.getChildren().addAll(gridPane, btn, Graph, outputArea);

        Scene scene = new Scene(root, 400, 600);
        primaryStage.setTitle("TSP");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    //Method to calculate via 0 node: (awel col without 1->1)
    private int[] extractColumnValues(int[][] array) {
        //int[] extractedArray; //1d array shayla awel col 
        return new int[]{array[1][0], array[2][0], array[3][0]};
    }

    // Method to calculate via one node:
    private int[] calculateSecondStep(int[][] array, int[] extractedArray) {
        int[] secondStep = new int[6];
        secondStep[0] = array[1][2] + extractedArray[1]; // C(2,{3})    
        secondStep[1] = array[1][3] + extractedArray[2]; // C(2,{4})    
        secondStep[2] = array[2][1] + extractedArray[0]; // C(3,{2})   
        secondStep[3] = array[2][3] + extractedArray[2]; // C(3,{4})
        secondStep[4] = array[3][1] + extractedArray[0]; // C(4,{2})
        secondStep[5] = array[3][2] + extractedArray[1]; // C(4,{3})
        return secondStep;
    }

    // Method to calculate via 2 nodes:
    private int[] calculateThirdStep(int[][] array, int[] secondStep) {
        int[] thirdStep = new int[3];
        // Minimum of (arr[1][2] + C(3,{4}) and arr[1][3] + C(4,{3}))
        thirdStep[0] = Math.min(array[1][2] + secondStep[3], array[1][3] + secondStep[5]);
        if (array[1][2] + secondStep[3] <= array[1][3] + secondStep[5]) { //low  D23 +C(3,{4}) as8r
            minpath1 = "2 -> 3 -> 4-> 1";
        } else { //low  D24 +C(4,{3}) as8r
            minpath1 = "2 -> 4 -> 3 -> 1";
        }

        thirdStep[1] = Math.min(array[2][1] + secondStep[2], array[2][3] + secondStep[4]); // Minimum of (arr[2][1] + C(2,{3}) and arr[2][3] + C(4,{2}))
        if (array[2][1] + secondStep[2] <= array[2][3] + secondStep[4]) { //low  D32 +C(2,{4}) as8r
            minpath2 = "3 -> 2 -> 4 -> 1";
        } else {//low  D34 +C(4,{2}) as8r
            minpath2 = "3 -> 4 -> 2 -> 1";
        }

        thirdStep[2] = Math.min(array[3][2] + secondStep[2], array[3][1] + secondStep[0]); // Minimum of (arr[3][2] + C(3,{2}) and arr[3][1] + C(2,{3}))
        if (array[3][2] + secondStep[2] <= array[3][1] + secondStep[0]) { //low  D43 +C(3,{2}) as8r
            minpath3 = "4 -> 3 -> 2 -> 1";
        } else {//low  D42 +C(2,3}) as8r
            minpath3 = "4 -> 2 -> 3 -> 1";
        }

        return thirdStep;
    }

    // Method to calculate via 3 nodes:
    private int[] calculateLastStep(int[][] array, int[] thirdStep) {
        int[] lastStep = new int[3];
        lastStep[0] = array[0][1] + thirdStep[0]; // arr[0][1] + C(2,{3,4})
        lastStep[1] = array[0][2] + thirdStep[1]; // arr[0][2] + C(3,{4,2})
        lastStep[2] = array[0][3] + thirdStep[2]; // arr[0][3] + C(4,{2,3})

        if (lastStep[0] <= lastStep[1] && lastStep[0] <= lastStep[2]) { //low D12 + C(2,{3,4}) as8r
            path = "1 -> " + minpath1;
        } else if (lastStep[1] <= lastStep[0] && lastStep[1] <= lastStep[2]) { //low D13+ C(3,{4,2})as8r
            path = "1 -> " + minpath2;
        } else { //low D14+ C(4,{2,3})as8r
            path = "1 -> " + minpath3;
        }

        Arrays.sort(lastStep);
//return el min b3d ma et3mlha sort:
        minDistance = lastStep[0];

        return lastStep;
    }

}
