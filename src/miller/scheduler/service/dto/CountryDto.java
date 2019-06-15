package miller.scheduler.service.dto;

import miller.scheduler.domain.Country;
import miller.scheduler.domain.ValidationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class CountryDto implements Validator {

    private Integer id;
    private String country;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public CountryDto() {
    }

    public CountryDto(Country country) {
        this.id = country.getId();
        this.country = country.getCountry();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CountryDto.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("country='" + country + "'")
                .toString();
    }

    @Override
    public boolean isValid() {
        return violations().isEmpty();
    }

    @Override
    public List<String> violations() {
        List<String> violations = new ArrayList<>();

        if(ValidationUtils.isNullOrEmpty(country)){
            violations.add("Country must not be empty.");
        }
        return violations;
    }
}
