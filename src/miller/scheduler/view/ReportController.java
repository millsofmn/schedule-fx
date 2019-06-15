package miller.scheduler.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import miller.scheduler.domain.User;
import miller.scheduler.service.AppointmentService;
import miller.scheduler.service.ReportItem;
import miller.scheduler.service.dto.AppointmentDto;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * FXML Controller class
 *
 * @author m108491
 */
public class ReportController extends AnchorPane implements Initializable {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-M-d h:mm a");

    @FXML
    private Label reportHeader;
    @FXML
    private TextArea textArea;

    private String reportType;
    private User user;
    private AppointmentService appointmentService;

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAppointmentService(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;

        if (reportType.equals("appointmentType")) {
            createAppointmentTypeReport();
        } else if (reportType.equals("consultant")) {
            createConsultantReport();
        } else {
            createReportByCustomer();
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    private void createAppointmentTypeReport() {

        List<AppointmentDto> appointments = appointmentService.findAllAsDto();

        Map<String, List<AppointmentDto>> map = new HashMap<>();

        List<ReportItem> reportItems = new ArrayList<>();

        for (AppointmentDto appointment : appointments) {
            boolean found = false;
            for (ReportItem items : reportItems) {
                if (items.isDateIncluded(appointment.getStart())) {
                    items.add(appointment);
                    found = true;
                }
            }
            if (!found) {
                reportItems.add(new ReportItem(appointment));
            }
        }

        reportItems.sort(ReportItem.compareByDate());

        StringBuilder sb = new StringBuilder();
        for (ReportItem item : reportItems) {
            sb.append(item.toString()).append("\r\n");
        }
        reportHeader.setText("Appointment By Type");
        textArea.setText(sb.toString());
    }

    private void createConsultantReport() {
        Map<String, List<AppointmentDto>> map = new HashMap<>();

        List<AppointmentDto> appointmentDtos = appointmentService.findAllAsDto();

        for (AppointmentDto a : appointmentDtos) {
            String key = a.getUser();
            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<>());
            }
            map.get(key).add(a);
        }

        StringBuilder sb = new StringBuilder();
        for (String key : map.keySet()) {
            sb.append(key).append("\r\n");
            sb.append("------------------------------\r\n");
            List<AppointmentDto> apps = map.get(key);
            apps.sort(AppointmentDto.compareByStartTime());

            for (AppointmentDto dto : apps) {
                LocalDateTime startTime = LocalDateTime.ofInstant(dto.getStart(), ZoneId.systemDefault());
                LocalDateTime endTime = LocalDateTime.ofInstant(dto.getEnd(), ZoneId.systemDefault());

                sb.append(startTime.format(DATE_TIME_FORMATTER)).append("\t")
                        .append(endTime.format(DATE_TIME_FORMATTER)).append("\t")
                        .append(dto.getDescription()).append("\t").append(" with ")
                        .append(dto.getCustomer().getCustomerName()).append("\r\n");
            }
            sb.append("\r\n");
        }

        reportHeader.setText("Consultant Schedule");
        textArea.setText(sb.toString());
    }

    private void createReportByCustomer() {
        List<AppointmentDto> appointmentDtos = appointmentService.findAllAsDto();
        Map<String, List<AppointmentDto>> appointmentsByCustomer = new HashMap<>();

        for (AppointmentDto a : appointmentDtos) {
            String key = a.getCustomer().getCustomerName();
            if (!appointmentsByCustomer.containsKey(key)) {
                appointmentsByCustomer.put(key, new ArrayList<>());
            }
            appointmentsByCustomer.get(key).add(a);
        }

        StringBuilder sb = new StringBuilder();
        for (String key : appointmentsByCustomer.keySet()) {
            sb.append(key).append("\r\n");
            sb.append("------------------------------\r\n");
            List<AppointmentDto> apps = appointmentsByCustomer.get(key);
            apps.sort(AppointmentDto.compareByStartTime());

            for (AppointmentDto dto : apps) {
                LocalDateTime startTime = LocalDateTime.ofInstant(dto.getStart(), ZoneId.systemDefault());
                LocalDateTime endTime = LocalDateTime.ofInstant(dto.getEnd(), ZoneId.systemDefault());

                sb.append(startTime.format(DATE_TIME_FORMATTER)).append("\t")
                        .append(endTime.format(DATE_TIME_FORMATTER)).append("\t")
                        .append(dto.getDescription()).append("\t")
                        .append(dto.getCustomer().getCustomerName()).append("\r\n");
            }
            sb.append("\r\n");


            reportHeader.setText("Appointments By Customer");
            textArea.setText(sb.toString());
        }
    }

}
