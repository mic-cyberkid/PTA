import javafx.concurrent.Task;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class OllamaStreamer {

    public void streamOllamaOutput(String prompt, java.util.function.Consumer<String> onUpdate) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    
                    URL url = new URL("http://localhost:8000/llm/chat");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Authorization", "Bearer " + Session.getAuthToken());
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true);

                    JSONObject data = new JSONObject();
                    data.put("message", prompt);
                    data.put("new_chat",Session.isNewChat());
                    data.put("conversation_id", Session.getConversation_id());

                    try (OutputStream os = conn.getOutputStream()) {
                        os.write(data.toString().getBytes());
                        os.flush();
                    }

                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (!line.trim().isEmpty()) {
                                // Each line is a JSON object, parse and extract the `response`
                                String responseToken = extractResponse(line);
                                if (responseToken != null) {
                                    
                                    Platform.runLater(() -> onUpdate.accept(responseToken));
                                }
                            }
                        }
                    }

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
                
            }
        };

        new Thread(task).start();
    }

    // Extracts the 'response' field from Ollama's JSON line
    private String extractResponse(String jsonLine) {
        try {
            JSONObject raw = new JSONObject("{"+jsonLine+"}");
            JSONObject data = raw.getJSONObject("data");
            return data.getString("response");
        } catch (Exception e) {
            return null;
        }
    }
}

