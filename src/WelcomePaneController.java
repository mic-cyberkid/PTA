import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class WelcomePaneController {
    
    ChatPaneController controller;
    
    public WelcomePaneController(ChatPaneController mainController){
        this.controller = mainController;
    }
    @FXML
    private Button createQuizPrompt;

    @FXML
    private Button explainConceptPrompt;

    @FXML
    private Pane welcomeDialog;

    @FXML
    private Label welcomeMsgBox;
    
    @FXML
    private VBox chatsBox;

    @FXML
    void curriculumIntegrationPrompt(ActionEvent event) {
        System.out.println("Curriculum was pressed");

    }

    @FXML
    void explainConceptPrompt(ActionEvent event) {

    }

    @FXML
    void lessonPlannerPrompt(ActionEvent event) {

    }

    @FXML
    void plotGraph(ActionEvent event) {

    }
    
     @FXML
    void quizPrompt(ActionEvent event) {

    }

}
