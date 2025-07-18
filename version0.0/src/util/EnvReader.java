package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class EnvReader {
    private static final HashMap<String, String> envMap = new HashMap<>();

    static {
        try (BufferedReader reader = new BufferedReader(new FileReader(".env"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Abaikan baris kosong atau komentar
                if (line.trim().isEmpty() || line.startsWith("#"))
                    continue;

                // Split berdasarkan '='
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    envMap.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            System.err.println("❌ Gagal membaca file .env: " + e.getMessage());
        }
    }

    public static String get(String key) {
        return envMap.get(key);
    }
}
