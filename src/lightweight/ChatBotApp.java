import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;

public class ChatBotApp extends Application {

    private WebView webView;
    private WebEngine webEngine;

    @Override
    public void start(Stage primaryStage) {
        webView = new WebView();
        webEngine = webView.getEngine();

        // Load basic HTML layout
        webEngine.loadContent("""
            <html><body>
            <div id='chatbox'>
                <div class='user'>User: Hello</div>
                <div class='bot' id='latest-bot-msg'>Bot: </div>
            </div>
            </body></html>
        """);

        primaryStage.setScene(new Scene(webView, 600, 400));
        primaryStage.setTitle("ChatBot Streaming");
        primaryStage.show();

        // Start the streaming response
        streamResponseFromOllama("http://localhost:11434", "llama3.2", "Explain quantum computing.");
    }

    private void streamResponseFromOllama(String baseUrl, String model, String prompt) {
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
                                    JSONObject obj = new JSONObject(line);
                                    String content = obj.optString("response", "");
                                    if (!content.isEmpty()) {
                                        // JavaFX DOM updates must be on the JavaFX Application Thread
                                        Platform.runLater(() -> appendToChat(content));
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Injects content into the "latest-bot-msg" div
    private void appendToChat(String content) {
        String escaped = content.replace("'", "\\'").replace("\n", "<br>");
        webEngine.executeScript(
            "document.getElementById('latest-bot-msg').innerHTML += '" + escaped + "';"
        );
    }

    public static void main(String[] args) {
        launch(args);
    }
}
