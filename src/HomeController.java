import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author pc
 */
public class HomeController implements Initializable {

    private static Stage stage;

    public HomeController(Stage stage) {
        this.stage = stage;
    }
    
    @FXML
    private VBox dynamicBox;
    @FXML
    private VBox homePage;

    /**
     * Initializes the controller.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        System.out.println("Stage:"+stage);
        
        
    }    
    
    public HomeController(){}

    @FXML
    private void loginPage(ActionEvent event) {
        try{
            VBox loginPane = FXMLLoader.load(getClass().getResource("/res/uxLogin.fxml"));
            dynamicBox.getChildren().set(0, loginPane);
          
        }catch(IOException iox){
            iox.printStackTrace();
        }
    }
    
    @FXML
    private void minimizeWindow(ActionEvent event){
        Stage stage = (Stage)((javafx.scene.Node) event.getSource()).getScene().getWindow();
    }

    @FXML
    private void signupPage(ActionEvent event) {
        try{
            VBox signUpPane = FXMLLoader.load(getClass().getResource("/res/signupPane.fxml"));
            dynamicBox.getChildren().set(0, signUpPane);
            
        }catch(IOException iox){
            iox.printStackTrace();
        }
    }

    @FXML
    private void exitApp(ActionEvent event) {
        System.exit(0);
    }
    
}
