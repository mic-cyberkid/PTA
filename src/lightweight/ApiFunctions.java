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
    
    

    // Function to change passw0rd 
    public static JSONObject changePassword(String old_password,String new_password) throws IOException {
        String url = BASE_URL + "/update-password";
        JSONObject body = new JSONObject();
        body.put("old_password", old_password);
        body.put("new_password", old_password);


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
            return responseJson;
        } else if (responseCode == 401 || responseCode == 403) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            JSONObject responseJson = new JSONObject(content.toString());
            System.out.println(content.toString());
            return responseJson;
        } else if (responseCode == 400 || responseCode == 404) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            JSONObject responseJson = new JSONObject(content.toString());
            System.out.println(content.toString());
            return responseJson;
        } else if (responseCode == 500 || responseCode == 502 || responseCode == 503 || responseCode == 504) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            JSONObject responseJson = new JSONObject(content.toString());
            
            System.out.println(content.toString());
            return responseJson;
        } else {
            
            System.out.println("Unexpected response: " + connection.getResponseMessage());
        }
        return null;
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
    
    
     public static JSONObject explainConversion(String message) throws IOException {
        String url = BASE_URL + "/conversion";
        JSONObject body = new JSONObject();
        body.put("message", message);

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
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
            return responseJson;
        } else {
            System.out.println("Error sending message.");
           
            UtilityMethods.showAlert(Alert.AlertType.ERROR, "Chat", "Error sending message.");
            return null;
    
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

    // Load chat history by ID
    public static JSONObject deleteChatHistory(String conversationId) throws IOException {
        String url = BASE_URL + "/conversations/" + conversationId;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("DELETE");
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
            return responseJson;
        } else {
                System.out.println("Error loading chat history");
           
        }
        return null;
    }

}
