package util;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.hc.core5.http.ContentType;

import java.io.*;
import java.util.Properties;

public class GroqService {

    private static final String API_KEY;

    static {
        Properties props = new Properties();
        try (InputStream fis = GroqService.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (fis == null) {
                throw new RuntimeException("File config.properties tidak ditemukan di classpath!");
            }
            props.load(fis);
            API_KEY = props.getProperty("GROQ_API_KEY");
            if (API_KEY == null || API_KEY.trim().isEmpty()) {
                throw new RuntimeException("API key kosong atau tidak ditemukan di config.properties");
            }
        } catch (IOException e) {
            throw new RuntimeException("Gagal membaca API key dari config.properties", e);
        }
    }

    public static String askGroq(String userInput) {
        try {
            String url = "https://api.groq.com/openai/v1/chat/completions";

            // Format permintaan OpenAI-style
            String requestBody = "{\n" +
                    "  \"model\": \"llama3-70b-8192\",\n" +
                    "  \"messages\": [\n" +
                    "    {\"role\": \"user\", \"content\": \"" + escapeJson(userInput) + "\"}\n" +
                    "  ]\n" +
                    "}";

            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            post.setHeader("Content-Type", "application/json");
            post.setHeader("Authorization", "Bearer " + API_KEY);
            post.setEntity(new StringEntity(requestBody, ContentType.APPLICATION_JSON));

            ClassicHttpResponse response = client.execute(post);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuilder responseText = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseText.append(line);
            }

            reader.close();
            response.close();
            client.close();

            // Debug
            System.out.println("DEBUG: JSON response: " + responseText);

            return extractReplyFromJson(responseText.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return "[Gagal menghubungi Groq: " + e.getMessage() + "]";
        }
    }

    private static String escapeJson(String text) {
        return text.replace("\"", "\\\"").replace("\n", "\\n");
    }

    private static String extractReplyFromJson(String json) {
        try {
            JSONObject root = new JSONObject(json);
            JSONArray choices = root.getJSONArray("choices");
            if (choices.length() > 0) {
                JSONObject message = choices.getJSONObject(0).getJSONObject("message");
                return message.getString("content");
            } else {
                return "[Respons kosong dari Groq]";
            }
        } catch (Exception e) {
            return "[Gagal membaca jawaban Groq: " + e.getMessage() + "]";
        }
    }

    public static String cleanMarkdown(String text) {
        // Hilangkan bold **teks** atau __teks__
        return text.replaceAll("\\*\\*(.*?)\\*\\*", "$1")
                .replaceAll("__(.*?)__", "$1");
    }

}
