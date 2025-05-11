import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class MermaidOfflineExample extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create WebView and WebEngine
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        
        // Load local HTML file that will reference the locally stored Mermaid.js
        String htmlContent = loadHtmlContent();
        webEngine.loadContent(htmlContent);
        
        // Create scene and stage
        StackPane root = new StackPane();
        root.getChildren().add(webView);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Mermaid Offline in JavaFX WebView");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private String loadHtmlContent() {
        String script = getClass().getResource("lightweight/mermaid.min.js").toExternalForm();
        String mathJax = getClass().getResource("lightweight/tex-mml-chtml.min.js").toExternalForm();
        String htmlTemplate = "<html>" +
            "<head>" +
            "<script type='text/javascript' src='"+ script + "'></script>" + // Change to local path
            "<script async type='text/javascript' src='"+ mathJax + "'></script>" +
            "<script type='text/javascript'>" +
            "mermaid.initialize({ startOnLoad: true });" +
            "</script>" +
            "</head>" +
            "<body>" +
            "<div class='mermaid'>" +
            "graph LR\n" +
            "    A--> B\n" +
            "    A-->C\n" +
            "    B-->D\n" +
            "    C-->Data" +
            "</div>" + """
                       <div class='mermaid'>
                           graph LR\n
                             A --> Review of Motion\n
                             B --> Definition and Importance of Kinematics\n
                             C --> Distance, Displacement, Speed, Velocity, Acceleration \n
                             D -->Solving for Distance and Displacement \n
                             E --> Problem Set: Review and Apply Key Concepts \n
                       
                             A --> Linear vs. Non-Linear Motion \n
                             B --> Uniform, Variable, and Circular Motion \n
                             C --> Real-Life Applications of Each Type of Motion \n
                             D --> Identifying the Type of Motion in a Scenario \n
                             E --> Problem Set: Apply Kinematic Concepts to Real-Life Situations \n
                       
                             A --> Distance Formula  \n
                             B --> Solving for Distance Using the Formula \n
                             C --> Calculating Distances in Everyday Life \n
                             D --> Finding Distance Using a Diagram \n
                             E --> Problem Set: Applying Distance Formulas \n
                       
                             A --> Velocity Formula  \n
                             B --> Solving for Velocity Using the Formula \n
                             C --> Understanding Velocity Graphs \n
                             D --> Finding Acceleration from Velocity Data \n
                             E --> Problem Set: Applying Velocity Formulas \n\n
                       
                             A --> Review of Key Concepts \n
                             B --> Application Problems with Answers \n
                             C --> Formal Assessment of Kinematic Understanding \n
                             E --> Reflection on Progress and Future Learning \n
                         </div>
                       """+
                " <p>Inline math: \\( a^2 + b^2 = c^2 \\)</p>\n" +
"    <p>Display math:</p>\n" + "<p>"+
"    \\[ E = mc^2 \\]" +"</p>"+
            "</body>" +
            "</html>";
        
        // Replace with the correct file path for your local mermaid.min.js
        return htmlTemplate.replace("file:///path/to/mermaid.min.js", "file:///" + Paths.get("resources/mermaid/mermaid.min.js").toAbsolutePath());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
