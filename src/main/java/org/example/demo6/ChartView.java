package org.example.demo6;

import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.sql.*;

public class ChartView {

    public static void showChart() {
        Stage stage = new Stage();
        stage.setTitle("Produktivitet af alle møller i løbet af dagen");

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Tid (seneste = 0)");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("kW");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("WTG01 til WTG06");

        XYChart.Series<Number, Number>[] seriesArray = new XYChart.Series[6];
        for (int i = 0; i < 6; i++) {
            seriesArray[i] = new XYChart.Series<>();
            seriesArray[i].setName("WTG0" + (i + 1));
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:vindmoller.db");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM readings ORDER BY timestamp DESC LIMIT 144")) {

            int counter = 0;
            while (rs.next()) {
                for (int i = 0; i < 6; i++) {
                    double value = rs.getDouble("wtg0" + (i + 1));
                    seriesArray[i].getData().add(new XYChart.Data<>(counter, value));
                }
                counter++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (XYChart.Series<Number, Number> series : seriesArray) {
            lineChart.getData().add(series);
        }

        Scene scene = new Scene(lineChart, 700, 400);
        stage.setScene(scene);
        stage.show();
    }
}
