package com.youtubetutorial.ecommerce_backend.service;

import com.youtubetutorial.ecommerce_backend.api.model.LoginBody;
import com.youtubetutorial.ecommerce_backend.api.model.RegistrationBody;
import com.youtubetutorial.ecommerce_backend.exception.UserAlreadyExistsException;
import com.youtubetutorial.ecommerce_backend.model.LocalUser;
import com.youtubetutorial.ecommerce_backend.model.dao.LocalUserDAO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private LocalUserDAO localUserDAO;
    private EncryptionService encryptionService;
    private JWTService jwtService;

    public UserService(LocalUserDAO localUserDAO, EncryptionService encryptionService, JWTService jwtService) {
        this.localUserDAO = localUserDAO;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
    }

    public LocalUser registerUser(RegistrationBody registrationBody) throws UserAlreadyExistsException {
        if (localUserDAO.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent() ||
        localUserDAO.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent() )
        {
            throw new UserAlreadyExistsException();
        }
        LocalUser localUser = new LocalUser();
        localUser.setUsername(registrationBody.getUsername());
        localUser.setFirstName(registrationBody.getFirstName());
        localUser.setLastName(registrationBody.getLastName());
        localUser.setEmail(registrationBody.getEmail());
        localUser.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));
        return localUserDAO.save(localUser);
    }

    public String loginUser(LoginBody loginBody) {
        Optional<LocalUser> optUser = localUserDAO.findByUsernameIgnoreCase(loginBody.getUsername());
        if (optUser.isPresent()) {
            System.out.println("User present");
            LocalUser localUser = optUser.get();
            if (encryptionService.verifyPassword(loginBody.getPassword(), localUser.getPassword())) {
                System.out.println("Password verified");
                return jwtService.generateJWT(localUser);
            }
        }
        return null;
    }
}
