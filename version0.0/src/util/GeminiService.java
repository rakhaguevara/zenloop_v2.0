package util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class GeminiService {

    private static String GEMINI_API_KEY;

    // ✅ Letakkan endpoint di luar method
    private static final String ENDPOINT = "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-pro:generateContent";

    static {
        loadApiKeyFromEnv();
    }

    private static void loadApiKeyFromEnv() {
        File envFile = new File(".env");
        if (!envFile.exists()) {
            throw new RuntimeException("❌ Gagal membaca file .env: file tidak ditemukan");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(envFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("GEMINI_API_KEY=")) {
                    GEMINI_API_KEY = line.substring("GEMINI_API_KEY=".length()).trim();
                    return;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("❌ Gagal membaca file .env: " + e.getMessage());
        }

        if (GEMINI_API_KEY == null || GEMINI_API_KEY.isEmpty()) {
            throw new RuntimeException("❌ GEMINI_API_KEY tidak ditemukan dalam file .env");
        }
    }

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

    // Escape karakter yang bermasalah di JSON seperti tanda kutip
    private static String escapeJson(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }
}
