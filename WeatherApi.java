package com.project.weatherApi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import java.lang.String;

public class WeatherApi {
    static private String apiKey = "7848d460f5be45a188044054250306";
    static private String location = "London";

    public static void main(String[] args) {
        try {
            String apiUrl = "http://api.weatherapi.com/v1/current.json?key=" + apiKey + "&q=" + location;

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject jsonResponse = new JSONObject(response.toString());

                // Extract location info
                JSONObject locationObj = jsonResponse.getJSONObject("location");
                String city = locationObj.getString("name");
                String country = locationObj.getString("country");
                String localTime = locationObj.getString("localtime");

                // Extract weather info
                JSONObject current = jsonResponse.getJSONObject("current");
                double temperature = current.getDouble("temp_c");
                double feelsLike = current.getDouble("feelslike_c");
                double windSpeed = current.getDouble("wind_kph");
                int humidity = current.getInt("humidity");
                String condition = current.getJSONObject("condition").getString("text");

                // Display
                System.out.println("Weather Report");
                System.out.println("...............");
                System.out.println("Location: " + city + ", " + country);
                System.out.println("Local Time: " + localTime);
                System.out.println("Temperature: " + temperature + " °C");
                System.out.println("Feels Like: " + feelsLike + " °C");
                System.out.println("Condition: " + condition);
                System.out.println("Wind Speed: " + windSpeed + " kph");
                System.out.println("Humidity: " + humidity + "%");
            } else {
                System.out.println("GET request failed. Response code: " + responseCode);

                // Only try to read the error stream if available
                if (connection.getErrorStream() != null) {
                    BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    String line;
                    while ((line = errorReader.readLine()) != null) {
                        System.out.println(line);
                    }
                    errorReader.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

