/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miller.scheduler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import miller.scheduler.domain.User;
import miller.scheduler.repository.*;
import miller.scheduler.service.*;
import miller.scheduler.service.impl.*;
import miller.scheduler.view.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author m108491
 */
public class Main extends Application {
    private static Logger logger = Logger.getLogger(Main.class.getName());

    private Stage primaryStage;
    private BorderPane rootLayout;

    private User user;
    private AppointmentService appointmentService;
    private AddressService addressService;
    private CityService cityService;
    private CountryService countryService;
    private CustomerService customerService;
    private UserService userService;

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        DatabaseConnection db = BasicConnectionPool.createStoredConnectio();
        AddressRepository addressRepository = new AddressRepository(db);
        AppointmentRepository appointmentRepository = new AppointmentRepository(db);
        CityRepository cityRepository = new CityRepository(db);
        CountryRepository countryRepository = new CountryRepository(db);
        CustomerRepository customerRepository = new CustomerRepository(db);
        UserRepository userRepository = new UserRepository(db);

        addressService = new AddressServiceImpl(cityRepository, countryRepository, addressRepository);
        appointmentService = new AppointmentServiceImpl(appointmentRepository, addressRepository, cityRepository, countryRepository, customerRepository, userRepository);
        cityService = new CityServiceImpl(cityRepository, countryRepository);
        countryService = new CountryServiceImpl(countryRepository);
        customerService = new CustomerServiceImpl(addressRepository, cityRepository, countryRepository, customerRepository);
        userService = new UserServiceImpl(userRepository);

        this.user = userService.findOne(1).orElseThrow(RuntimeException::new);

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Scheduler");

        gotoLoginScreen();
//        initRootLayout();
//        gotoCalendarScreen();
    }


    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("miller/scheduler/view/RootLayout.fxml"));

            rootLayout = (BorderPane) loader.load();

            RootLayoutController controller = loader.getController();
            controller.setMain(this);
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gotoLoginScreen() {
        try {
            //  -Duser.language=fr -Duser.country=FR
            ResourceBundle rb = ResourceBundle.getBundle("miller/scheduler/messages");

            String fxml = "miller/scheduler/view/LoginForm.fxml";
            FXMLLoader loader = new FXMLLoader();
            InputStream in = getClass().getClassLoader().getResourceAsStream(fxml);
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            loader.setLocation(getClass().getClassLoader().getResource(fxml));
            loader.setResources(rb);

            AnchorPane page;
            try {
                page = (AnchorPane) loader.load(in);
            } finally {
                in.close();
            }
            // Store the stage width and height in case the user has resized the window
            double stageWidth = primaryStage.getWidth();
            if (!Double.isNaN(stageWidth)) {
                stageWidth -= (primaryStage.getWidth() - primaryStage.getScene().getWidth());
            }

            double stageHeight = primaryStage.getHeight();
            if (!Double.isNaN(stageHeight)) {
                stageHeight -= (primaryStage.getHeight() - primaryStage.getScene().getHeight());
            }

            Scene scene = new Scene(page);
            if (!Double.isNaN(stageWidth)) {
                page.setPrefWidth(stageWidth);
            }
            if (!Double.isNaN(stageHeight)) {
                page.setPrefHeight(stageHeight);
            }

            primaryStage.setScene(scene);
            primaryStage.sizeToScene();

            LoginFormController controller = loader.getController();
            controller.setUserService(userService);
            controller.setMain(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gotoCalendarScreen() {
        try {
            AppointmentController controller = (AppointmentController) loadAnchorPane("miller/scheduler/view/Appointment.fxml");
            controller.setAppointmentService(appointmentService);
            controller.setCustomerService(customerService);
            controller.setUser(user);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gotoCityScreen() {
        try {
            CityController controller = (CityController) loadAnchorPane("miller/scheduler/view/City.fxml");

            controller.setCityService(cityService);
            controller.setCountryService(countryService);
            controller.setUser(user);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gotoCountryScreen() {
        try {
            CountryController controller = (CountryController) loadAnchorPane("miller/scheduler/view/Country.fxml");
            controller.setCountryService(countryService);
            controller.setUser(user);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void gotoCustomerScreen() {
        try {
            CustomerController controller = (CustomerController) loadAnchorPane("miller/scheduler/view/Customer.fxml");
            controller.setAddressService(addressService);
            controller.setCityService(cityService);
            controller.setCustomerService(customerService);
            controller.setUser(user);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void gotoUserScreen() {
        try {
            UserController controller = (UserController) loadAnchorPane("miller/scheduler/view/User.fxml");
            controller.setUser(user);
            controller.setUserService(userService);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gotoReportScreen(String reportType) {
        try {
            ReportController controller = (ReportController) loadAnchorPane("miller/scheduler/view/Report.fxml");
            controller.setUser(user);
            controller.setReportType(reportType);
            controller.setAppointmentService(appointmentService);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void gotoReportApptTypesScreen(){
        gotoReportScreen("appointmentType");
    }

    public void gotoReportConsultantScreen() {
        gotoReportScreen("consultant");
    }

    public void gotoReportCustomerScreen() {
        gotoReportScreen("customer");
    }


    private AnchorPane loadAnchorPane(String fxml) throws IOException {
        // Load person overview.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource(fxml));
        AnchorPane anchorPane = (AnchorPane) loader.load();

        // Set person overview into the center of root layout.
        rootLayout.setCenter(anchorPane);

        return loader.getController();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
