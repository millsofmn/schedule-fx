package miller.scheduler.view;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import miller.scheduler.domain.User;
import miller.scheduler.service.AddressService;
import miller.scheduler.service.CityService;
import miller.scheduler.service.CustomerService;
import miller.scheduler.service.dto.AddressDto;
import miller.scheduler.service.dto.CityDto;
import miller.scheduler.service.dto.CustomerDto;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class CustomerController extends AnchorPane implements Initializable {
    private final Logger log = Logger.getLogger(CustomerController.class.getName());

    @FXML
    private TableView<CustomerDto> customerTableView;

    @FXML
    private TableColumn<CustomerDto, Integer> colCustomerId;

    @FXML
    private TableColumn<CustomerDto, String> colCustomerName;

    @FXML
    private TableColumn<CustomerDto, Boolean> colCustomerActive;

    @FXML
    private TableColumn<CustomerDto, String> colPostalCode;

    @FXML
    private TableColumn<CustomerDto, String> colPhone;

    @FXML
    private TextField txtFldCustomerName;

    @FXML
    private TextField txtFldAddress1;

    @FXML
    private TextField txtFldAddress2;

    @FXML
    private ComboBox<CityDto> cmbCity;

    @FXML
    private TextField txtFldPostalCode;

    @FXML
    private TextField txtFldCountry;

    @FXML
    private TextField txtFldPhone;

    @FXML
    private CheckBox chkBxActive;

    private CityDto selectedCityDto;

    private Integer customerId;
    private Integer addressId;

    private User user;
    private AddressService addressService;
    private CityService cityService;
    private CustomerService customerService;

    public void setUser(User user) {
        this.user = user;
    }

    public void setAddressService(AddressService addressService) {
        this.addressService = addressService;
    }

    public void setCityService(CityService cityService) {
        this.cityService = cityService;
        cmbCity.setItems(FXCollections.observableArrayList(cityService.findAllAsDto()));
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
        customerTableView.setItems(FXCollections.observableArrayList(customerService.findAllAsDto()));
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colCustomerId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
        colCustomerName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCustomerName()));
        colCustomerActive.setCellValueFactory(data -> new SimpleBooleanProperty(data.getValue().isActive()));
        colPostalCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAddress().getPostalCode()));
        colPhone.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAddress().getPhone()));

        customerTableView.setItems(FXCollections.observableArrayList());

        customerTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                setForm(newSelection);
            }
        });

        // city drop down
        Callback<ListView<CityDto>, ListCell<CityDto>> cellFactory = new Callback<ListView<CityDto>, ListCell<CityDto>>() {
            @Override
            public ListCell<CityDto> call(ListView<CityDto> l) {
                return new ListCell<CityDto>() {
                    @Override
                    protected void updateItem(CityDto item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            selectedCityDto = item;
                            setText(item.getCity());
                            txtFldCountry.setText(item.getCountry().getCountry());
                        }
                    }
                };
            }
        };

        cmbCity.setButtonCell(cellFactory.call(null));
        cmbCity.setCellFactory(cellFactory);

    }

    @FXML
    private void handleDeleteButtonEvent() {
        CustomerDto customer = customerTableView.getSelectionModel().getSelectedItem();
        log.info("Deleting customer id = " + customer.getId() + ", address id = " + customer.getAddress().getId());

        if(customer != null) {
            boolean customerResult = customerService.delete(customer.getId());

            if(!customerResult){
                Alert alert = ViewUtils.showValidationErrors(Arrays.asList("Deleting customer will violate referential integrity."));
                alert.showAndWait();
            } else {
                boolean addressResult = addressService.delete(customer.getAddress().getId());
                log.info("Results for customer = " + customerResult + ", address = " + addressResult);
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

        log.info("Save started ...");
        log.info("Customer Id = " + customerId + ", Address Id = " + addressId);

        AddressDto addressDto = new AddressDto();
        addressDto.setId(addressId);
        addressDto.setPhone(txtFldPhone.getText());
        addressDto.setAddress(txtFldAddress1.getText());
        addressDto.setAddress2(txtFldAddress2.getText());
        addressDto.setPostalCode(txtFldPostalCode.getText());
        addressDto.setCity(selectedCityDto);

        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(customerId);
        customerDto.setCustomerName(txtFldCustomerName.getText());
        customerDto.setActive(chkBxActive.isSelected());
        customerDto.setAddress(addressDto);

        log.info("Object to save : " + customerDto);

        if (customerDto.isValid() && addressDto.isValid()) {
            log.info("Customer and Address are valid");

            if (addressId == null) {
                log.info("Creating new address");
                addressDto = addressService.create(addressDto, user.getUserName());
                customerDto.setAddress(addressDto);
            } else {
                log.info("Updating address");
                addressService.update(addressDto, user.getUserName());
            }

            if (customerId == null) {
                log.info("Creating new customer");
                customerService.create(customerDto, user.getUserName());
            } else {
                log.info("Updating customer");
                customerService.update(customerDto, user.getUserName());
            }
            resetForm();

        } else {
            log.severe("Errors in validating Address and Customer");
            List<String> errors = new ArrayList<>();
            errors.addAll(customerDto.violations());
            errors.addAll(addressDto.violations());

            Alert alert = ViewUtils.showValidationErrors(errors);
            alert.showAndWait();
        }

    }

    private void setForm(CustomerDto customerDto) {
        log.info("Setting customer to : " + customerDto);

        customerId = customerDto.getId();
        addressId = customerDto.getAddress().getId();

        log.info("Customer Id = " + customerId + ", Address Id = " + addressId);

        selectedCityDto = customerDto.getAddress().getCity();

        txtFldCustomerName.setText(customerDto.getCustomerName());
        txtFldAddress1.setText(customerDto.getAddress().getAddress());
        txtFldAddress2.setText(customerDto.getAddress().getAddress2());
        txtFldPostalCode.setText(customerDto.getAddress().getPostalCode());
        txtFldCountry.setText(customerDto.getAddress().getCity().getCountry().getCountry());
        txtFldPhone.setText(customerDto.getAddress().getPhone());
        chkBxActive.setSelected(customerDto.isActive());

        cmbCity.getItems()
                .stream()
                .filter(city -> customerDto.getAddress().getCity().getId().equals(city.getId()))
                .findAny()
                .ifPresent(cmbCity.getSelectionModel()::select);
    }

    private void resetForm() {
        customerId = null;
        addressId = null;
        selectedCityDto = null;
        cmbCity.getSelectionModel().clearSelection();

        txtFldCustomerName.clear();
        txtFldAddress1.clear();
        txtFldAddress2.clear();
        txtFldPostalCode.clear();
        txtFldCountry.clear();
        txtFldPhone.clear();
        chkBxActive.setSelected(true);

        reloadTable();
    }

    private void reloadTable() {
        customerTableView.setItems(FXCollections.observableArrayList(customerService.findAllAsDto()));
    }

}
