package com.zsxsoft.homework.atm.backend.controller;

import com.zsxsoft.homework.atm.backend.exception.ResourceNotFoundException;
import com.zsxsoft.homework.atm.backend.exception.TransactionException;
import com.zsxsoft.homework.atm.backend.model.Transaction;
import com.zsxsoft.homework.atm.backend.model.User;
import com.zsxsoft.homework.atm.backend.model.UserStatus;
import com.zsxsoft.homework.atm.backend.payload.ApiResponse;
import com.zsxsoft.homework.atm.backend.payload.PagedResponse;
import com.zsxsoft.homework.atm.backend.payload.TransactionRequest;
import com.zsxsoft.homework.atm.backend.payload.TransactionResponse;
import com.zsxsoft.homework.atm.backend.repository.UserRepository;
import com.zsxsoft.homework.atm.backend.security.CurrentUser;
import com.zsxsoft.homework.atm.backend.security.UserPrincipal;
import com.zsxsoft.homework.atm.backend.service.ATMService;
import com.zsxsoft.homework.atm.backend.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;


@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository userRepository;

    private final ATMService atmService;

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    public AdminController(ATMService atmService, UserRepository userRepository) {
        this.atmService = atmService;
        this.userRepository = userRepository;
    }

    @PostMapping("/user/status/update/{id}")
    public ResponseEntity<?> updateUserStatus (@PathVariable(value = "id") String userId) {
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        if (user.getStatus().equals(UserStatus.USERSTATUS_ENABLED)) {
            user.setStatus(UserStatus.USERSTATUS_DISABLED);
        } else if (user.getStatus().equals(UserStatus.USERSTATUS_DISABLED)) {
            user.setStatus(UserStatus.USERSTATUS_ENABLED);
        }
        userRepository.save(user);
        return ResponseEntity.ok().body(new ApiResponse(true, "ok"));
    }

    @PostMapping("/user/password/reset/{id}")
    public ResponseEntity<?> resetUserPassword (@PathVariable(value = "id") String userId) {
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        atmService.resetPassword(user);
        return ResponseEntity.ok().body(new ApiResponse(true, "ok"));
    }


}
