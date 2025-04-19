package org.example.demo6;

import java.net.http.*;
import java.net.URI;
import com.fasterxml.jackson.databind.*;
import javafx.application.Platform;
import eu.hansolo.medusa.Gauge;

public class DataFetcher {

    private static final String API_URL = "https://vind-og-klima-app.videnomvind.dk/api/v1/live/location/vindtved";

    public static void fetchData(Gauge windSpeedGauge, Gauge windEffectGauge, Gauge[] turbineGauges) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode root = mapper.readTree(response);
                        JsonNode latest = root.path("latest_reading");

                        double windSpeed = latest.path("wind_speed").asDouble();
                        double windEffect = latest.path("wind_effect").asDouble();
                        JsonNode turbines = latest.path("data").path("turbines");

                        Platform.runLater(() -> {
                            windSpeedGauge.setValue(windSpeed);
                            windEffectGauge.setValue(windEffect);
                            for (int i = 0; i < 6; i++) {
                                String key = "wtg0" + (i + 1);
                                double value = turbines.path(key).asDouble();
                                turbineGauges[i].setValue(value);
                            }
                        });

                        String timestamp = latest.path("logged_at").asText();
                        double[] turbineValues = new double[6];
                        for (int i = 0; i < 6; i++) {
                            String key = "wtg0" + (i + 1);
                            turbineValues[i] = turbines.path(key).asDouble();
                        }
                        DatabaseManager.insertReading(timestamp, windSpeed, windEffect, turbineValues);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });
    }
}
