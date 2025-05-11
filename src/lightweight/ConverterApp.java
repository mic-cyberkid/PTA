import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.json.JSONObject;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ConverterApp extends Application {

    // Maps for unit data
    private Map<String, Map<String, JSONObject>> categoryUnits = new HashMap<>();

    // UI components
    private ComboBox<String> categoryBox = new ComboBox<>();
    private ComboBox<String> fromUnitBox = new ComboBox<>();
    private ComboBox<String> toUnitBox = new ComboBox<>();
    private TextField valueInput = new TextField();
    private Label resultLabel = new Label("Result: ");
    private Button convertButton = new Button("Convert");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the JSON data
        loadJsonData();

        // Set up the UI components
        setupUI();

        // Create the main layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(
            new Label("Select Category: "), categoryBox,
            new Label("From Unit: "), fromUnitBox,
            new Label("To Unit: "), toUnitBox,
            new Label("Value: "), valueInput,
            convertButton, resultLabel
        );

        // Create and show the scene
        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setTitle("Unit Converter");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Handle category selection
        categoryBox.setOnAction(e -> updateUnits());

        // Handle conversion logic
        convertButton.setOnAction(e -> convertUnits());
    }

    // Load the JSON file
    private void loadJsonData() throws IOException {
        // Replace with the correct path to your JSON file
        String jsonString = new String(Files.readAllBytes(Paths.get(getClass().getResource("src/lightweight/NewDataSet.json").toExternalForm())));
        JSONObject root = new JSONObject(jsonString);
        // Process the JSON data and map to categoryUnits
        for (String category : root.keySet()) {
            JSONObject units = root.getJSONObject(category);
            Map<String, JSONObject> unitsMap = new HashMap<>();
            for (String unit : units.keySet()) {
                unitsMap.put(unit, units.getJSONObject(unit));
            }
            categoryUnits.put(category, unitsMap);
        }
        // Populate the category combo box
        categoryBox.getItems().addAll(categoryUnits.keySet());
    }

    // Set up the UI components
    private void setupUI() {
        // Initially empty ComboBoxes
        fromUnitBox.setPromptText("Select unit");
        toUnitBox.setPromptText("Select unit");

        valueInput.setPromptText("Enter value");

        // Initially, no category is selected
        categoryBox.setPromptText("Select a category");
    }

    // Update the From and To unit ComboBoxes based on selected category
    private void updateUnits() {
        String selectedCategory = categoryBox.getValue();
        if (selectedCategory == null) return;

        // Get the units for the selected category
        Map<String, JSONObject> units = categoryUnits.get(selectedCategory);

        // Clear previous selections
        fromUnitBox.getItems().clear();
        toUnitBox.getItems().clear();

        // Add units to both ComboBoxes
        fromUnitBox.getItems().addAll(units.keySet());
        toUnitBox.getItems().addAll(units.keySet());
    }

    // Handle the conversion logic
    private void convertUnits() {
        // Get the selected values from the UI
        String category = categoryBox.getValue();
        String fromUnit = fromUnitBox.getValue();
        String toUnit = toUnitBox.getValue();
        String valueText = valueInput.getText();

        if (category == null || fromUnit == null || toUnit == null || valueText.isEmpty()) {
            resultLabel.setText("Please fill all fields");
            return;
        }

        try {
            double value = Double.parseDouble(valueText);

            // Retrieve the unit conversion formulas
            JSONObject fromUnitData = categoryUnits.get(category).get(fromUnit);
            JSONObject toUnitData = categoryUnits.get(category).get(toUnit);

            // Get conversion formulas
            String fromBaseFormula = fromUnitData.getString("fromBase");
            String toBaseFormula = toUnitData.getString("toBase");

            // Replace iv with the input value in the formulas
            fromBaseFormula = fromBaseFormula.replace("iv", String.valueOf(value));
            toBaseFormula = toBaseFormula.replace("iv", String.valueOf(value));

            // Convert the value using the formulas
            double intermediateValue = evaluateFormula(fromBaseFormula);
            double result = evaluateFormula(toBaseFormula);

            // Display the result
            resultLabel.setText("Result: " + result);

        } catch (NumberFormatException e) {
            resultLabel.setText("Invalid value input");
        } catch (Exception e) {
            resultLabel.setText("Error in conversion");
        }
    }

    // Improved method to evaluate formulas
    private double evaluateFormula(String formula) throws ScriptException {
        // Check for invalid characters to avoid security risks
        if (!formula.matches("[0-9+\\-*/^().\\s]*")) {
            throw new ScriptException("Invalid formula");
        }

        // Handle basic math functions and operations
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
        return ((Number) engine.eval(formula)).doubleValue();
    }
}
