package com.youtubetutorial.ecommerce_backend.api.controller.auth;

import com.youtubetutorial.ecommerce_backend.api.model.LoginBody;
import com.youtubetutorial.ecommerce_backend.api.model.LoginResponse;
import com.youtubetutorial.ecommerce_backend.api.model.RegistrationBody;
import com.youtubetutorial.ecommerce_backend.exception.UserAlreadyExistsException;
import com.youtubetutorial.ecommerce_backend.model.LocalUser;
import com.youtubetutorial.ecommerce_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<HttpStatus> registerUser(@Valid @RequestBody RegistrationBody registrationBody) {
        System.out.println("Received registration data: " + registrationBody.getLastName());
        System.out.println("Received registration data: " + registrationBody.getFirstName());
        try {
            userService.registerUser(registrationBody);
            return ResponseEntity.ok().build();
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginBody loginBody) {
        String jwt = userService.loginUser(loginBody);
        System.out.println(jwt);
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            LoginResponse response = new LoginResponse();
            response.setJwt(jwt);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    @GetMapping("/me")
    public LocalUser getLoggedInUserProfile(@AuthenticationPrincipal LocalUser user) {
        return user;
    }
}
