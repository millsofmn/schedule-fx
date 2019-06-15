package miller.scheduler.service;

import miller.scheduler.domain.User;

public interface UserService extends Service<User, Integer> {

    User authenticate(String username, String password);
}
