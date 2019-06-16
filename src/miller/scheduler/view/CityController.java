package miller.scheduler.view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import miller.scheduler.domain.User;
import miller.scheduler.service.CityService;
import miller.scheduler.service.CountryService;
import miller.scheduler.service.dto.CityDto;
import miller.scheduler.service.dto.CountryDto;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class CityController extends AnchorPane implements Initializable {

    @FXML
    private TableView<CityDto> cityTable;

    @FXML
    private TableColumn<CityDto, Integer> colCityId;

    @FXML
    private TableColumn<CityDto, String> colCity;

    @FXML
    private TableColumn<CityDto, String> colCountry;

    @FXML
    private TextField txtFldCity;

    @FXML
    private ComboBox<CountryDto> cmbCountry;

    private CountryDto selectedCountryDto;

    private Integer cityId;
    private User user;

    private CityService cityService;
    private CountryService countryService;

    public void setUser(User user) {
        this.user = user;
    }

    public void setCityService(CityService cityService) {
        this.cityService = cityService;

        cityTable.setItems(FXCollections.observableArrayList(cityService.findAllAsDto()));
    }

    public void setCountryService(CountryService countryService) {
        this.countryService = countryService;

        cmbCountry.setItems(FXCollections.observableArrayList(countryService.findAllAsDto()));
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // transform data into integer property
        colCityId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
        colCity.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCity()));
        colCountry.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCountry().getCountry()));

        cityTable.setItems(FXCollections.observableArrayList());

        cityTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                setForm(newSelection);
            }
        });

        Callback<ListView<CountryDto>, ListCell<CountryDto>> cellFactory = new Callback<ListView<CountryDto>, ListCell<CountryDto>>() {
            @Override
            public ListCell<CountryDto> call(ListView<CountryDto> l) {
                return new ListCell<CountryDto>() {

                    @Override
                    protected void updateItem(CountryDto item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            selectedCountryDto = item;
                            setText(item.getCountry());
                        }
                    }
                };
            }
        };

        cmbCountry.setButtonCell(cellFactory.call(null));
        cmbCountry.setCellFactory(cellFactory);
    }

    @FXML
    private void handleDeleteButtonEvent() {
        CityDto city = cityTable.getSelectionModel().getSelectedItem();

        if (city != null) {
            if (!cityService.delete(city.getId())) {
                Alert alert = ViewUtils.showValidationErrors(Arrays.asList("Deleting city will violate referential integrity."));
                alert.showAndWait();
            }
        }
        resetForm();
    }

    @FXML
    private void handleClearButtonEvent() {
        resetForm();
    }

    @FXML
    private void handleSaveButtonEvent() {
        CityDto cityDto = new CityDto();
        cityDto.setId(cityId);
        cityDto.setCity(txtFldCity.getText());

        if (selectedCountryDto != null) {
            cityDto.setCountry(selectedCountryDto);
        }

        if (cityDto.isValid()) {
            if (cityId == null) {
                cityService.create(cityDto, user.getUserName());
            } else {
                cityService.update(cityDto, user.getUserName());
            }
            resetForm();

        } else {
            Alert alert = ViewUtils.showValidationErrors(cityDto.violations());
            alert.showAndWait();
        }
    }

    private void setForm(CityDto cityDto) {
        cityId = cityDto.getId();
        txtFldCity.setText(cityDto.getCity());
        selectedCountryDto = cityDto.getCountry();

        // loop through all of the items in the country combobox and find the one
        // that matches the selected one
        cmbCountry.getItems()
                .stream()
                .filter(country -> cityDto.getCountry().getId() == country.getId())
                .findAny()
                .ifPresent(cmbCountry.getSelectionModel()::select);
    }

    private void resetForm() {
        cityId = null;
        selectedCountryDto = null;
        txtFldCity.clear();
        cmbCountry.getSelectionModel().clearSelection();

        reloadTable();
    }

    private void reloadTable() {
        cityTable.setItems(FXCollections.observableArrayList(cityService.findAllAsDto()));
    }
}
