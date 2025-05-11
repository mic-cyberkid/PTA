import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.stage.Screen;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 *
 */
public class ChatPaneController implements Initializable {

    
    @FXML
    private BorderPane MainPane;
    @FXML
    private ScrollPane HistoryPane;
    @FXML
    private Accordion chatHistoryAccordion;
    @FXML
    private TextArea userMessage;
    @FXML
    private ScrollPane chatPane;
    @FXML
    private VBox chatsBox;
 
    @FXML
    private Pane welcomeDialog;

    @FXML
    private Label welcomeMsgBox;
  
    private static Stage stage; 
    ArrayList<Chat> ChatHistory;
    
    HashMap<String, Chat> UserChats; // Chat lookup table
    
    
    User activeUser;
    
    /* State of Chat Pane
    |  0 for new users with zero history, 
    |  1 for user with history just logged in,
    |  2 for 'new chat' for user with history (pressed new chat button),
    |  3 for continue chat (pressed load chat)
    */
    int state; 
    
    ArrayList<String> UserMessages;
    ArrayList<String> ChatBotMessages;
    
    //USE HashMap instead of arraylist so we can pair each message and response.
    
    @FXML
    private TitledPane noHistory;
    private ColorPicker fgColorPicker;
    private ColorPicker bgColorPicker;
    @FXML
    private Button newChatBtn;
    @FXML
    private ImageView sendBtn;
    @FXML
    private ProgressIndicator chatProgress;
    
    public Accordion widget;
    
    private Spinner font_Size;
    @FXML
    private HBox menuPane;
    
    private Pane welcomePane;
    
    
    
    
    // variables to enable dragging
    private double xOffset = 0;
    private double yOffset = 0;
    private boolean isDragging = false;
    private boolean isMaximized = false;
    private double prevWidth, prevHeight, prevX, prevY;
    @FXML
    private Button createQuizPrompt;
    @FXML
    private Button explainConceptPrompt;
    @FXML
    private VBox resize_box;
    private HTMLTextFlow chatWebView;
    
    private WebEngine webEngine;
    private boolean newChat;
    

    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        /************************
        * TODO : Make code clean after build (o-^)
         */
        
