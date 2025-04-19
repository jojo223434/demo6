package org.example.demo6;

public class Reading {
    private String timestamp;
    private double windSpeed;
    private double windEffect;
    private double[] turbines;

    public Reading(String timestamp, double windSpeed, double windEffect, double[] turbines) {
        this.timestamp = timestamp;
        this.windSpeed = windSpeed;
        this.windEffect = windEffect;
        this.turbines = turbines;
    }

    public String getTimestamp() { return timestamp; }
    public double getWindSpeed() { return windSpeed; }
    public double getWindEffect() { return windEffect; }
    public double getWtg01() { return turbines[0]; }
    public double getWtg02() { return turbines[1]; }
    public double getWtg03() { return turbines[2]; }
    public double getWtg04() { return turbines[3]; }
    public double getWtg05() { return turbines[4]; }
    public double getWtg06() { return turbines[5]; }
}
