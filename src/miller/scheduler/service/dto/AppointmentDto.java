package miller.scheduler.service.dto;

import miller.scheduler.domain.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;

public class AppointmentDto implements Validator {

    private Integer id;
    private CustomerDto customer;
    private String user;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String url;
    private Instant start;
    private Instant end;

    public AppointmentDto() {
    }

    public AppointmentDto(Appointment appointment, Customer customer, Address address, City city, Country country) {
        this.id = appointment.getId();
        this.title = appointment.getTitle();
        this.description = appointment.getDescription();
        this.location = appointment.getLocation();
        this.contact = appointment.getContact();
        this.url = appointment.getUrl();
        this.start = appointment.getStart();
        this.end = appointment.getEnd();
        this.user = appointment.getCreatedBy();
        this.customer = new CustomerDto(customer, address, city, country);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CustomerDto getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDto customer) {
        this.customer = customer;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Instant getStart() {
        return start;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    public Instant getEnd() {
        return end;
    }

    public void setEnd(Instant end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AppointmentDto.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("customer=" + customer)
                .add("user=" + user)
                .add("title='" + title + "'")
                .add("description='" + description + "'")
                .add("location='" + location + "'")
                .add("contact='" + contact + "'")
                .add("url='" + url + "'")
                .add("start=" + start)
                .add("end=" + end)
                .add("user=" + user)
                .toString();
    }

    @Override
    public boolean isValid() {
        return violations().isEmpty();
    }

    @Override
    public List<String> violations() {
        List<String> violations = new ArrayList<>();

        if(customer == null){
            violations.add("Appointment must be for a customer.");
        }

        if(ValidationUtils.isNullOrEmpty(description)){
            violations.add("Appointment must contain a type.");
        }

        if(start == null || end == null){
            violations.add("Appointment must have a starting time and ending time.");

        } else if(start.isAfter(end)){
            violations.add("Appointment start time must be before end time.");
        }

        return violations;
    }

    public static Comparator<AppointmentDto> compareByStartTime() {
        return Comparator.nullsLast(Comparator.comparing(AppointmentDto::getStart));
    }
}
