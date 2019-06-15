package miller.scheduler.view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import miller.scheduler.domain.User;
import miller.scheduler.service.AppointmentService;
import miller.scheduler.service.CustomerService;
import miller.scheduler.service.dto.AppointmentDto;
import miller.scheduler.service.dto.CustomerDto;
import miller.scheduler.view.exception.OutsideOfBusinessHoursException;
import miller.scheduler.view.exception.OverlappingAppointmentTimesException;

import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class AppointmentController extends AnchorPane implements Initializable {
    private final Logger log = Logger.getLogger(AppointmentController.class.getName());

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy")
            .withZone(ZoneId.systemDefault());
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a")
            .withZone(ZoneId.systemDefault());
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-M-d h:mm a");

    /* Appointment Table */
    @FXML
    private TableView<AppointmentDto> tblAppointment;

    @FXML
    private TableColumn<AppointmentDto, Integer> colApptId;

    @FXML
    private TableColumn<AppointmentDto, String> colApptDate;

    @FXML
    private TableColumn<AppointmentDto, String> colApptStartTime;

    @FXML
    private TableColumn<AppointmentDto, String> colApptEndTime;

    @FXML
    private TableColumn<AppointmentDto, String> colApptCustomerName;

    @FXML
    private TableColumn<AppointmentDto, String> colApptDesc;

    @FXML
    private RadioButton rdoViewByMonth;

    @FXML
    private RadioButton rdoViewByWeek;

    @FXML
    private RadioButton rdoViewAll;

    /* Appointment Form */
    @FXML
    private ComboBox<CustomerDto> cmbCustomer;

    @FXML
    private TextField txtFldTitle;

    @FXML
    private ComboBox<String> cmbAppDesc;

    @FXML
    private TextField txtFldLocation;

    @FXML
    private TextField txtFldContact;

    @FXML
    private TextField txtFldUrl;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<String> cmbStartHour;

    @FXML
    private ComboBox<String> cmbStartMinute;

    @FXML
    private ComboBox<String> cmbStartPeriod;

    @FXML
    private ComboBox<String> cmbEndHour;

    @FXML
    private ComboBox<String> cmbEndMinute;

    @FXML
    private ComboBox<String> cmbEndPeriod;

    private ObservableList<AppointmentDto> appointments = FXCollections.observableArrayList();
    private ObservableList<AppointmentDto> filteredAppointments = FXCollections.observableArrayList();

    private Integer appointmentId;
    private CustomerDto selectedCustomerDto;

    private AppointmentService appointmentService;

    private User user;
    private List<String> hours = new ArrayList<>();
    private List<String> minutes = new ArrayList<>();
    private List<String> periods = new ArrayList<>();
    private List<String> appDesc = new ArrayList<>();

    public void setAppointmentService(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
        reloadTable();
    }

    public void setCustomerService(CustomerService customerService) {
        cmbCustomer.setItems(FXCollections.observableArrayList(customerService.findAllAsDto()));
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AppointmentController() {
        filteredAppointments.addAll(appointments);
        appointments.addListener((ListChangeListener<AppointmentDto>) c -> updateFilteredAppointments());
    }


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        colApptId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
        colApptDate.setCellValueFactory(data -> new SimpleStringProperty(DATE_FORMATTER.format(data.getValue().getStart())));
        colApptStartTime.setCellValueFactory(data -> new SimpleStringProperty(TIME_FORMATTER.format(data.getValue().getStart())));
        colApptEndTime.setCellValueFactory(data -> new SimpleStringProperty(TIME_FORMATTER.format(data.getValue().getEnd())));
        colApptCustomerName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCustomer().getCustomerName()));
        colApptDesc.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));


        SortedList<AppointmentDto> appointmentDtoSortedList = new SortedList<>(filteredAppointments, AppointmentDto.compareByStartTime());
        tblAppointment.setItems(appointmentDtoSortedList);


        tblAppointment.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                setForm(newSelection);
            }
        });

        Callback<ListView<CustomerDto>, ListCell<CustomerDto>> cellFactory = new Callback<ListView<CustomerDto>, ListCell<CustomerDto>>() {
            @Override
            public ListCell<CustomerDto> call(ListView<CustomerDto> l) {
                return new ListCell<CustomerDto>() {

                    @Override
                    protected void updateItem(CustomerDto item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            selectedCustomerDto = item;
                            setText(item.getCustomerName());
                        }
                    }
                };
            }
        };

        cmbCustomer.setButtonCell(cellFactory.call(null));
        cmbCustomer.setCellFactory(cellFactory);

        hours.addAll(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"));
        minutes.addAll(Arrays.asList("00", "15", "30", "45"));
        periods.addAll(Arrays.asList("AM", "PM"));

        cmbStartHour.setItems(FXCollections.observableArrayList(hours));
        cmbStartMinute.setItems(FXCollections.observableArrayList(minutes));
        cmbStartPeriod.setItems(FXCollections.observableArrayList(periods));

        cmbEndHour.setItems(FXCollections.observableArrayList(hours));
        cmbEndMinute.setItems(FXCollections.observableArrayList(minutes));
        cmbEndPeriod.setItems(FXCollections.observableArrayList(periods));

        appDesc.addAll(Arrays.asList("Initial Consult", "Fix Crazy", "Follow Up"));
        cmbAppDesc.setItems(FXCollections.observableArrayList(appDesc));
    }

    @FXML
    private void handleDeleteButtonEvent() {
        AppointmentDto appointment = tblAppointment.getSelectionModel().getSelectedItem();

        if (appointment != null) {
            log.info("Deleting appointment id = " + appointment.getId());
            boolean deleteResult = appointmentService.delete(appointment.getId());
            log.info("Result for deletion = " + deleteResult);
        }
        resetForm();
        reloadTable();
    }

    @FXML
    private void handleClearButtonEvent() {
        selectedCustomerDto = null;
        resetForm();
    }

    @FXML
    private void handleSaveButtonEvent() {
        log.info("Save started ...");

        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setId(appointmentId);
        appointmentDto.setCustomer(selectedCustomerDto);
        appointmentDto.setTitle(txtFldTitle.getText());
        appointmentDto.setDescription(cmbAppDesc.getValue());
        appointmentDto.setLocation(txtFldLocation.getText());
        appointmentDto.setContact(txtFldContact.getText());
        appointmentDto.setUrl(txtFldUrl.getText());

        LocalDate date = datePicker.getValue();

        if (!cmbStartHour.getSelectionModel().isEmpty() &&
                !cmbStartMinute.getSelectionModel().isEmpty() &&
                !cmbStartPeriod.getSelectionModel().isEmpty() &&
                !cmbStartHour.getSelectionModel().isEmpty()) {

            String timeDateString = date.getYear() + "-" +
                    date.getMonthValue() + "-" +
                    date.getDayOfMonth() + " " +
                    cmbStartHour.getValue() + ":" +
                    cmbStartMinute.getValue() + " " +
                    cmbStartPeriod.getValue();
            log.info("date time string : " + timeDateString);
            LocalDateTime startTime = LocalDateTime.parse(timeDateString, DATE_TIME_FORMATTER);

            appointmentDto.setStart(startTime.atZone(ZoneId.systemDefault()).toInstant());
        }

        if (!cmbEndHour.getSelectionModel().isEmpty() &&
                !cmbEndMinute.getSelectionModel().isEmpty() &&
                !cmbEndPeriod.getSelectionModel().isEmpty() &&
                !cmbEndHour.getSelectionModel().isEmpty()) {

            LocalDateTime endTime = LocalDateTime.parse(date.getYear() + "-" +
                    date.getMonthValue() + "-" +
                    date.getDayOfMonth() + " " +
                    cmbEndHour.getValue() + ":" +
                    cmbEndMinute.getValue() + " " +
                    cmbEndPeriod.getValue(), DATE_TIME_FORMATTER);
            appointmentDto.setEnd(endTime.atZone(ZoneId.systemDefault()).toInstant());
        }
        log.info("Appointment to save : " + appointmentDto);

        if (appointmentDto.isValid()) {

            try {
                insideBusinessHours(appointmentDto.getStart(), appointmentDto.getEnd());
                checkForOverlappingAppointments(appointmentDto);

            } catch (OutsideOfBusinessHoursException e) {
                Alert alert = ViewUtils.showExceptionErrors(
                        "Do you really want to schedule appointment to be outside of 8 - 5 business hours?");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.CANCEL) {
                    return;
                }
            } catch (OverlappingAppointmentTimesException e) {
                Alert alert = ViewUtils.showExceptionErrors(
                        "Appointment overlaps with another.");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.CANCEL) {
                    return;
                }
            }

            if (appointmentId == null) {
                log.info("Creating new appointment");
                appointmentService.create(appointmentDto, user.getUserName());
            } else {
                log.info("Updating appointment");
                appointmentService.update(appointmentDto, user.getUserName());
            }
            resetForm();
            reloadTable();
        } else {
            log.severe("Errors in validation of appointment");
            Alert alert = ViewUtils.showValidationErrors(appointmentDto.violations());
            alert.showAndWait();
        }

    }

    @FXML
    private void handleViewByRadioButton() {
        updateFilteredAppointments();
    }

    private void updateFilteredAppointments() {
        filteredAppointments.clear();

        for (AppointmentDto app : appointments) {
            if (matchesfilter(app)) {
                filteredAppointments.add(app);
            }
        }

    }

    private boolean isInThisTimePeriod(Instant date, LocalDateTime futureDate) {
        boolean inTimePeriod = true;

        LocalDateTime myDate = LocalDateTime.ofInstant(date, ZoneId.systemDefault());
        LocalDateTime today = LocalDateTime.now();

        if (myDate.isBefore(today) || myDate.isAfter(futureDate)) {
            inTimePeriod = false;
        }

        return inTimePeriod;
    }

    private boolean matchesfilter(AppointmentDto app) {
        boolean matched = true;

        if (rdoViewByMonth.isSelected()) {
            matched = isInThisTimePeriod(app.getStart(), LocalDateTime.now().plusMonths(1));

        } else if (rdoViewByWeek.isSelected()) {
            matched = isInThisTimePeriod(app.getStart(), LocalDateTime.now().plusWeeks(1));
        }

        return matched;
    }

    private void setForm(AppointmentDto appointmentDto) {
        log.info("Setting address to : " + appointmentDto);

        appointmentId = appointmentDto.getId();

        selectedCustomerDto = appointmentDto.getCustomer();

        cmbAppDesc.setValue(appointmentDto.getDescription());

        cmbCustomer.getItems()
                .stream()
                .filter(customer -> appointmentDto.getCustomer().getId() == customer.getId())
                .findAny()
                .ifPresent(cmbCustomer.getSelectionModel()::select);

        txtFldTitle.setText(appointmentDto.getTitle());
        txtFldLocation.setText(appointmentDto.getLocation());
        txtFldContact.setText(appointmentDto.getContact());
        txtFldUrl.setText(appointmentDto.getUrl());

        LocalDateTime startTimeDate = LocalDateTime.ofInstant(appointmentDto.getStart(), ZoneId.systemDefault());

        datePicker.setValue(startTimeDate.toLocalDate());

        int startHour = startTimeDate.getHour();
        cmbStartPeriod.setValue(getTimePeriod(startHour));
        cmbStartHour.setValue(getHour(startHour));

        int startMinute = startTimeDate.getMinute();
        cmbStartMinute.setValue(formatMinutes(startMinute));

        LocalDateTime endTimeDate = LocalDateTime.ofInstant(appointmentDto.getEnd(), ZoneId.systemDefault());
        int endHour = endTimeDate.getHour();
        cmbEndPeriod.setValue(getTimePeriod(endHour));
        cmbEndHour.setValue(getHour(endHour));

        int endMinute = endTimeDate.getMinute();
        cmbEndMinute.setValue(formatMinutes(endMinute));

    }

    private String getTimePeriod(int hour) {
        String period = "PM";
        if (hour < 12) {
            period = "AM";
        }
        return period;
    }

    private String getHour(int hour) {
        if (hour > 12) {
            hour = hour - 12;
        }
        return String.valueOf(hour);
    }

    private String formatMinutes(int min) {
        String minute = String.valueOf(min);
        if (min < 10) {
            minute = "0" + minute;
        }
        return minute;
    }

    private void resetForm() {

        selectedCustomerDto = null;

        datePicker.setValue(LocalDateTime.now().toLocalDate());

        cmbStartHour.getSelectionModel().clearSelection();
        cmbStartMinute.getSelectionModel().clearSelection();
        cmbStartPeriod.getSelectionModel().clearSelection();

        cmbEndHour.getSelectionModel().clearSelection();
        cmbEndMinute.getSelectionModel().clearSelection();
        cmbEndPeriod.getSelectionModel().clearSelection();

        cmbAppDesc.getSelectionModel().clearSelection();

        cmbCustomer.getSelectionModel().clearSelection();

        txtFldTitle.clear();
        txtFldLocation.clear();
        txtFldContact.clear();
        txtFldUrl.clear();

    }

    private void reloadTable() {
        appointments.clear();
        appointments.addAll(appointmentService.findAllAsDto());
        updateFilteredAppointments();
        checkForCurrentAppointments();
    }

    private void insideBusinessHours(Instant startTime, Instant endTime) throws OutsideOfBusinessHoursException {
        LocalDateTime appStartTime = LocalDateTime.ofInstant(startTime, ZoneId.systemDefault());
        LocalDateTime appEndTime = LocalDateTime.ofInstant(endTime, ZoneId.systemDefault());

        LocalDateTime startOfDay = LocalDate.from(appStartTime).atTime(8, 0);
        LocalDateTime endOfDay = LocalDate.from(appEndTime).atTime(17, 0);

        if(appStartTime.isBefore(startOfDay) || appEndTime.isAfter(endOfDay)){
            throw new OutsideOfBusinessHoursException();
        }
    }

    private void checkForOverlappingAppointments(AppointmentDto appointment) throws OverlappingAppointmentTimesException {
        List<AppointmentDto> appointments = appointmentService.findAllAsDto();

        LocalDateTime schedStartTime;
        LocalDateTime schedEndTime;

        LocalDateTime appStartTime = LocalDateTime.ofInstant(appointment.getStart(), ZoneId.systemDefault());
        LocalDateTime appEndTime = LocalDateTime.ofInstant(appointment.getEnd(), ZoneId.systemDefault());

        for(AppointmentDto sched : appointments){
            schedStartTime = LocalDateTime.ofInstant(sched.getStart(), ZoneId.systemDefault());

            if(appStartTime.getYear() == schedStartTime.getYear() &&
                    appStartTime.getDayOfYear() == schedStartTime.getDayOfYear() &&
                    !appointment.getId().equals(sched.getId())){

                schedEndTime = LocalDateTime.ofInstant(sched.getEnd(), ZoneId.systemDefault());

                if((appStartTime.isAfter(schedStartTime) && appStartTime.isBefore(schedEndTime)) ||
                        (appEndTime.isAfter(schedStartTime) && appEndTime.isBefore(schedEndTime)) ||
                        (schedStartTime.isAfter(appStartTime) && schedStartTime.isBefore(appEndTime)) ||
                        (schedEndTime.isAfter(appStartTime) && schedEndTime.isBefore(appEndTime)) ) {
                    throw new OverlappingAppointmentTimesException();
                }
            }


        }
    }

    private void checkForCurrentAppointments(){
        List<AppointmentDto> appointmentDtos = appointmentService.findAllAsDto();
        List<String> currentAppointments = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime then = LocalDateTime.now().plusMinutes(15);

        LocalDateTime apptTime;
        for(AppointmentDto appt : appointmentDtos){
            apptTime = LocalDateTime.ofInstant(appt.getStart(), ZoneId.systemDefault());

            if(apptTime.isAfter(now) && apptTime.isBefore(then)){
                long minutes = now.until(apptTime, ChronoUnit.MINUTES);
                currentAppointments.add("There is a current appointment in " + minutes + " minutes at " + apptTime.format(TIME_FORMATTER) + " with " + appt.getCustomer().getCustomerName());
            }
        }

        if(!currentAppointments.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Up Coming Appointments");
            alert.setHeaderText("Up Coming Appointments");
            alert.setContentText(currentAppointments.stream().collect(Collectors.joining("\n")));
            alert.showAndWait();
        }
    }
}
