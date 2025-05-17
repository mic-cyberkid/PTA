import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;

import org.json.JSONObject;

public class OllamaStreamHandler {

    public static void streamResponseFromOllama(String baseUrl, String model, String prompt) {
        try {
            String json = String.format("{\"model\": \"%s\", \"prompt\": \"%s\", \"stream\": true}", model, prompt);

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(baseUrl + "/api/generate"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream())
                    .thenAccept(response -> {
                        try (InputStream inputStream = response.body();
                             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                if (!line.trim().isEmpty()) {
                                    try {
                                        JSONObject obj = new JSONObject(line);
                                        String content = obj.optString("response", "");
                                        System.out.print(content); // Append partial response
                                    } catch (Exception e) {
                                        System.err.println("Invalid JSON chunk: " + line);
                                    }
                                }
                            }
                            System.out.println(); // Final newline after streaming completes
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).join();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        streamResponseFromOllama("http://localhost:11434", "qwen1.5_q8", "Explain quantum computing.");
    }
}
