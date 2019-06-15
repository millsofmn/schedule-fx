package miller.scheduler.domain;

import java.util.StringJoiner;

public class City extends AbstractEntity {

    private int countryId;

    private String city;

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", City.class.getSimpleName() + "[", "]")
                .add("cityId=" + getId())
                .add("countryId=" + countryId)
                .add("city='" + city + "'")
                .add("createDate=" + getCreateDate())
                .add("createdBy='" + getCreatedBy() + "'")
                .add("lastUpdate=" + getLastUpdate())
                .add("lastUpdatedBy='" + getLastUpdatedBy() + "'")
                .toString();
    }

}
