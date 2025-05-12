import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author pc
 */
public class ConverterController implements Initializable {

    @FXML
    private TabPane MainTab;
    @FXML
    private Tab homeTab;
    @FXML
    private Tab temperatureTab;
    @FXML
    private ComboBox<String> tempFromBox;
    @FXML
    private ComboBox<String> tempToBox;
    @FXML
    private VBox tempExplainArea;
    @FXML
    private Tab timeTab;
    @FXML
    private TextField timeResult;
    @FXML
    private ComboBox<String> timeFromBox;
    @FXML
    private ComboBox<String> timeToBox;
    @FXML
    private VBox timeExplainArea;
    @FXML
    private Tab lengthTab;
    @FXML
    private TextField lengthResult;
    @FXML
    private ComboBox<String> lengthFromBox;
    @FXML
    private ComboBox<String> lengthToBox;
    @FXML
    private VBox lengthExplainArea;
    @FXML
    private Tab areaTab;
    @FXML
    private TextField areaResult;
    @FXML
    private ComboBox<String> areaFromBox;
    @FXML
    private ComboBox<String> areaToBox;
    @FXML
    private VBox areaExplainArea;
    @FXML
    private Tab weightTab;
    @FXML
    private TextField weightResult;
    @FXML
    private ComboBox<String> weightFromBox;
    @FXML
    private ComboBox<String> weightToBox;
    @FXML
    private VBox weightExplainArea;
    @FXML
    private Tab volumeTab;
    @FXML
    private TextField volumeResult;
    @FXML
    private ComboBox<String> volumeFromBox;
    @FXML
    private ComboBox<String> volumeToBox;
    @FXML
    private VBox volumeExplainArea;
    
    private JSONObject dataset;
    @FXML
    private TextField tempInput;
    @FXML
    private TextField timeInput;
    @FXML
    private TextField lengthInput;
    @FXML
    private TextField areaInput;
    @FXML
    private TextField weightInput;
    @FXML
    private TextField volumeInput,tempResult;
    private HBox explainRegion;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // Clear all tabs except home tab
        MainTab.getTabs().clear();
        MainTab.getTabs().add(homeTab);
        
        //Load dataset
        dataset = loadDataset();
        
        // Load combo boxes
        // Load length box
        JSONObject lenghtConversions = dataset.getJSONObject("Length");
        JSONObject areaConversions = dataset.getJSONObject("Area");
        JSONObject timeConversions = dataset.getJSONObject("Time");
        JSONObject volumeConversions = dataset.getJSONObject("Volume");
        JSONObject weightConversions = dataset.getJSONObject("Weight");
        JSONObject temperatureConversions = dataset.getJSONObject("Temperature");
        
        fillBox(lenghtConversions,lengthFromBox,lengthToBox);
        fillBox(areaConversions,areaFromBox,areaToBox);
        fillBox(timeConversions,timeFromBox,timeToBox);
        fillBox(volumeConversions,volumeFromBox,volumeToBox);
        fillBox(weightConversions,weightFromBox,weightToBox);
        fillBox(temperatureConversions,tempFromBox,tempToBox);
        
