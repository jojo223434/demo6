package org.example.demo6;

import java.net.http.*;
import java.net.URI;
import com.fasterxml.jackson.databind.*;
import javafx.application.Platform;
import eu.hansolo.medusa.Gauge;

public class DataFetcher {

    private static final String API_URL = "https://vind-og-klima-app.videnomvind.dk/api/stats?location=vindtved";

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

                        if (latest.isMissingNode()) {
                            System.out.println("No latest reading found in API response.");
                            return;
                        }

                        double windSpeed = latest.path("wind_speed").asDouble();
                        double windEffect = latest.path("wind_effect").asDouble();
                        JsonNode turbines = latest.path("data").path("turbines");

                        double[] turbineValues = new double[6];
                        for (int i = 0; i < 6; i++) {
                            String key = "wtg0" + (i + 1);
                            turbineValues[i] = turbines.path(key).isMissingNode() ? 0.0 : turbines.path(key).asDouble();
                        }

                        String timestamp = latest.path("logged_at").asText();

                        Platform.runLater(() -> {
                            windSpeedGauge.setValue(windSpeed);
                            windEffectGauge.setValue(windEffect);
                            for (int i = 0; i < 6; i++) {
                                turbineGauges[i].setValue(turbineValues[i]);
                            }
                        });

                        DatabaseManager.insertReading(timestamp, windSpeed, windEffect, turbineValues);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .exceptionally(e -> {
                    System.out.println("Failed to fetch data from API.");
                    e.printStackTrace();
                    return null;
                });
    }
}
