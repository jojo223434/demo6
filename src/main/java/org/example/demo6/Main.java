package org.example.demo6;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main extends Application {

    private Gauge windSpeedGauge;
    private Gauge windEffectGauge;
    private Gauge[] turbineGauges = new Gauge[6];

    @Override
    public void start(Stage primaryStage) {
        DatabaseManager.initDatabase();

        Text title = new Text("Vindtved Møllerne LIVE");

        windSpeedGauge = GaugeBuilder.create()
                .title("Vindhastighed")
                .unit("m/s")
                .minValue(0)
                .maxValue(25)
                .animated(true)
                .build();

        windEffectGauge = GaugeBuilder.create()
                .title("Effekt")
                .unit("kW")
                .minValue(-500)
                .maxValue(10000)
                .animated(true)
                .build();

        VBox turbineBox = new VBox(10);
        turbineBox.setAlignment(Pos.CENTER);

        for (int i = 0; i < 6; i++) {
            turbineGauges[i] = GaugeBuilder.create()
                    .title("WTG0" + (i + 1))
                    .unit("kW")
                    .minValue(-50)
                    .maxValue(2000)
                    .animated(true)
                    .build();
            turbineBox.getChildren().add(turbineGauges[i]);
        }

        Button chartBtn = new Button("Se diagram");
        chartBtn.setOnAction(e -> ChartView.showChart());

        Button tableBtn = new Button("Se regneark");
        tableBtn.setOnAction(e -> TableViewWindow.showTable());

        VBox root = new VBox(20, title, windSpeedGauge, windEffectGauge, turbineBox, chartBtn, tableBtn);
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root, 500, 600);

        primaryStage.setTitle("Vindtved Vindmøller App");
        primaryStage.setScene(scene);
        primaryStage.show();

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            DataFetcher.fetchData(windSpeedGauge, windEffectGauge, turbineGauges);
        }, 0, 10, TimeUnit.MINUTES);

        primaryStage.setOnCloseRequest(event -> scheduler.shutdown());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