        newChat = true;
        // Initialize Variables and User Interface
        chatWebView = new HTMLTextFlow();
        chatWebView.setStyle("-fx-background-coleor:  linear-gradient(to bottom, #2c3e50, #4ca1af); -fx-background-radius: 10px; -fx-border-radius: 10px;");
        //Add custom dragging
        System.out.println("ChatPane stage :"+stage);
        MainPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
            isDragging = true;
            


        });

        MainPane.setOnMouseDragReleased(event ->{
            isDragging = false;
            checkForSnap(stage);
        });

        MainPane.setOnMouseDragged(event -> {
            if(isDragging){
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });
        webEngine = chatWebView.getWebEngine();
        
        String markedJsPath = getClass().getResource("lightweight/Marked.js").toExternalForm();
        String mathJaxPath = getClass().getResource("lightweight/tex-mml-chtml.min.js").toExternalForm();
        
        // TODO: Add mermaid to display diagrams
        String mermaidPath = getClass().getResource("lightweight/mermaid.min.js").toExternalForm();
        
        System.out.println("Path:"+mathJaxPath);
        System.out.println("Path:"+markedJsPath);
        
        
        String initialContent = """
        <!DOCTYPE html>
        <html>
        <head>
        <style>
          body { font-family: Arial, sans-serif; background-color: #f5f5f5; margin: 10px; padding: 10px; }
          .message { max-width: 80%; border-radius: 10px; margin: 5px; }
          .user { background-color: #2c3e50; color: white; align-self: flex-end; text-align: justify; margin: 3px; padding: 5px; }
          .bot { background-color: #e0e0e0; color: black; align-self: flex-start; text-align: left; margin: 5px; padding: 10px; overflow-wrap: break-word; }
          pre{ background-color: #000102; border-radius: 10px; color: wheat; box-shadow: 2px 2px grey; padding:5px; margin: 5px; }
          /* Table style for the chat */
              table {
                border-collapse: separate;
                width: 100%;
              }
              
              th, td {
                border: 1px solid #ddd;
                padding: 10px;
                text-align: left;
                font-size: 14px;
                font-weight: bold;
                color: #333;
              }
              
              th {
                background-color: #f0f0f0;
              }
              
              tr:nth-child(even) {
                background-color: #f9f9f9;
              }
              
              table caption {
                margin-bottom: 10px;
                text-align: center;
              }
              
              table caption img {
                max-width: 50%;
                height: auto;
              }
              
              table .header-row {
                background-color: #333;
                color: #fff;
                font-size: 18px;
                font-weight: bold;
                padding: 5px;
              }
              /* Add a fade-in effect when the table is loaded */
              table {
                animation: fadeIn 1s;
              }
              
              @keyframes fadeIn {
                from {
                  opacity: 0;
                }
                to {
                  opacity: 1;
                }
              }
          
          .typing { font-weight: bold; animation: blink 1s infinite; }
          @keyframes blink { 0%% { opacity: 1; } 50%% { opacity: 0; } 100%% { opacity: 1; } }
          #chatbox { display: flex; flex-direction: column; }
        </style>""" +
         "<script src='" + markedJsPath + "'></script>" +
         " <script async src='" + mathJaxPath + "'></script>" +
         
         """
         <script type="text/javascript">
           window.onload = function() {
             if (typeof MathJax !== 'undefined' && MathJax.Hub) {
               MathJax.Hub.Queue(["Typeset", MathJax.Hub, 'chatbox']);
             }
           };
         
           function renderMarkdown(markdownText, user) {
             // Parse markdown using marked.js
             var htmlContent = marked.parse(markdownText);
         
             // Create a message container div
             var div = document.createElement('div');
             div.className = 'message ' + user;
             div.innerHTML = htmlContent;
         
             // Append to chatbox
             document.getElementById('chatbox').appendChild(div);
         
             // Scroll to bottom
             window.scrollTo(0, document.body.scrollHeight);
         
             // Trigger MathJax rendering for math content
             if (typeof MathJax !== 'undefined' && MathJax.Hub) {
               MathJax.Hub.Queue(["Typeset", MathJax.Hub, div]);
             }
           }
         </script>
         """ +
        """
        </head>
        <body>
          <div id="chatbox" class="mermaid"></div>
        </body>
        </html>
        """;
        
        webEngine.loadContent(initialContent);
        //Hide chat history
        MainPane.setLeft(null);
        //Load welcome dialog
        welcomePane = loadWelcomePage();
        
        // Display welcome message
        welcomeMsgBox.setText("Welcome "+Session.getAuthUser()+"!");
        
      
        
        
        // Space out chat messages
        chatsBox.setSpacing(5);
        UserChats = new HashMap<>();
        
        //set some features
        editImageButton(sendBtn);
        
        //load widget
        widget = loadWidgets();
        
        if(Session.getTotal_conversations() < 1){
            state = 0; // fresh chat, fresh user
        }else{
            // remove dummy chat history pane
            chatHistoryAccordion.getPanes().remove(noHistory);
            //Populate history pane
            loadChatsPane();
            // load last conversation
             
            state = 1;
        }
       
        
 
        
    }    
    

    /* ******************************
    * UTILITY AND APP LOGIC METHODS
    *******************************/
    
    private String parseMarkdownAndLatex(String markdown) {
        // You can replace $ for inline math and $$ for block math
        // Inline: \( ... \)
        markdown = markdown.replaceAll("\\$(.*?)\\$", "\\\\($1\\\\)");  // Inline math
        // Block: \[ ... \]
        markdown = markdown.replaceAll("\\$\\$(.*?)\\$\\$", "\\\\[$1\\\\]");  // Block math
        return markdown;
    }
    
    private void executeJavaScript(String functionName, String markdownMessage, String senderClass) {
       Gson gson = new Gson();
       String jsonMessage = gson.toJson(markdownMessage);
       String jsonClass = gson.toJson(senderClass);
       String script = functionName + "(" + jsonMessage + ", " + jsonClass + ");";
       chatWebView.getWebEngine().executeScript(script);
    }

    
    //Check for window snap
    public void checkForSnap(Stage stage){
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        double x = stage.getX();
        double y = stage.getY();
        
        if(y<= 0){
            maximizeWindow(stage, screenWidth, screenHeight);
        }else if(x <= 0){
            snapWindow(stage,0,0,screenWidth/2.0, screenHeight);
            
            //snapWindow(stage, screenWidth/2.0, screenWidth/2.0, screenHeight);
            
        }else if(x + stage.getWidth() >= screenWidth){
            snapWindow(stage,screenWidth/2, 0, screenWidth/2, screenHeight);
            
        }
    }
    
    public void snapWindow(Stage stage, double x, double y, double screenWidth, double screenHeight){
        stage.setX(x);
        stage.setY(y);
        stage.setWidth(screenWidth);
        stage.setHeight(screenHeight);
        isMaximized = false;
        
    }
    
    public void restoreWindow(Stage stage){
        stage.setX(prevX);
        stage.setY(prevY);
        stage.setWidth(prevWidth);
        stage.setHeight(prevHeight);
        isMaximized = false;
    }
    
    
    // Method to maximize window
    public void maximizeWindow(Stage stage, double screenWidth, double screenHeight){
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
            
        }else{
            restoreWindow(stage);
        }
        
    }
    
    
    //method to load welcom pane
    public Pane loadWelcomePage(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/res/welcomeDialog.fxml"));
            WelcomePaneController welcomeController = new WelcomePaneController(this);
            loader.setController(welcomeController);
            Pane pane = loader.load();
            System.out.println(pane);
            return pane;
        } catch (Exception e) {
            System.out.println("Error: "+ e);
            return null;
        }
    }
    
    //Show or hide chat history
    @FXML
    void showChatHistory(MouseEvent event) {
        if(MainPane.getLeft() == null){
            MainPane.setLeft(HistoryPane);
        }else{
            MainPane.setLeft(null);
        }
    }
    
    public void streamMessage(String htmlMessage) {
        String senderClass = "bot";

        // Create an empty div for the message and add a typing indicator
        String initScript = String.format(
            "var div = document.createElement('div');" +
            "div.className = 'message %s';" +
            "div.innerHTML = '<span class=\"typing\">...</span>';" + // Typing dots
            "document.getElementById('chatbox').appendChild(div);" +
            "window.scrollTo(0, document.body.scrollHeight);" +
            "div", 
            senderClass
        );

        Platform.runLater(() -> {
            JSObject messageDiv = (JSObject) chatWebView.getWebEngine().executeScript(initScript);
            System.out.println(messageDiv);
            startStreaming(htmlMessage, messageDiv);
        });
    }

    private void startStreaming(String htmlMessage, JSObject messageDiv) {
        new Thread(() -> {
            try {
                Thread.sleep(500); // Show typing indicator for a moment
                Platform.runLater(() -> messageDiv.eval("this.innerHTML = '';")); // Remove typing indicator

                String[] words = htmlMessage.split(" ");
                for (String word : words) {
                    String appendScript = String.format(
                        "this.innerHTML += '%s ';" + 
                        "window.scrollTo(0, document.body.scrollHeight);", 
                        word.replace("'", "\\'")
                    );

                    Platform.runLater(() -> messageDiv.eval(appendScript));
                    Thread.sleep(150); // Adjust speed of word streaming
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    
    public void addMessage(String markdownMessage, boolean isUser) {
        String senderClass = isUser ? "user" : "bot";
        executeJavaScript("renderMarkdown",markdownMessage, senderClass);   
    }
    
    
    //Load widget from fxml
    public Accordion loadWidgets(){
        try {
            Accordion widget = FXMLLoader.load(getClass().getResource("/res/widgetsPane.fxml"));
            return widget;
        } catch (Exception e) {
            e.printStackTrace();
            return new Accordion();
        }
    }
    
   
     
    //Process user input
    private void processUserInput(){
        // perform various checks
        if(userMessage.getText().isEmpty()){
            
            userMessage.setText("Please enter message here");
            userMessage.getStyleClass().add("warning");
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
                userMessage.getStyleClass().remove("warning");
                userMessage.setText("");
            }
            ));
            timeline.setCycleCount(1);
            timeline.play();
            
             return;
        }
        
        //remove welcome dialog
        if(chatsBox.getAlignment() == Pos.CENTER){
            chatsBox.getChildren().clear();
            chatsBox.setAlignment(Pos.TOP_LEFT);
            chatsBox.getChildren().add(chatWebView);
            chatsBox.setFillWidth(true);
        }
        
        String message = userMessage.getText();
        userMessage.clear();
        addMessage(message, true);
        chatProgress.setVisible(true);
        
        // enable progress while receiving response
        Platform.runLater(()->{
            chatProgress.setVisible(true);
        });
        // Send Message and get response
        BackGroundFetch getRes = new BackGroundFetch(message, Session.isNewChat(), Session.getConversation_id());
        
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> response = (Future<String>) executor.submit(getRes);
        executor.submit(() -> {
            try {
                String chatbotMessage =  response.get();
                chatProgress.setVisible(false);
                
                //update ui
                Platform.runLater(() -> {
                    try {
                        if(response.get() != null){
                            
                            //Display chatbot message
                            addMessage(chatbotMessage, false);
                       
                            switch (state) {
                                // New chat
                                case 0 -> {
                                   
                                    //remove Dummy chatHistory
                                    chatHistoryAccordion.getPanes().remove(noHistory);
                                    appendHistory(message, chatbotMessage);
                                    state = 3; //Set state for chat continuation
                                    return;
                                }
                                case 2 -> {
                                    //Initialize chat settings
                                    //HandleChat(message, chatbotMessage);
                                    appendHistory(message, chatbotMessage);
                                    state = 3; //Set state for chat continuation
                                    return;
                                }
                                case 1 -> {
                                    //HandleChat(message, chatbotMessage);
                                    //remove Dummy chatHistory
                                    chatHistoryAccordion.getPanes().remove(noHistory);
                                    appendHistory(message, chatbotMessage);
                                    state = 3; //Set state for chat continuation
                                    return;
                                }
                                default -> {
                                    //Continuing a chat
                                    // Save and keep track of chats for both user and chatbot
                                    
                                }
                            }
                            
                        }
                    } catch (InterruptedException | ExecutionException ex) {
                        Logger.getLogger(ChatPaneController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
          
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(ChatPaneController.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            executor.shutdown();
        }
    });
        // Check conversation ids before adding message
    }
   
    
    
 

    //Method to generate historyPane for accordion
    public TitledPane makeChatPane(String chatId, String title, String description){
        //create main pane
        TitledPane pane = new TitledPane();
        pane.setStyle("-fx-font-size: 14px; -fx-font-family: Century; -fx-text-fill: white;");
        pane.setAnimated(true);
        pane.setId(chatId);
        // Create Vbox to hold contents
        VBox content = new VBox();
        VBox.setVgrow(pane, Priority.ALWAYS);
        
        // Create label for description
        Label desc = new Label(description);
        desc.setStyle("-fx-font-size: 16px; -fx-font-family: Century; -fx-text-fill: #575757;");
        desc.setTextOverrun(OverrunStyle.CENTER_ELLIPSIS);
        desc.setWrapText(true);
        
        // Create Hbox for buttons
        HBox btnWrap = new HBox();
        btnWrap.setSpacing(7);
        //Create buttons
        Button loadBtn = new Button("   Load");
        loadBtn.setTooltip(new Tooltip("Load chat"));
        loadBtn.setId("loadbtn");
        loadBtn.setPrefWidth(70.0);
        loadBtn.setPrefHeight(10.0);
        loadBtn.setEffect(new DropShadow());
        
        //TODO: Add onclick listener to perform neccessary actions for both buttons
        // For loadBtn ensure we save current chat state before loading new one after clearing initial state
        loadBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent eh) {
                String selectedChatId = chatHistoryAccordion.getExpandedPane().getId();
                //Clear current pane
                chatsBox.getChildren().clear();
                chatsBox.setAlignment(Pos.TOP_LEFT);
                chatsBox.setFillWidth(true);
                chatWebView.getWebEngine().executeScript("document.getElementById('chatbox').innerHTML = '';");
                chatsBox.getChildren().add(chatWebView);
                // Set converstion id
                Session.setConversation_id(selectedChatId);
                // Load chatBox
                loadChatBox(selectedChatId);
                state = 3;
            }
        });
        
        
        // Create delete button
        Button deleteBtn = new Button("    Delete");
        deleteBtn.setId("deletebtn");
        
        deleteBtn.setTooltip(new Tooltip("Delete chat"));
      
        //deleteBtn.setGraphic(iconView2);
        deleteBtn.setPrefWidth(70.0);
        deleteBtn.setPrefHeight(10.0);
        deleteBtn.setEffect(new DropShadow());
        
        
        deleteBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent eh) {
                String selectedChatId = chatHistoryAccordion.getExpandedPane().getId();
                // Get selected chat from chatList
                if(selectedChatId == Session.getConversation_id()){
                    Alert prompt = new Alert(Alert.AlertType.CONFIRMATION);
                    prompt.setHeaderText("Permission to delete current chat.");
                    prompt.setContentText("Are you sure you want to delete this chat?");
                    Optional<ButtonType> result = prompt.showAndWait();
                    if(result.isPresent() && result.get() == ButtonType.OK){
                        // remove and delete chat
                        chatsBox.getChildren().clear();
                        // Delete chat pane from history pane, do it well
                        chatHistoryAccordion.getPanes().remove(chatHistoryAccordion.getExpandedPane());
                        // Clear session conversation id
                        Session.setConversation_id(null);
                        // TODO: Delete chat from local database too
                        deleteChat(selectedChatId);
                        openNewChat(eh);
                        state = 0;
                    }
                }else{
                    // remove and delete chat
                    chatsBox.getChildren().clear();
                    // Delete chat pane from history pane, do it well
                    chatHistoryAccordion.getPanes().remove(chatHistoryAccordion.getExpandedPane());

                    // TODO: Delete chat from local database too
                    deleteChat(selectedChatId);
                    state = 0;
                }
            }
        });
        // Wrap butttons
        btnWrap.getChildren().addAll(loadBtn, deleteBtn);
        
        //Fill content
        content.getChildren().addAll(desc, btnWrap);
        
        
        pane.setText(title);
        pane.setExpanded(true);
        pane.setCollapsible(true);
        pane.setAnimated(true);
        
        // Set pane content
        pane.setContent(content);
        
        return pane;
    }
    
    
    //Add chat to histpane
    public void appendHistory(String message, String chatBotMessage ){
        String[] message_split = message.split(" ");
        String title = message_split[0];
        TitledPane chatHist = makeChatPane(Session.getConversation_id(), title, chatBotMessage);
        chatHist.setStyle("-fx-background-radius: 50px;");
        chatHistoryAccordion.getPanes().add(chatHist);

    }  
    
    // Load Previous chats pane
    public void loadChatsPane(){
        //Loop through chat list, create a pane for it and load it
        // Make request to the /conversation endpoint
        try {
            /* [{
                "conversation_id": conv_id,
                "conversation_header": header,
                "conversation_desc": description
            }]*/
            JSONObject response = ApiFunctions.loadConversations();
            int total_convos = response.getInt("total");
            if(total_convos > 0){
                JSONArray conversations = response.getJSONArray("messages");
                System.out.println("JSONArray : "+ conversations);
                for(Object conversation : conversations){
                    JSONObject chatDesc = (JSONObject) conversation;
                    String chat_id = chatDesc.getString("conversation_id");
                    String header = chatDesc.getString("conversation_header");
                    String description = chatDesc.getString("conversation_desc");
                    chatHistoryAccordion.getPanes().add(makeChatPane(chat_id, header, description));
                }
            }else{
                // Display noHistory pane
                System.out.println("No Chat history!");
            }
      
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    
    
    //Display Warnings and Messages
    private static void showAlert(AlertType type, String title, String msg){
        Alert alert = new Alert(type);
        alert.setHeaderText(title);
        alert.setContentText(msg);
        alert.show();
    }


    /* *****************\
    | DATABASE METHODS  |
    \*******************/
    
    //Fetch userMessages. Call this method when load Button is clicked
    public void loadChatBox(String chatId){
        //Clear initial texts on screen
        chatWebView.getWebEngine().executeScript("document.getElementById('chatbox').innerHTML = '';");
        try{
            
            //Fetch chat messages
            JSONObject responseJson = ApiFunctions.loadChatHistory(chatId);
            JSONObject conversations = responseJson.getJSONObject("conversations");

            JSONArray userMessages = conversations.getJSONArray("userMessages");
            JSONArray botMessages = conversations.getJSONArray("botMessages");

            int maxLength = Math.max(userMessages.length(), botMessages.length());
            for (int i = 0; i < maxLength; i++) {
                if (i < userMessages.length()) {
                    addMessage(userMessages.getString(i), true);
                    System.out.println("User: " + userMessages.getString(i));
                }
                if (i < botMessages.length()) {
                    addMessage(botMessages.getString(i), false);
                    System.out.println("Assistant: " + botMessages.getString(i));
                }
            }
 
        }catch(IOException ex){
            System.out.println("Error:"+ ex.getMessage());
       
        }
    }
    
    
    //Delete Chat from database
    private void deleteChat(String chatId){
        //TODO : Make request to delete chat from database
        try {
            JSONObject response = ApiFunctions.deleteChatHistory(chatId);
            if(response != null){
                String msg = response.getString("status");
                if(msg.equals("deleted")){
                    showAlert(Alert.AlertType.INFORMATION, "Chat", "Message deleted");
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    /* **********************\
    | THEME AND UI METHODS   |
    \************************/

    //Generic button editing 
    //Modify code to take two colors also
    public void editBtn(Button btn, String color1, String color2){
        btn.setOnMouseEntered(eh -> {
            String style = "-fx-background-color: "+ color1 +"; -fx-padding: 10 20; -fx-border-radius: 5; -fx-background-radius: 5;";
            btn.setStyle(style);
        });
        
        btn.setOnMouseExited(eh -> {
            String style = "-fx-background-color: "+ color2 +"; -fx-padding: 10 20; -fx-border-radius: 5; -fx-background-radius: 5;";
            btn.setStyle(style);
        });
        
        btn.setOnMousePressed(eh -> {
            ScaleTransition sclTrans = new ScaleTransition(Duration.millis(100), btn);
            sclTrans.setToX(0.9);
            sclTrans.setToY(0.9);
            sclTrans.play();
            
        });
        
        
        btn.setOnMouseReleased(eh -> {
            ScaleTransition sclTrans = new ScaleTransition(Duration.millis(100), btn);
            sclTrans.setToX(1.0);
            sclTrans.setToY(1.0);
            sclTrans.play();
            
        });
    }
    
    // Add image to button
    public void editImageButton(ImageView img){
        ScaleTransition scaleTrans = new ScaleTransition(Duration.millis(200), img);
        scaleTrans.setToX(1.2);
        scaleTrans.setToY(1.2);
        scaleTrans.setCycleCount(2);
        scaleTrans.setAutoReverse(true);
        
        img.setOnMousePressed(eh -> {
            scaleTrans.play();
        
        });
    }


    @FXML
    private void openNewChat(ActionEvent event) {
        
        //TODO: Clear current chatsBox
        newChat = true;
        Session.setNewChat(newChat);
        
        chatsBox.getChildren().clear();
        System.out.println("ChatBox : "+ chatsBox);
        chatsBox.getChildren().add(welcomePane);
        chatsBox.setAlignment(Pos.CENTER);
        chatsBox.setFillWidth(false);
        chatWebView.getWebEngine().executeScript("document.getElementById('chatbox').innerHTML = '';");
        
        // TODO : HTML WELCOME CARD and delete it when user press send button
        
        //Clear current converstion id
        Session.setConversation_id(null);
        // Set state
        if(state == 1){
            state = 2;
        }
        state = 0;
    }

    @FXML
    private void sendMessage(MouseEvent event) {
        // send user input to backend server
        processUserInput();
        
    }
    
    //COPY MESSAGE
    private void handleTextFlowClick(MouseEvent event, TextFlow textFlow){
        // Get the text from the TextFlow
        StringBuilder textToCopy = new StringBuilder();
        for(var node : textFlow.getChildren()){
            if(node instanceof Text){
                textToCopy.append(((Text) node).getText());
            }
        }
        
        // Copy the text to the clipboard
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(textToCopy.toString());
        clipboard.setContent(content);
        
        
        
    }

    //Set font size
    private void setFontColor(ActionEvent event) {
        String fgColor = fgColorPicker.getValue().toString().replace("0x", "#");
        //TODO: Logic to set font color
        //chatsBox.setStyle("-fx-text-fill:"+fgColor+";"); 
    }

    //Set background color
    private void setBodyColor(ActionEvent event) {
        String bgColor = bgColorPicker.getValue().toString().replace("0x", "#");
        chatsBox.setStyle("-fx-background-color: black; -fx-background-radius: 10px; ");
    }

    @FXML
    private void showWidgetsPane(MouseEvent event) {
        // Load widgets pane from file and embed it ib border pane (right) side.
        if(MainPane.getRight() == null){
            MainPane.setRight(widget);
        }else{
            MainPane.setRight(null);
        }
        
    }
    



    @FXML
    private void curriculumIntegrationPrompt(ActionEvent event) {
        String prompt = "Using the curriculum document, provide guidance on how a given topic aligns with the curriculum."
                + "Ask me for the topic first, then retrieve relevant information from the curriculum document.";
        

    }

    @FXML
    void explainConceptPrompt(ActionEvent event) {
        String prompt = "I need an explanation of a concept. "
                + "Ask me which concept I want to learn about, then provide a clear and concise explanationwith examples.";
        userMessage.setText(prompt);
        processUserInput();
    }

     @FXML
    void quizPrompt(ActionEvent event) {

        String prompt = "Generate a set of quiz questions for a topic. "
                + "Ask me for the topic first, and then provide a mix of multiple choice and short-answer questions";
        userMessage.setText(prompt);
        processUserInput();
    }
    
    
    @FXML
    void lessonPlannerPrompt(ActionEvent event) {
        String promptOne = "I need a lesson plan for teaching the topic I'll specify in my next message.";
        String promptTwo = "Help me design a lesson plan with key concepts and activities for the topic I'll specify in my next message.";
        String promptThree = "Create a structured lesson plan for the topic I'll specify in my next message.";
        ArrayList prompts = new ArrayList<String>();
        prompts.add(promptOne);
        prompts.add(promptTwo);
        prompts.add(promptThree);
        Random rand = new Random();
        int index = rand.nextInt(0, 2);
        System.out.println("Random integer: "+index);
        String prompt = prompts.get(index).toString();
        System.out.println("Prompt : "+prompt);
        userMessage.setText(prompt);
        processUserInput();
        

    }
    
    @FXML
    void plotGraph(ActionEvent event) {

        // Open graph pane
        
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
    
    public ChatPaneController(Stage stage) {
        this.stage = stage;
    }
    
    public ChatPaneController(){}

   
}
