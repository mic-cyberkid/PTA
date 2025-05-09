import org.json.JSONObject;
import org.json.JSONArray;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javafx.application.Platform;
import javafx.scene.control.Alert;

public class ApiFunctions {

    private static final String BASE_URL = "http://127.0.0.1:8000/llm"; // Set your base URL
    private static String authToken = ""; // Store the authentication token
    private static boolean newChat = true;
    private static String conversation_id = "";

    // Function to register user
    public static JSONObject registerUser(String username, String password) throws IOException {
        String url = BASE_URL + "/register";
        String params = "username=" + username + "&password=" + password;
        
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setDoOutput(true);
        
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = params.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        // Handle variious cases (User already exist, Standard error )
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject responseJson = new JSONObject(response.toString());
            System.out.println("User registered successfully");
            return responseJson;
        } else {
            System.out.println("Failed to register user");
            return null;
            
        }
    }

    // Function to login user
    public static JSONObject loginUser(String username, String password) throws IOException {
        String url = BASE_URL + "/token";
        String params = "username=" + username + "&password=" + password;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = params.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject responseJson = new JSONObject(response.toString());
            System.out.println("Response:"+responseJson);
            authToken = responseJson.getString("access_token"); // Store the auth token
            String usernameResponse = responseJson.getString("username");
            System.out.println("Logged in as: " + usernameResponse);

            // Redirect to the ChatPane (implement as needed)
            System.out.println("Login successful");
            return responseJson;
            
        } else {
                System.out.println("Error logging in.");
                return null;
                //showAlert(Alert.AlertType.ERROR, "Chat History", "Error Logging In.");
             
        }
        
    }

    // Function to send message
    public static JSONObject sendMessage(String message, boolean newChat, String conversation_id) throws IOException {
        String url = BASE_URL + "/chat";
        JSONObject body = new JSONObject();
        body.put("message", message);

        if (newChat) {
            body.put("new_chat", true);
        } else {
            body.put("conversation_id", conversation_id); // Replace with actual conversation ID
        }

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + authToken);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = body.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject responseJson = new JSONObject(response.toString());
            System.out.println("Response: "+responseJson);
            String assistantResponse = responseJson.getString("response");
            System.out.println("Assistant: " + assistantResponse);
            return responseJson;
        } else {
            System.out.println("Error sending message.");
            return null;
            //showAlert(Alert.AlertType.ERROR, "Chat", "Error sending message.");
    
        }
    }

    // Load conversations
    public static JSONObject loadConversations() throws IOException {
        String url = BASE_URL + "/conversations";
        
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + authToken);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject responseJson = new JSONObject(response.toString());
            JSONArray messages = responseJson.getJSONArray("messages");
            System.out.println("Convos:"+responseJson);
            for (int i = 0; i < messages.length(); i++) {
                JSONObject conversation = messages.getJSONObject(i);
                System.out.println("Conversation ID: " + conversation.getString("conversation_id"));
            }
            return responseJson;
        } else {
            System.out.println("Error getting converstions");
            return null;
        }
    }

    // Load chat history by ID
    public static JSONObject loadChatHistory(String conversationId) throws IOException {
        String url = BASE_URL + "/conversations/" + conversationId;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + authToken);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject responseJson = new JSONObject(response.toString());
            JSONObject conversations = responseJson.getJSONObject("conversations");

            JSONArray userMessages = conversations.getJSONArray("userMessages");
            JSONArray botMessages = conversations.getJSONArray("botMessages");

            int maxLength = Math.max(userMessages.length(), botMessages.length());
            for (int i = 0; i < maxLength; i++) {
                if (i < userMessages.length()) {
                    System.out.println("User: " + userMessages.getString(i));
                }
                if (i < botMessages.length()) {
                    System.out.println("Assistant: " + botMessages.getString(i));
                }
            }
            
            return responseJson;
        } else {
                System.out.println("Error loading chat history");
           
        }
        return null;
    }

    
    
    // main
    public static void main(String[] args) throws IOException {
        // Example usage
        //registerUser("testUser", "password123");
        loginUser("testUser", "password123");
        sendMessage("Define Matter",false,"6461e402-2579-413a-aeea-5d4e977804c5");
        loadConversations();
        loadChatHistory("6461e402-2579-413a-aeea-5d4e977804c5");
    }
}
