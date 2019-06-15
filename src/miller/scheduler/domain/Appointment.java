package miller.scheduler.domain;

import java.time.Instant;
import java.util.StringJoiner;

public class Appointment extends AbstractEntity {

    private int customerId;
    private String userId;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String url;
    private Instant start;
    private Instant end;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
        return new StringJoiner(", ", Appointment.class.getSimpleName() + "[", "]")
                .add("appointmentId=" + getId())
                .add("customerId=" + customerId)
                .add("userId=" + userId)
                .add("title='" + title + "'")
                .add("description='" + description + "'")
                .add("location='" + location + "'")
                .add("contact='" + contact + "'")
                .add("url='" + url + "'")
                .add("start=" + start)
                .add("end=" + end)
                .add(super.toString())
                .toString();
    }
}
