package miller.scheduler.domain;

import java.time.Instant;
import java.util.StringJoiner;

public class AbstractEntity {
    private Integer id;
    private Instant createDate;
    private String createdBy;
    private Instant lastUpdate;
    private String lastUpdatedBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AbstractEntity.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("createDate=" + createDate)
                .add("createdBy='" + createdBy + "'")
                .add("lastUpdate=" + lastUpdate)
                .add("lastUpdatedBy='" + lastUpdatedBy + "'")
                .toString();
    }
}
