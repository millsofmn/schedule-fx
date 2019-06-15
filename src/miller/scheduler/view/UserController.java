package miller.scheduler.view;

import java.net.URL;
import java.util.ResourceBundle;

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
import miller.scheduler.service.UserService;

public class UserController  extends AnchorPane implements Initializable {

    @FXML
    private TableView<User> tblUser;

    @FXML
    private TableColumn<User, Integer> colUserId;

    @FXML
    private TableColumn<User, String> colUsername;

    @FXML
    private TableColumn<User, String> colPassword;

    @FXML
    private TextField txtFldUsername;

    @FXML
    private TextField txtFldPassword;

    private UserService userService;

    private Integer userId;
    private User user;

    public void setUserService(UserService userService) {
        this.userService = userService;
        tblUser.setItems(FXCollections.observableArrayList(userService.findAll()));
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colUserId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
        colUsername.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUserName()));
        colPassword.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPassword()));

        tblUser.setItems(FXCollections.observableArrayList());

        tblUser.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                setForm(newSelection);
            }
        });
    }

    @FXML
    private void handleDeleteButtonEvent(){
        User user = tblUser.getSelectionModel().getSelectedItem();

        if(user != null){
            userService.delete(user.getId());
        }
        resetForm();
    }

    @FXML
    private void handleClearButtonEvent(){
        resetForm();
    }

    @FXML
    private void handleSaveButtonEvent(){
        User user = new User();
        user.setId(userId);
        user.setUserName(txtFldUsername.getText());
        user.setPassword(txtFldPassword.getText());

        if(user.isValid()){
            userService.save(user, user.getUserName());
            resetForm();
        } else {
            Alert alert = ViewUtils.showValidationErrors(user.violations());
            alert.showAndWait();
        }
    }
    private void setForm(User user){
        userId = user.getId();
        txtFldUsername.setText(user.getUserName());
        txtFldPassword.setText(user.getPassword());
    }

    private void resetForm(){
        userId = null;
        txtFldUsername.clear();
        txtFldPassword.clear();

        reloadTable();
    }

    private void reloadTable(){
        tblUser.setItems(FXCollections.observableArrayList(userService.findAll()));
    }
}
