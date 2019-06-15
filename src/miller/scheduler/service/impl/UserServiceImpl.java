package miller.scheduler.service.impl;

import miller.scheduler.domain.User;
import miller.scheduler.repository.UserRepository;
import miller.scheduler.service.UserService;
import miller.scheduler.service.exception.InvalidUsernameOrPasswordException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    enum LoginAttempt {SUCCESSFUL, FAILED}

    private static String AUDIT_FILE = "LoginAudit.txt";

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User entity, String username) {
        return repository.saveOrUpdate(entity, username);
    }

    @Override
    public boolean delete(Integer id) {
        return repository.delete(id);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<User> findOne(Integer id) {
        return repository.findById(id);
    }

    @Override
    public User authenticate(String username, String password) {
        boolean valid = true;
        User user = null;
        Optional<User> optional = repository.findOneByName(username);

        if(optional.isPresent()){

            user = optional.get();

            if(!user.getPassword().equals(password)){
                valid = false;
            }
        } else {
            valid = false;
        }

        if (!valid) {
            userLoginAudit(LoginAttempt.FAILED, username);
            throw new InvalidUsernameOrPasswordException();
        }

        userLoginAudit(LoginAttempt.SUCCESSFUL, username);
        return user;
    }


    private void userLoginAudit(LoginAttempt loginAttempt, String username){
        String contentToAppend = LocalDateTime.now() + " " + loginAttempt.name() + " login attempt by " + username + "\n";
        try {
            Path path = Paths.get(AUDIT_FILE);

            if (Files.notExists(path)) {
                Files.createFile(path);
            }

            Files.write(
                    Paths.get(AUDIT_FILE),
                    contentToAppend.getBytes(),
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
