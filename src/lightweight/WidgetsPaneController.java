import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class WidgetsPaneController implements Initializable{

    @FXML
    private Label create_date;
    
    @FXML
    private Label infoLabel;

    @FXML
    private PasswordField new_password;

    @FXML
    private PasswordField old_password;

    @FXML
    private Label prof_username;

    @FXML
    private Label total_chat;
    
    @FXML
    private ImageView profilePic;
    
    public Stage mainStage;
    
    User user;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        String username = Session.getAuthUser();
        prof_username.setText(username);
        total_chat.setText(String.valueOf(Session.getTotal_conversations()));
        
    }
    
    // Logout method
    @FXML
    public void logout(ActionEvent event) {
        loadPane(event, "res/Home.fxml");
        

    }

    //Load fxml pane
    public void loadPane(ActionEvent event, String page){
        try {
                Parent root = FXMLLoader.load(getClass().getResource(page));
                Scene scene = new Scene(root);
                Node node = (Node)event.getSource();
                mainStage  = (Stage) node.getScene().getWindow();
                mainStage.close();
                mainStage.getIcons().add(new Image(getClass().getResource("res/images/rounded/image1.png").toExternalForm()));
                mainStage.setScene(scene);
                mainStage.setResizable(true);
                mainStage.show();
            } catch (IOException ex) {
                Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
            } 

    }
    
    //Load UnitsConverter
    public void loadConverterPane(String page){
        try {
                Parent root = FXMLLoader.load(getClass().getResource(page));
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.getIcons().add(new Image(getClass().getResource("res/images/rounded/image1.png").toExternalForm()));
                stage.setScene(scene);
                stage.setResizable(true);
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            } 

    }
    
    // method to get user image and display
    public void setupProfile(String username){
        try{
            String query = "select image from user_profiles where username = ?";
            
            // Get user image from server via API
            /*
            while(result.next()){
                byte[] imageBytes = result.getBytes("image");
                ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
                Image image = new Image(bis);
                profilePic.setImage(image);
            }*/
        }catch(Exception e){
            System.out.println("Error : "+e); 
        }
    }
    
    // Method to update password
    @FXML
    public void updatePassword(ActionEvent event) {
        if(old_password.getText().isEmpty() || new_password.getText().isEmpty()){
                infoLabel.setText("Fill all fields!");
                infoLabel.setStyle("-fx-text-fill: red;");
                return;
            
        }
        
        try{
            String newPassword = UtilityMethods.getMD5(new_password.getText());
            String oldPassword = UtilityMethods.getMD5(old_password.getText());
            String query = "UPDATE users SET password = ? WHERE username = ? and password = ? ";
            /*
            stmt.setString(1, user.getUsername());
            stmt.setString(0, newPassword);
            stmt.setString(2, oldPassword);
            stmt.execute();
            */
            infoLabel.setText("Password Changed!");
            infoLabel.setStyle("-fx-text-fill: lime;");
            
        }catch(Exception ex){
                System.out.println("Error: "+ ex);
                infoLabel.setText("Incorrect Password!");
                infoLabel.setStyle("-fx-text-fill: red;");
                //UtilityMethods.showAlert(Alert.AlertType.NONE, "Wrong Password", "Old Password is not corect!");
        }

    }
    
    
    // Clear all chats
     @FXML
    void clearAllChats(ActionEvent event) {

    }

    // Launch game pane
    @FXML
    void launchGames(ActionEvent event) {

    }

    // Launch graph plotter
    @FXML
    void launchGrapPlotter(ActionEvent event) {

    }

    // Launch units conveter
    @FXML
    void launchUnitsConverter(ActionEvent event) {
        //Run units Converter on different thread
        /*new Thread(() ->{
            Platform.runLater(() -> {
            loadConverterPane("UnitsConverter.fxml");
            });
        }).start();*/

        Platform.runLater(() -> {
            loadConverterPane("res/UnitsConverter.fxml");
        });
        

    }


    // Launch formula sheet
    @FXML
    void openFormulaSheet(ActionEvent event) {
        Platform.runLater(() -> {
            loadConverterPane("res/FormulaSheet.fxml");
        });
    }
    
    @FXML
    private void minimizeWindow(ActionEvent event){
        Stage stage = (Stage)((javafx.scene.Node) event.getSource()).getScene().getWindow();
    }
    

}
