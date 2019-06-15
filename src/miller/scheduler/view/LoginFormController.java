package miller.scheduler.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import miller.scheduler.Main;
import miller.scheduler.domain.User;
import miller.scheduler.service.UserService;
import miller.scheduler.service.exception.InvalidUsernameOrPasswordException;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginFormController extends AnchorPane implements Initializable {

    @FXML
    private TextField txtFldUsername;

    @FXML
    private TextField txtFldPassword;

    private ResourceBundle bundle;
    private Main main;
    private UserService userService;

    public void setMain(Main main) {
        this.main = main;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @FXML
    private void handleLoginButtonEvent() {
        try {
            User user = userService.authenticate(txtFldUsername.getText(), txtFldPassword.getText());

            main.setUser(user);

            main.initRootLayout();
            main.gotoCalendarScreen();
        } catch (InvalidUsernameOrPasswordException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(bundle.getString("login.error.title"));
            alert.setHeaderText(bundle.getString("login.error.header"));
            alert.setContentText(bundle.getString("login.error.msg"));
            alert.showAndWait();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        bundle = resources;
    }

}
