package edu.eci.ieti.lab.service.user;

import edu.eci.ieti.lab.model.user.User;
import edu.eci.ieti.lab.model.user.UserDto;
import edu.eci.ieti.lab.repository.user.UserMongoRepository;
import edu.eci.ieti.lab.security.encrypt.PasswordEncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersServiceMongoDb implements UsersService {

    private final UserMongoRepository userMongoRepository;
    private final PasswordEncryptionService passwordEncryptionService;

    @Autowired
    public UsersServiceMongoDb(UserMongoRepository userMongoRepository, PasswordEncryptionService passwordEncryptionService) {
        this.userMongoRepository = userMongoRepository;
        this.passwordEncryptionService = passwordEncryptionService;
    }

    @Override
    public User save(UserDto user) {
        return userMongoRepository.save(new User(user,passwordEncryptionService.encrypt(user.getPassword())));
    }

    @Override
    public Optional<User> findById(String id) {
        Optional<User> user =userMongoRepository.findById(id);
        if (user.isPresent()){

            return user;
        }else {
            return Optional.empty();
        }
    }

    @Override
    public List<User> all() {
        return userMongoRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        userMongoRepository.deleteById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public User update(UserDto userDto, String userId) {
        Optional<User> userFound =userMongoRepository.findById(userId);
        if(userFound.isPresent()){
            User user = userFound.get();
            user.update(userDto);
            userMongoRepository.save(user);
            return user;
        }else {
            return null;
        }
    }
}
