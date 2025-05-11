import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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
    private TextField tempResult;
    @FXML
    private ComboBox<?> tempFromBox;
    @FXML
    private ComboBox<?> tempToBox;
    @FXML
    private TextArea tempExplainArea;
    @FXML
    private Tab timeTab;
    @FXML
    private TextField timeResult;
    @FXML
    private ComboBox<?> timeFromBox;
    @FXML
    private ComboBox<?> timeToBox;
    @FXML
    private TextArea timeExplainArea;
    @FXML
    private Tab lengthTab;
    @FXML
    private TextField lengthResult;
    @FXML
    private ComboBox<?> lengthFromBox;
    @FXML
    private ComboBox<?> lengthToBox;
    @FXML
    private TextArea lengthExplainArea;
    @FXML
    private Tab areaTab;
    @FXML
    private TextField areaResult;
    @FXML
    private ComboBox<?> areaFromBox;
    @FXML
    private ComboBox<?> areaToBox;
    @FXML
    private TextArea areaExplainArea;
    @FXML
    private Tab weightTab;
    @FXML
    private TextField weightResult;
    @FXML
    private ComboBox<?> weightFromBox;
    @FXML
    private ComboBox<?> weightToBox;
    @FXML
    private TextArea weightExplainArea;
    @FXML
    private Tab volumeTab;
    @FXML
    private TextField volumeResult;
    @FXML
    private ComboBox<?> volumeFromBox;
    @FXML
    private ComboBox<?> volumeToBox;
    @FXML
    private TextArea volumeExplainArea;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // Clear all tabs except home tab
        MainTab.getTabs().clear();
        MainTab.getTabs().add(homeTab);
    }    

    @FXML
    private void addTemperatureTab(ActionEvent event) {
        if(MainTab.getTabs().contains(temperatureTab) == false){
            MainTab.getTabs().add(temperatureTab);
            temperatureTab.getContent().setFocusTraversable(true);
        }else{
            MainTab.getSelectionModel().select(temperatureTab);
        }
    }

    @FXML
    private void addTimeTab(ActionEvent event) {
    }

    @FXML
    private void addLengthTab(ActionEvent event) {
    }

    @FXML
    private void addAreaTab(ActionEvent event) {
    }

    @FXML
    private void addVolumeTab(ActionEvent event) {
    }

    @FXML
    private void addWeightTab(ActionEvent event) {
    }

    @FXML
    private void temperatureConvert(ActionEvent event) {
    }

    @FXML
    private void temperatureSwap(ActionEvent event) {
    }

    @FXML
    private void temperatureExplain(ActionEvent event) {
    }

    @FXML
    private void timeConvert(ActionEvent event) {
    }

    @FXML
    private void timeSwap(ActionEvent event) {
    }

    @FXML
    private void timeExplain(ActionEvent event) {
    }

    @FXML
    private void lengthConvert(ActionEvent event) {
    }

    @FXML
    private void lengthSwap(ActionEvent event) {
    }

    @FXML
    private void lengthExplain(ActionEvent event) {
    }

    @FXML
    private void areaConvert(ActionEvent event) {
    }

    @FXML
    private void areaSwap(ActionEvent event) {
    }

    @FXML
    private void areaExplain(ActionEvent event) {
    }

    @FXML
    private void weightConvert(ActionEvent event) {
    }

    @FXML
    private void weightSwap(ActionEvent event) {
    }

    @FXML
    private void weightExplain(ActionEvent event) {
    }

    @FXML
    private void volumeConvert(ActionEvent event) {
    }

    @FXML
    private void volumeSwap(ActionEvent event) {
    }

    @FXML
    private void volumeExplain(ActionEvent event) {
    }
    
}
