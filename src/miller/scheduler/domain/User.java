package miller.scheduler.domain;

import miller.scheduler.service.dto.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class User extends AbstractEntity implements Validator {

    private String userName;
    private String password;
    private boolean active;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("userId=" + getId())
                .add("userName='" + userName + "'")
                .add("password='" + password + "'")
                .add("active=" + active)
                .add(super.toString())
                .toString();
    }

    @Override
    public boolean isValid() {
        return violations().isEmpty();
    }

    @Override
    public List<String> violations() {
        List<String> violations = new ArrayList<>();

        if(ValidationUtils.isNullOrEmpty(userName)){
            violations.add("Username cannot be empty");
        }

        if(ValidationUtils.isNullOrEmpty(password)){
            violations.add("Password cannot be empty");
        }
        return violations;
    }
}
