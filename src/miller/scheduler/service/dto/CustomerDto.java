package miller.scheduler.service.dto;

import miller.scheduler.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class CustomerDto implements Validator {

    private Integer id;
    private String customerName;
    private AddressDto address;
    private boolean active = true;

    public CustomerDto() {
    }

    public CustomerDto(Customer customer, Address address, City city, Country country) {
        this.id = customer.getId();
        this.customerName = customer.getCustomerName();
        this.active = customer.isActive();
        this.address = new AddressDto(address, city, country);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CustomerDto.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("customerName='" + customerName + "'")
                .add("address=" + address)
                .add("active=" + active)
                .toString();
    }

    @Override
    public boolean isValid() {
        return violations().isEmpty();
    }

    @Override
    public List<String> violations() {
        List<String> violations = new ArrayList<>();

        if(ValidationUtils.isNullOrEmpty(customerName)){
            violations.add("Customer must have a name.");
        }
        if(address == null){
            violations.add("Customer must have an address.");
        }

        return violations;
    }
}
