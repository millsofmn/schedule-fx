package miller.scheduler.service.dto;

import miller.scheduler.domain.Address;
import miller.scheduler.domain.City;
import miller.scheduler.domain.Country;
import miller.scheduler.domain.ValidationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class AddressDto implements Validator {

    private Integer id;
    private String address;
    private String address2;
    private String postalCode;
    private String phone;
    private CityDto city;

    public AddressDto() {
    }

    public AddressDto(Address address, City city, Country country){
        this.id = address.getId();
        this.address = address.getAddress();
        this.address2 = address.getAddress2();
        this.postalCode = address.getPostalCode();
        this.phone = address.getPhone();
        this.city = new CityDto(city, country);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public CityDto getCity() {
        return city;
    }

    public void setCity(CityDto city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AddressDto.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("address='" + address + "'")
                .add("address2='" + address2 + "'")
                .add("postalCode='" + postalCode + "'")
                .add("phone='" + phone + "'")
                .add("city=" + city)
                .toString();
    }

    @Override
    public boolean isValid() {
        return violations().isEmpty();
    }

    @Override
    public List<String> violations() {
        List<String> violations = new ArrayList<>();

        if(ValidationUtils.isNullOrEmpty(address)){
            violations.add("Address must have an address.");
        }

        if(ValidationUtils.isNullOrEmpty(phone)){
            violations.add("Address must have a phone.");
        }

        if(city == null){
            violations.add("Address must have a city.");
        }
        return violations;
    }
}
