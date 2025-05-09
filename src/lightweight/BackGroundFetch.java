import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 *
 * @author Prince
 */
public class BackGroundFetch implements Callable<String>{

    String message;
    JSONArray context;
    String response;
    
    
    public BackGroundFetch(JSONArray ctxt){
        this.context = ctxt;
        
    }
     //Display Warnings and Messages
    private void showAlert(Alert.AlertType type, String title, String msg){
        Alert alert = new Alert(type);
        alert.setHeaderText(title);
        alert.setContentText(msg);
        alert.show();
    }
    public String getResponse(){
        return this.response;
    }

    @Override
    public String call(){
        try {
            // Create the HTTP client
            HttpClient client = HttpClient.newHttpClient();
            
            // Create a JSON object to send in the body of the POST request
            // '{"model":"llama3.2", "prompt":"As a physics teacher define what matter is", "stream":false, "format":"json"}'
            /* For LLAMA REQUESTS
            // llama url = http://127.0.0.1:11434/api/generate 
          
            */
           
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("model", "llama3.2");
            jsonBody.put("stream", false);
            jsonBody.put("messages", context);
            System.out.println(jsonBody);
           
           // jsonBody.put("prompt", message); // Single meessage.
           // jsonBody.put("stream", false);
            // Create the POST request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://127.0.0.1:11434/api/chat"))  // Flask server endpoint
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody.toString(), StandardCharsets.UTF_8))
                    .build();
            
            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            // Print the response
            JSONObject parseResponse = new JSONObject(response.body());
            System.out.println(response.body().toString());
            int statusCode = response.statusCode();
            System.out.println("Response code: " + statusCode);
            if( statusCode >= 500){
                return "An error occured while fetching response.\nProbably a server side error.";
            }
            System.out.println("Response body: " + parseResponse.getJSONObject("message").getString("content"));
            return  parseResponse.getJSONObject("message").getString("content");
        } catch( ConnectException  ex){
            ex.printStackTrace();
            Platform.runLater(() -> {
                showAlert(Alert.AlertType.ERROR, "An error occured while fetching response.", "Probably a network error.");
            
            });
            
            return "An error occured while fetching response.\nProbably a network error.";
        }catch (IOException ex) {
            ex.printStackTrace();
            Platform.runLater(() -> {
                 showAlert(Alert.AlertType.ERROR, "An error occured while fetching response.", "Probably a network error.");
            });
            return "An error occured while fetching response.\nProbably a network error.";
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            Platform.runLater(() -> {
                showAlert(Alert.AlertType.ERROR, "Program Interrupt", "Process was interrupted");
            });
            
            return "An error occured while fetching response.";
        }
        
        
    }
}
