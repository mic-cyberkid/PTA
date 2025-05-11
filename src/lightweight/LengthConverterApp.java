import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.HashMap;

public class LengthConverterApp extends Application {

    // Define the dataset
    private static final String[][] lA = {
            {"Length", "lA"},
            {"Meter", "iv", "iv"},
            {"Kilometer", "iv*1000", "iv/1000"},
            {"Centimeter", "iv*0.01", "iv/0.01"},
            {"Millimeter", "iv*0.001", "iv/0.001"},
            {"Micrometer", "iv*0.000001", "iv/0.000001"},
            {"Nanometer", "iv*0.000000001", "iv/0.000000001"},
            {"Mile", "iv*1609.344", "iv/1609.344"},
            {"Yard", "iv*0.9144", "iv/0.9144"},
            {"Foot", "iv*0.3048", "iv/0.3048"},
            {"Inch", "iv*0.0254", "iv/0.0254"},
            {"Light Year", "iv*9.46066e+15", "iv/9.46066e+15"},
    };

    // Store conversion factors (to and from meters)
    private final HashMap<String, Double> toMeterMap = new HashMap<>();
    private final HashMap<String, Double> fromMeterMap = new HashMap<>();

    @Override
    public void start(Stage primaryStage) {
        loadConversionData();

        TextField valueField = new TextField();
        ComboBox<String> fromUnit = new ComboBox<>();
        ComboBox<String> toUnit = new ComboBox<>();
        Label resultLabel = new Label();

        for (int i = 1; i < lA.length; i++) {
            String unit = lA[i][0];
            fromUnit.getItems().add(unit);
            toUnit.getItems().add(unit);
        }

        fromUnit.setValue("Kilometer");
        toUnit.setValue("Inch");

        Button convertButton = new Button("Convert");
        convertButton.setOnAction(e -> {
            try {
                double value = Double.parseDouble(valueField.getText());
                String from = fromUnit.getValue();
                String to = toUnit.getValue();

                double meters = value * toMeterMap.get(from);
                double converted = meters * fromMeterMap.get(to);

                resultLabel.setText(String.format("%.6f %s = %,.6f %s", value, from, converted, to));
            } catch (NumberFormatException ex) {
                resultLabel.setText("Please enter a valid number.");
            }
        });

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.add(new Label("Value:"), 0, 0);
        grid.add(valueField, 1, 0);
        grid.add(new Label("From:"), 0, 1);
        grid.add(fromUnit, 1, 1);
        grid.add(new Label("To:"), 0, 2);
        grid.add(toUnit, 1, 2);
        grid.add(convertButton, 1, 3);
        grid.add(resultLabel, 1, 4);

        primaryStage.setTitle("Length Unit Converter");
        primaryStage.setScene(new Scene(grid, 400, 250));
        primaryStage.show();
    }

    private void loadConversionData() {
        for (int i = 1; i < lA.length; i++) {
            String unit = lA[i][0];
            String toMeters = lA[i][1].replace("iv*", "");
            String fromMeters = lA[i][2].replace("iv/", "");
            toMeterMap.put(unit, Double.parseDouble(toMeters));
            fromMeterMap.put(unit, 1.0 / Double.parseDouble(toMeters));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
