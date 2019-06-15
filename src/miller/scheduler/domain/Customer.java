package miller.scheduler.domain;

import java.util.StringJoiner;

public class Customer extends AbstractEntity {

    private String customerName;
    private int addressId;
    private boolean active;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Customer.class.getSimpleName() + "[", "]")
                .add("customerId='" + getId() + "'")
                .add("customerName='" + customerName + "'")
                .add("addressId=" + addressId)
                .add("active=" + active)
                .add(super.toString())
                .toString();
    }

}
