package util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class GeminiService {

    private static final String GEMINI_API_KEY = "AIzaSyB0zdmxHxrPn75LBQVRkyjTKp78OUz4L8M";

    private static final String ENDPOINT = "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-pro:generateContent";

    public static String generateContent(String prompt) {
        try {
            String endpointWithKey = ENDPOINT + "?key=" + GEMINI_API_KEY;
            String payload = "{ \"contents\": [{ \"parts\": [{ \"text\": \"" + escapeJson(prompt) + "\" }] }] }";

            URL url = new URL(endpointWithKey);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes("UTF-8"));
            }

            int responseCode = conn.getResponseCode();
            InputStream is = (responseCode == 200) ? conn.getInputStream() : conn.getErrorStream();

            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
            return response.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return "{\"error\":\"" + e.getMessage() + "\"}";
        }
    }

    // Escape karakter untuk JSON string
    private static String escapeJson(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }
}
