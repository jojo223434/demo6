package org.example.demo6;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.*;

public class TableViewWindow {

    public static void showTable() {
        Stage stage = new Stage();
        stage.setTitle("Datatabel - Turbineaflæsninger");

        TableView<Reading> table = new TableView<>();
        ObservableList<Reading> data = FXCollections.observableArrayList();

        // أعمدة
        TableColumn<Reading, String> timestampCol = new TableColumn<>("tiden");
        timestampCol.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        TableColumn<Reading, Double> windSpeedCol = new TableColumn<>("vindhastighed");
        windSpeedCol.setCellValueFactory(new PropertyValueFactory<>("windSpeed"));

        TableColumn<Reading, Double> windEffectCol = new TableColumn<>("Vindeffekt");
        windEffectCol.setCellValueFactory(new PropertyValueFactory<>("windEffect"));

        TableColumn<Reading, Double> wtg01Col = new TableColumn<>("WTG01");
        wtg01Col.setCellValueFactory(new PropertyValueFactory<>("wtg01"));

        TableColumn<Reading, Double> wtg02Col = new TableColumn<>("WTG02");
        wtg02Col.setCellValueFactory(new PropertyValueFactory<>("wtg02"));

        TableColumn<Reading, Double> wtg03Col = new TableColumn<>("WTG03");
        wtg03Col.setCellValueFactory(new PropertyValueFactory<>("wtg03"));

        TableColumn<Reading, Double> wtg04Col = new TableColumn<>("WTG04");
        wtg04Col.setCellValueFactory(new PropertyValueFactory<>("wtg04"));

        TableColumn<Reading, Double> wtg05Col = new TableColumn<>("WTG05");
        wtg05Col.setCellValueFactory(new PropertyValueFactory<>("wtg05"));

        TableColumn<Reading, Double> wtg06Col = new TableColumn<>("WTG06");
        wtg06Col.setCellValueFactory(new PropertyValueFactory<>("wtg06"));

        table.getColumns().addAll(timestampCol, windSpeedCol, windEffectCol,
                wtg01Col, wtg02Col, wtg03Col, wtg04Col, wtg05Col, wtg06Col);

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:vindmoller.db");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM readings ORDER BY timestamp DESC LIMIT 100")) {

            while (rs.next()) {
                String ts = rs.getString("timestamp");
                double ws = rs.getDouble("wind_speed");
                double we = rs.getDouble("wind_effect");
                double[] turbines = {
                        rs.getDouble("wtg01"),
                        rs.getDouble("wtg02"),
                        rs.getDouble("wtg03"),
                        rs.getDouble("wtg04"),
                        rs.getDouble("wtg05"),
                        rs.getDouble("wtg06")
                };
                data.add(new Reading(ts, ws, we, turbines));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        table.setItems(data);

        Scene scene = new Scene(table, 1000, 600);
        stage.setScene(scene);
        stage.show();
    }
}
