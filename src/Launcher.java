

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
public class Launcher extends Application {
    
    // variables to enable dragging
    private double xOffset = 0;
    private double yOffset = 0;
    private boolean isDragging = false;

    @Override
    public void start(Stage stage) {

        try {
            Class.forName("org.sqlite.JDBC");
            Parent root = FXMLLoader.load(getClass().getResource("res/Home.fxml"));
            Scene scene = new Scene(root);
            
            //Add custom dragging
            root.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
                isDragging = true;
                
                
            });
            
            root.setOnMouseReleased(event -> isDragging = false);
            
            root.setOnMouseDragged(event -> {
                if(isDragging){
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
            });
            
            
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.getIcons().add(new Image(getClass().getResource("res/images/rounded/image1.png").toExternalForm()));
            stage.setResizable(false);
            LoginController login = new LoginController(stage);
            HomeController home = new HomeController(stage);
            ChatPaneController chatPane = new ChatPaneController(stage);
            stage.show();

        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String args[]) {
        launch(args);
    }

}
