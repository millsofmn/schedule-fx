package miller.scheduler.service.dto;

import miller.scheduler.domain.City;
import miller.scheduler.domain.Country;
import miller.scheduler.domain.ValidationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class CityDto implements Validator {

    private Integer id;
    private String city;
    private CountryDto country;

    public CityDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public CountryDto getCountry() {
        return country;
    }

    public void setCountry(CountryDto country) {
        this.country = country;
    }

    public CityDto(City city, Country country){
        this.id = city.getId();
        this.city = city.getCity();
        this.country = new CountryDto(country);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CityDto.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("city='" + city + "'")
                .add("country=" + country)
                .toString();
    }

    @Override
    public boolean isValid() {
        return violations().isEmpty();
    }

    @Override
    public List<String> violations() {
        List<String> violations = new ArrayList<>();

        if(ValidationUtils.isNullOrEmpty(city)){
            violations.add("City must not be empty.");
        }

        if(country == null){
            violations.add("Country must be entered.");
        }

        return violations;
    }
}