        // Set initial conversions
        lengthFromBox.getSelectionModel().select(0);
        lengthToBox.getSelectionModel().select(1);
        areaFromBox.getSelectionModel().select(0);
        areaToBox.getSelectionModel().select(1);
        timeFromBox.getSelectionModel().select(0);
        timeToBox.getSelectionModel().select(1);
        volumeFromBox.getSelectionModel().select(0);
        volumeToBox.getSelectionModel().select(1);
        tempFromBox.getSelectionModel().select(0);
        tempToBox.getSelectionModel().select(1);
        weightFromBox.getSelectionModel().select(0);
        weightToBox.getSelectionModel().select(1);
        
    }    

    // Method to load combo box
    public void fillBox(JSONObject conversion, ComboBox fromBox, ComboBox toBox){
        for(String key: conversion.keySet()){
            fromBox.getItems().add(key);
            toBox.getItems().add(key);
        }
    }
    
    
    @FXML
    private void addTemperatureTab(ActionEvent event) {
        if(MainTab.getTabs().contains(temperatureTab) == false){
            MainTab.getTabs().add(temperatureTab);
            temperatureTab.getContent().setFocusTraversable(true);
            MainTab.getSelectionModel().select(temperatureTab);
        }else{
            MainTab.getSelectionModel().select(temperatureTab);
        }
    }

    @FXML
    private void addTimeTab(ActionEvent event) {
        if(MainTab.getTabs().contains(timeTab) == false){
            MainTab.getTabs().add(timeTab);
            timeTab.getContent().setFocusTraversable(true);
            MainTab.getSelectionModel().select(timeTab);
        }else{
            MainTab.getSelectionModel().select(timeTab);
        }
    }

    @FXML
    private void addLengthTab(ActionEvent event) {
        if(MainTab.getTabs().contains(lengthTab) == false){
            MainTab.getTabs().add(lengthTab);
            lengthTab.getContent().setFocusTraversable(true);
            MainTab.getSelectionModel().select(lengthTab);
        }else{
            MainTab.getSelectionModel().select(lengthTab);
        }
    }

    @FXML
    private void addAreaTab(ActionEvent event) {
        if(MainTab.getTabs().contains(areaTab) == false){
            MainTab.getTabs().add(areaTab);
            areaTab.getContent().setFocusTraversable(true);
            MainTab.getSelectionModel().select(areaTab);
        }else{
            MainTab.getSelectionModel().select(areaTab);
        }
    }

    @FXML
    private void addVolumeTab(ActionEvent event) {
        if(MainTab.getTabs().contains(volumeTab) == false){
            MainTab.getTabs().add(volumeTab);
            volumeTab.getContent().setFocusTraversable(true);
            MainTab.getSelectionModel().select(volumeTab);
        }else{
            MainTab.getSelectionModel().select(volumeTab);
        }
    }

    @FXML
    private void addWeightTab(ActionEvent event) {
        if(MainTab.getTabs().contains(weightTab) == false){
            MainTab.getTabs().add(weightTab);
            weightTab.getContent().setFocusTraversable(true);
        }else{
            MainTab.getSelectionModel().select(weightTab);
        }
    }

    
    // Method to handle calculations [Obeying DRY]
    public void doCalculation(String value, String fromUnit, String toUnit, String unit, TextField output){
        if(!value.isEmpty()){
            
            try{
                Double.parseDouble(value);
                Object result = convert(value, fromUnit, toUnit, unit);
                output.setText(result.toString());
            
            }catch(Exception ex){
                UtilityMethods.showAlert(Alert.AlertType.INFORMATION, "Conversion Error", "Please enter numerical values only.");
                return;
            
            }
        }else{
            UtilityMethods.showAlert(Alert.AlertType.ERROR, "Conversion Error", "Please enter a value.");
        }
    }
    
    //Method to explain conversions
    public void explainConversion(String fromUnit, String toUnit, VBox output){
        try {
            String prompt = "Briefly describe how I can convert "+ fromUnit +" to "+ toUnit;
            JSONObject response = ApiFunctions.explainConversion(prompt);
            if(response != null){
                String explanation = response.getString("response");
                Text txt = new Text(explanation);
                txt.setFill(Paint.valueOf("white"));
                TextFlow tflow = new TextFlow(txt);
                tflow.setTextAlignment(TextAlignment.JUSTIFY);
                tflow.setPrefWidth(400);
                output.getChildren().clear();
                output.getChildren().add(txt);
                
              
            }else{
                output.getChildren().clear();
                output.getChildren().add(new Text());
            }
        } catch (Exception e) {
            output.getChildren().clear();
            output.getChildren().add(new Text());
        }
    }
    
    
    @FXML
    private void temperatureConvert(ActionEvent event) {
        String fromUnit = tempFromBox.getSelectionModel().getSelectedItem();
        String toUnit = tempToBox.getSelectionModel().getSelectedItem();
        String unit = "Temperature";
        String value = tempInput.getText();
        doCalculation(value, fromUnit, toUnit, unit, tempResult);
        
    }

    
    @FXML
    private void temperatureSwap(ActionEvent event) {
        String fromUnit = tempFromBox.getSelectionModel().getSelectedItem();
        String toUnit = tempToBox.getSelectionModel().getSelectedItem();
        //Switch conversions
        tempToBox.getSelectionModel().select(fromUnit);
        tempFromBox.getSelectionModel().select(toUnit);
        String unit = "Temperature";
        String value = tempInput.getText();
        doCalculation(value, toUnit, fromUnit, unit, tempResult);
    }

    @FXML
    private void temperatureExplain(ActionEvent event) {
        String fromUnit = tempFromBox.getSelectionModel().getSelectedItem();
        String toUnit = tempToBox.getSelectionModel().getSelectedItem();
        explainConversion(fromUnit, toUnit, tempExplainArea);
    }

    @FXML
    private void timeConvert(ActionEvent event) {
        String fromUnit = timeFromBox.getSelectionModel().getSelectedItem();
        String toUnit = timeToBox.getSelectionModel().getSelectedItem();
        String unit = "Time";
        String value = timeInput.getText();
        doCalculation(value, fromUnit, toUnit, unit, timeResult);
    }

    @FXML
    private void timeSwap(ActionEvent event) {
        String fromUnit = timeFromBox.getSelectionModel().getSelectedItem();
        String toUnit = timeToBox.getSelectionModel().getSelectedItem();
        String unit = "Time";
        String value = timeInput.getText();
        //Switch conversions
        timeToBox.getSelectionModel().select(fromUnit);
        timeFromBox.getSelectionModel().select(toUnit);
        doCalculation(value, toUnit, fromUnit, unit, timeResult);
    }

    @FXML
    private void timeExplain(ActionEvent event) {
        String fromUnit = timeFromBox.getSelectionModel().getSelectedItem();
        String toUnit = timeToBox.getSelectionModel().getSelectedItem();
        explainConversion(fromUnit, toUnit, timeExplainArea);
        
    }

    @FXML
    private void lengthConvert(ActionEvent event) {
        String fromUnit = lengthFromBox.getSelectionModel().getSelectedItem();
        String toUnit = lengthToBox.getSelectionModel().getSelectedItem();
        String unit = "Length";
        String value = lengthInput.getText();
        doCalculation(value, fromUnit, toUnit, unit, lengthResult);
    }

    @FXML
    private void lengthSwap(ActionEvent event) {
        String fromUnit = lengthFromBox.getSelectionModel().getSelectedItem();
        String toUnit = lengthToBox.getSelectionModel().getSelectedItem();
        String unit = "Length";
        String value = lengthInput.getText();
        //Switch conversions
        lengthToBox.getSelectionModel().select(fromUnit);
        lengthFromBox.getSelectionModel().select(toUnit);
        doCalculation(value,toUnit, fromUnit, unit, lengthResult);
    }

    @FXML
    private void lengthExplain(ActionEvent event) {
        String fromUnit = lengthFromBox.getSelectionModel().getSelectedItem();
        String toUnit = lengthToBox.getSelectionModel().getSelectedItem();
        explainConversion(fromUnit, toUnit, lengthExplainArea);
    }

    @FXML
    private void areaConvert(ActionEvent event) {
        String fromUnit = areaFromBox.getSelectionModel().getSelectedItem();
        String toUnit = areaToBox.getSelectionModel().getSelectedItem();
        String unit = "Area";
        String value = areaInput.getText();
        doCalculation(value, fromUnit, toUnit, unit, areaResult); 
    }

    @FXML
    private void areaSwap(ActionEvent event) {
        String fromUnit = areaFromBox.getSelectionModel().getSelectedItem();
        String toUnit = areaToBox.getSelectionModel().getSelectedItem();
        String unit = "Area";
        String value = areaInput.getText();
        //Switch conversions
        areaToBox.getSelectionModel().select(fromUnit);
        areaFromBox.getSelectionModel().select(toUnit);
        doCalculation(value, toUnit, fromUnit, unit, areaResult);
    }

    @FXML
    private void areaExplain(ActionEvent event) {
        String fromUnit = areaFromBox.getSelectionModel().getSelectedItem();
        String toUnit = areaToBox.getSelectionModel().getSelectedItem();
        explainConversion(fromUnit, toUnit, areaExplainArea);
    }

    @FXML
    private void weightConvert(ActionEvent event) {
        String fromUnit = weightFromBox.getSelectionModel().getSelectedItem();
        String toUnit = weightToBox.getSelectionModel().getSelectedItem();
        String unit = "Weight";
        String value = weightInput.getText();
        doCalculation(value, fromUnit, toUnit, unit, weightResult);
    }

    @FXML
    private void weightSwap(ActionEvent event) {
        String fromUnit = weightFromBox.getSelectionModel().getSelectedItem();
        String toUnit = weightToBox.getSelectionModel().getSelectedItem();
        String unit = "Weight";
        String value = weightInput.getText();
        //Switch conversions
        weightToBox.getSelectionModel().select(fromUnit);
        weightFromBox.getSelectionModel().select(toUnit);
        doCalculation(value, toUnit, fromUnit, unit, weightResult);
    }

    @FXML
    private void weightExplain(ActionEvent event) {
        String fromUnit = weightFromBox.getSelectionModel().getSelectedItem();
        String toUnit = weightToBox.getSelectionModel().getSelectedItem();
        explainConversion(fromUnit, toUnit, weightExplainArea);
    }

    @FXML
    private void volumeConvert(ActionEvent event) {
        String fromUnit = volumeFromBox.getSelectionModel().getSelectedItem();
        String toUnit = volumeToBox.getSelectionModel().getSelectedItem();
        String unit = "Volume";
        String value = volumeInput.getText();
        doCalculation(value, fromUnit, toUnit, unit, volumeResult);
    }

    @FXML
    private void volumeSwap(ActionEvent event) {
        String fromUnit = volumeFromBox.getSelectionModel().getSelectedItem();
        String toUnit = volumeToBox.getSelectionModel().getSelectedItem();
        String unit = "Volume";
        String value = volumeInput.getText();
        //Switch conversions
        volumeToBox.getSelectionModel().select(fromUnit);
        volumeFromBox.getSelectionModel().select(toUnit);
        doCalculation(value, toUnit, fromUnit, unit, volumeResult);
    }

    @FXML
    private void volumeExplain(ActionEvent event) {
        String fromUnit = volumeFromBox.getSelectionModel().getSelectedItem();
        String toUnit = volumeToBox.getSelectionModel().getSelectedItem();
        explainConversion(fromUnit, toUnit, volumeExplainArea);
    }
    
    
    // Load dataset
    public JSONObject loadDataset(){
         String jsonFile = "src/lightweight/NewDataset.json";
        try (BufferedReader br = new BufferedReader(new FileReader(jsonFile))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            JSONObject dataset = new JSONObject(sb.toString());
            System.out.println(dataset);
            return dataset;
            
        } catch (Exception e) {
            
            System.out.println("Error parsing JSON: " + e.getMessage());
            return null;
        }
    }
    
    // Convert value
    public Object convert(String iv, String fromUnit, String toUnit, String selectedUnit) {
        try {
            System.out.println("Dataset: "+dataset);
            JSONObject selectedConversion = dataset.getJSONObject(selectedUnit);
            System.out.println("SelectedConversion: "+selectedConversion);
            //JSONObject selectedConversion = dataset.getJSONObject(fromUnit).getJSONObject(toUnit);
            String baseConv = selectedConversion.getJSONObject(fromUnit).getString("toBase");
            String toConv = selectedConversion.getJSONObject(toUnit).getString("fromBase");
            
            // Iv to base unit
            Object baseValue = evalExpression(baseConv.replace("iv", String.valueOf(iv)));
            
            // Base value -> result unit
            Object toValue = evalExpression(toConv.replace("iv", String.valueOf(baseValue)));
            return toValue;
        } catch (Exception ex) {
            Logger.getLogger(UnitsConverterController.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

   // Evaluate expression
   private static Object evalExpression(String expression) throws Exception {
         // Create a new instance of the Webview and use its engine
         WebView view = new WebView();
         WebEngine engine = view.getEngine();
         Object result = engine.executeScript("eval('"+expression+"');");
        

        // Use the ScriptEngine to evaluate a string expression
        System.out.println(result);
        return result;
    }
    
}
