

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.ResultSet;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.StageStyle;


public class LoginController implements Initializable {
    
   
    private static Stage  stage;

    public LoginController(Stage stage) {
        this.stage = stage;
    }
    
    public LoginController(){}

    @FXML
    private Label loginInfoLabel;

    @FXML
    private TextField password_field;

    @FXML
    private TextField username_field;
    
    @FXML
    private Button backBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Stage:"+stage);
    }
    
    @FXML
    public void loginUser(ActionEvent event) {
        if(username_field.getText().isEmpty() || password_field.getText().isEmpty()){
            loginInfoLabel.setText("Please fill all fields.");
            
        }else{
            String username = username_field.getText();
            String password = UtilityMethods.getMD5(password_field.getText());
            
            try{
                String query = "select * from users where username = ? and password = ?";
                PreparedStatement stmt;
                stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
                stmt.setString(1, username);
                stmt.setString(2, password);
                ResultSet result  = stmt.executeQuery();
                
                if(result.next()){
                    User authUser = new User();
            
                    String authUname = result.getString("username");
                    int uid = result.getInt("user_id");
                    System.out.println("UID:"+uid);
                    System.out.println("Username:"+authUname);
                    stmt.close();
                    authUser.setUsername(authUname);
                    authUser.setUserId(uid);
                    Session.setAuthUser(authUser);
                    loginInfoLabel.setStyle("-fx-text-fill: lime; -fx-font-family: Century; -fx-font-size: 14px;");
                    loginInfoLabel.setText("Login Successful!");
                    System.out.println("Stage:"+stage);
                    loadChatPane(event);
                    return;
                    
                }
                loginInfoLabel.setStyle("-fx-text-fill: red; -fx-font-family: Century; -fx-font-size: 14px;");
                loginInfoLabel.setText("Wrong Details !");
                
                
                
            }catch(SQLException e){
                System.out.println("Error : "+e.getMessage());
                loginInfoLabel.setStyle("-fx-text-fill: red; -fx-font-family: Century; -fx-font-size: 21px;");
                loginInfoLabel.setText("An error occured during login!");
              
            }

        }

    }
    
    //Method to load chat pane
    public void loadChatPane(ActionEvent event){
        
        try {
                
                Parent root = FXMLLoader.load(getClass().getResource("/res/ChatPane.fxml"));
                Scene scene = new Scene(root);
                //Stage newstage = new Stage();
                Node node = (Node)event.getSource();
                //Stage newstage  = (Stage) node.getScene().getWindow();
                stage.getIcons().add(new Image(getClass().getResource("/res/images/rounded/image1.png").toExternalForm()));
                stage.setScene(scene);
                System.out.println("Old Stage:"+stage);
                ChatPaneController chatPane = new ChatPaneController(stage);
               
                stage.close();
                System.out.println("New Stage: "+stage);
                stage.setResizable(true);
                
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
            } 

    }

    //Method to load home pane
    @FXML
    private void backToHome(ActionEvent event) {
        try {
                
                Parent root = FXMLLoader.load(getClass().getResource("/res/Home.fxml"));
                Scene scene = new Scene(root);
                LoginController login = new LoginController(stage);
                HomeController home = new HomeController(stage);
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
