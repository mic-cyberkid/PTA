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
            "flowchart TD\n" +
            "    A--> B\n" +
            "    A-->C\n" +
            "    B-->D\n" +
            "    C-->Data" +
            "</div>" + """
                       <div class='mermaid'>
                           flowchart TD
                                 %% Swimlanes (invisible grouping)
                                 subgraph User
                                     A[Start] --> B[User Authentication]
                                     B -->|Login/Register| C[Post Item]
                                     F -->|Search| G[Item Detail Page]
                                     G --> H[Message Seller]
                                     G --> I[Add to Wishlist]
                                     G --> J[Buy Now]
                                     K[View Wishlist] --> G
                                 end
                             
                                 subgraph System
                                     C --> D{Admin Approval?}
                                     D -->|Yes| E[Save to Database]
                                     D -->|No| C
                                     E --> F[Browsing & Search]
                                     H --> L[Messaging Inbox]
                                     J --> M[Mark as Purchased]
                                     M --> N[Notify Seller]
                                     O[Flag Item] --> D
                                 end
                             
                                 subgraph Admin
                                     P[Admin Dashboard] -->|Review Items| D
                                     P -->|Manage Users| B
                                     P -->|Check Reports| O
                                     Q[Send Notifications] --> N
                                 end
                             
                                 %% Styling
                                 style User fill:#f9f9f9,stroke:#eee
                                 style System fill:#f0f7ff,stroke:#ccc
                                 style Admin fill:#fff0f0,stroke:#ccc \n
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
