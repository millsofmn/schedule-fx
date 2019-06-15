package miller.scheduler.domain;

import java.util.StringJoiner;

public class Address extends AbstractEntity {

    private String address;
    private String address2;
    private int cityId;
    private String postalCode;
    private String phone;

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

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
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

    @Override
    public String toString() {
        return new StringJoiner(", ", Address.class.getSimpleName() + "[", "]")
                .add("addressId=" + getId())
                .add("address='" + address + "'")
                .add("address2='" + address2 + "'")
                .add("cityId=" + cityId)
                .add("postalCode='" + postalCode + "'")
                .add("phone='" + phone + "'")
                .add(super.toString())
                .toString();
    }

}
