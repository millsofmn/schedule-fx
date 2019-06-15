package miller.scheduler.domain;

import java.util.StringJoiner;

public class Country extends AbstractEntity {

    private String country;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Country.class.getSimpleName() + "[", "]")
                .add("country='" + country + "'")
                .add("createDate=" + getCreateDate())
                .add("createdBy='" + getCreatedBy() + "'")
                .add("lastUpdate=" + getLastUpdate())
                .add("lastUpdatedBy='" + getLastUpdatedBy() + "'")
                .toString();
    }
}
