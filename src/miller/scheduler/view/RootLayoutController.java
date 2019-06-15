package miller.scheduler.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import miller.scheduler.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class RootLayoutController extends AnchorPane implements Initializable {

    private Main main;

    public void setMain(Main main) {
        this.main = main;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    public void handleCustomerButton() {
        main.gotoCustomerScreen();
    }

    @FXML
    public void handleCountryButton() {
        main.gotoCountryScreen();
    }

    @FXML
    public void handleCityButton() {
        main.gotoCityScreen();
    }

    @FXML
    public void handleAppointmentButton(){
        main.gotoCalendarScreen();
    }

    @FXML
    public void handleUserButton(){
        main.gotoUserScreen();
    }

    @FXML
    public void handleReportApptTypeButton(){
        main.gotoReportApptTypesScreen();
    }

    @FXML
    public void handleReportConsultantButton(){
        main.gotoReportConsultantScreen();
    }

    @FXML
    public void handleReportCustomerButton(){
        main.gotoReportCustomerScreen();
    }
}

