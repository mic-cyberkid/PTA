

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author pc
 */
public class SignupPaneController implements Initializable {

    @FXML
    private Label loginInfoLabel;
    @FXML
    private TextField username_field;
    @FXML
    private PasswordField password_field;
    @FXML
    private Button backBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

  
    @FXML
    public void loginUser(ActionEvent event) {
        if(username_field.getText().isEmpty() || password_field.getText().isEmpty()){
            loginInfoLabel.setText("Please fill all fields.");
            return;
        }else{
            String username = username_field.getText();
            String password = password_field.getText();
            
            try{
                String query = "insert into users(username, password) values(?,?)";
                String query2 = "select * from users where username = ? and password = ?";
                PreparedStatement stmt1, stmt2;
                stmt1 = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
                stmt1.setString(1, username);
                
                stmt1.setString(2, UtilityMethods.getMD5(password));
                stmt1.execute();
                stmt1.close();
                stmt2 = DatabaseConnection.getInstance().getConnection().prepareStatement(query2);
                stmt2.setString(1, username);
                stmt2.setString(2, password);
                ResultSet result  = stmt2.executeQuery();
                while(result.next()){
                    User authUser = new User();
                    authUser.setUsername(result.getString("username"));
                    authUser.setUserId(result.getInt("user_id"));
                    Session.setAuthUser(authUser);
                }
                loginInfoLabel.setStyle("-fx-font-family: Century; -fx-font-size: 21px; -fx-text-fill: #61bf49;");
                loginInfoLabel.setText("Signed Up!");
                Platform.runLater(() -> {
                    try {
                        Thread.sleep(3000);
                        loadHomePane(event);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SignupPaneController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                
                
            }catch(SQLException e){
                System.out.println("Error : "+e.getMessage());
                loginInfoLabel.setStyle("-fx-font-family: Century; -fx-font-size: 21px; -fx-text-fill: #f54949;");
                loginInfoLabel.setText("User Already exists !");
                return;
            }
        
        }

    }
    
    // Back to home page
    public void loadHomePane(ActionEvent event){
        try {
                Parent root = FXMLLoader.load(getClass().getResource("/res/Home.fxml"));
                Scene scene = new Scene(root);
                Node node = (Node)event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                stage.setScene(scene);
                stage.setResizable(true);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
            } 

    }
    
    
    public void loadChatPane(ActionEvent event){
        try {
                Parent root = FXMLLoader.load(getClass().getResource("/res/ChatPane.fxml"));
                Scene scene = new Scene(root);
                Node node = (Node)event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                stage.setScene(scene);
                stage.setResizable(true);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
            } 

    }
    

    @FXML
    private void backToHome(ActionEvent event) {
        try {
                
                Parent root = FXMLLoader.load(getClass().getResource("/res/Home.fxml"));
                Scene scene = new Scene(root);
                Node node = (Node)event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                stage.setScene(scene);
                stage.setResizable(true);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
            } 

    }
    
    
}
