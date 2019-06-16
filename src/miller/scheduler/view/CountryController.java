package miller.scheduler.view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import miller.scheduler.domain.User;
import miller.scheduler.service.CountryService;
import miller.scheduler.service.dto.CountryDto;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CountryController extends AnchorPane implements Initializable {

    @FXML
    private TableView<CountryDto> countryTable;

    @FXML
    private TableColumn<CountryDto, Integer> colCountryId;

    @FXML
    private TableColumn<CountryDto, String> colCountry;


    @FXML
    private TextField txtFldCountry;

    private Integer countryId;
    private User user;
    private CountryService countryService;

    public void setUser(User user) {
        this.user = user;
    }

    public void setCountryService(CountryService countryService) {
        this.countryService = countryService;

        resetForm();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colCountryId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject()); // lambda call back function
        colCountry.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCountry()));

        countryTable.setItems(FXCollections.observableArrayList());

        countryTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                setForm(newSelection);
            }
        });
    }

    @FXML
    private void handleDeleteButtonEvent() {
        CountryDto country = countryTable.getSelectionModel().getSelectedItem();

        if(country != null) {
            if(!countryService.delete(country.getId())){
                Alert alert = ViewUtils.showValidationErrors(Arrays.asList("Deleting address will violate referential integrity."));
                alert.showAndWait();
            }
        }
        resetForm();
    }

    @FXML
    private void handleClearButtonEvent() {
        countryId = null;
        txtFldCountry.clear();
    }

    @FXML
    private void handleSaveButtonEvent() {
        CountryDto countryDto = new CountryDto();
        countryDto.setId(countryId);
        countryDto.setCountry(txtFldCountry.getText());

        if (countryDto.isValid()) {
            if (countryId == null) {
                countryService.create(countryDto, user.getUserName());
            } else {
                countryService.update(countryDto, user.getUserName());
            }
            resetForm();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ValidationError");
            alert.setHeaderText("Invalid due to the following:");
            alert.setContentText(countryDto.violations().stream().collect(Collectors.joining("\n")));
            alert.showAndWait();
        }

    }

    private void setForm(CountryDto countryDto){
        countryId = countryDto.getId();
        txtFldCountry.setText(countryDto.getCountry());
    }

    private void resetForm() {
        countryId = null;
        txtFldCountry.clear();
        reloadTable();
    }

    private void reloadTable() {
        countryTable.setItems(FXCollections.observableArrayList(countryService.findAllAsDto()));
    }
}
