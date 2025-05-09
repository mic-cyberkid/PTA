import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author pc
 */
public class HomeController implements Initializable {

    private static Stage stage;

     // variables to enable dragging
    private double xOffset = 0;
    private double yOffset = 0;
    private boolean isDragging = false;
    private boolean isMaximized = false;
    private double prevWidth, prevHeight, prevX, prevY;
    
    
    public HomeController(Stage stage) {
        this.stage = stage;
    }
    
    @FXML
    private VBox dynamicBox;
    @FXML
    private VBox homePage;
    
    @FXML
    private VBox resize_box;
    

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
    
    
    
    
    @FXML
    void closeWindow(MouseEvent event) {
        stage.close();
        System.exit(0);

    }

    @FXML
    void minimizeWindow(MouseEvent event) {
        stage.setIconified(true);
    }
    @FXML
    void resizeWindow(MouseEvent event) {
        //Work on changing icons for resize
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        if(!isMaximized){
            prevX = stage.getX();
            prevY = stage.getY();
            prevWidth = stage.getWidth();
            prevHeight = stage.getHeight();
            
            stage.setX(0);
            stage.setY(0);
            stage.setWidth(screenWidth);
            stage.setHeight(screenHeight);
            isMaximized = true;
            ImageView img = new ImageView(new Image(getClass().getResource("res/images/icons/resize_white.png").toExternalForm()));
            img.setFitHeight(25);
            img.setFitWidth(25);
            resize_box.getChildren().set(0, img);
            
        }else{
            ImageView img = new ImageView(new Image(getClass().getResource("res/images/icons/maximize_white.png").toExternalForm()));
            img.setFitHeight(25);
            img.setFitWidth(25);
            resize_box.getChildren().set(0, img);
            stage.setX(prevX);
            stage.setY(prevY);
            stage.setWidth(prevWidth);
            stage.setHeight(prevHeight);
            isMaximized = false;
        }
        

    }
    
}
