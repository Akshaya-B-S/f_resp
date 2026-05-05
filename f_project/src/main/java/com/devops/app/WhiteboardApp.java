package com.devops.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.sql.*;

public class WhiteboardApp extends Application {
    private double lineWidth = 2.0;
    private Color strokeColor = Color.BLACK;

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(900, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        initDatabase();

        // --- Toolbar ---
        ColorPicker cp = new ColorPicker(Color.BLACK);
        cp.setOnAction(e -> strokeColor = cp.getValue());

        Button pencil = new Button("Pencil");
        pencil.setOnAction(e -> lineWidth = 1.0);

        Button marker = new Button("Marker");
        marker.setOnAction(e -> lineWidth = 6.0);

        Button brush = new Button("Brush");
        brush.setOnAction(e -> lineWidth = 15.0);

        Button saveBtn = new Button("💾 Save to DB");
        saveBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
        saveBtn.setOnAction(e -> saveToDB("UserDrawing", "CanvasDataStub"));

        HBox toolbar = new HBox(10, cp, pencil, marker, brush, saveBtn);
        toolbar.setStyle("-fx-padding: 10; -fx-background-color: #333;");

        // --- Drawing Events ---
        canvas.setOnMousePressed(e -> {
            gc.setStroke(strokeColor);
            gc.setLineWidth(lineWidth);
            gc.beginPath();
            gc.lineTo(e.getX(), e.getY());
            gc.stroke();
        });

        canvas.setOnMouseDragged(e -> {
            gc.lineTo(e.getX(), e.getY());
            gc.stroke();
        });

        BorderPane root = new BorderPane(canvas);
        root.setTop(toolbar);

        stage.setScene(new Scene(root));
        stage.setTitle("Whiteboard Pro - JavaFX Desktop");
        stage.show();
    }

    private void initDatabase() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:whiteboard.db")) {
            conn.createStatement().execute("CREATE TABLE IF NOT EXISTS drawings (id INTEGER PRIMARY KEY, name TEXT, data TEXT)");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void saveToDB(String name, String data) {
        String sql = "INSERT INTO drawings(name, data) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:whiteboard.db");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, data);
            pstmt.executeUpdate();
            System.out.println("Saved to local database!");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void main(String[] args) { launch(args); }
}
