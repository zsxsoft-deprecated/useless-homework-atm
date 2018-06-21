package com.zsxsoft.homework.atm.backend.controller;

import com.zsxsoft.homework.atm.backend.exception.ResourceNotFoundException;
import com.zsxsoft.homework.atm.backend.exception.TransactionException;
import com.zsxsoft.homework.atm.backend.model.RoleName;
import com.zsxsoft.homework.atm.backend.model.User;
import com.zsxsoft.homework.atm.backend.repository.UserRepository;
import com.zsxsoft.homework.atm.backend.security.UserPrincipal;
import com.zsxsoft.homework.atm.backend.service.ATMService;
import com.zsxsoft.homework.atm.backend.security.CurrentUser;
import com.zsxsoft.homework.atm.backend.payload.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserRepository userRepository;

    private final ATMService atmService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserRepository userRepository, ATMService atmService) {
        this.userRepository = userRepository;
        this.atmService = atmService;
    }

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserProfile getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        return new UserProfile(currentUser.getUser(), true);
    }

    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/users/username/{username}")
    public UserProfile getUserProfile(@CurrentUser UserPrincipal currentUser, @PathVariable(value = "username") String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        if (user.hasRole(RoleName.ROLE_SYSTEM)) throw new ResourceNotFoundException("User", "username", username);
        return new UserProfile(user, currentUser != null && (currentUser.getUser().hasRole(RoleName.ROLE_ADMIN) || currentUser.getId().equals(user.getId())));
    }

    @PostMapping("/user/password")
    @PreAuthorize("hasRole('USER')")
    public UserProfile updatePassword(@CurrentUser UserPrincipal currentUser, @RequestBody UpdatePasswordRequest passwordRequest) {
        return new UserProfile(atmService.checkAndUpdatePassword(currentUser.getUser(), passwordRequest.getOldPassword(), passwordRequest.getNewPassword()));
    }

}
