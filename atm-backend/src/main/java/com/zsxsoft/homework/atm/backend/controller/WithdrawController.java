package com.zsxsoft.homework.atm.backend.controller;


import com.zsxsoft.homework.atm.backend.exception.ResourceNotFoundException;
import com.zsxsoft.homework.atm.backend.exception.TransactionException;
import com.zsxsoft.homework.atm.backend.model.Card;
import com.zsxsoft.homework.atm.backend.model.Transaction;
import com.zsxsoft.homework.atm.backend.model.User;
import com.zsxsoft.homework.atm.backend.payload.ApiResponse;
import com.zsxsoft.homework.atm.backend.payload.TransactionRequest;
import com.zsxsoft.homework.atm.backend.repository.CardRepository;
import com.zsxsoft.homework.atm.backend.security.UserPrincipal;
import com.zsxsoft.homework.atm.backend.service.ATMService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/withdraw")
public class WithdrawController {

    private final ATMService atmService;
    private final CardRepository cardRepository;

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    public WithdrawController(ATMService atmService, CardRepository cardRepository) {
        this.atmService = atmService;
        this.cardRepository = cardRepository;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> withdraw(Authentication authentication, @Valid @RequestBody TransactionRequest transactionRequest) {
        User user = ((UserPrincipal) authentication.getPrincipal()).getUser();
        Transaction t;
        try {
            Card fromCard = cardRepository.findByCardNumber(transactionRequest.getFromCardNumber())
                    .orElseThrow(() -> new ResourceNotFoundException("Card", "card", transactionRequest.getFromCardNumber()));
            atmService.checkCardIsBelongToUserOrThrow(fromCard, user);
            t = atmService.withdraw(fromCard, transactionRequest);
        } catch (TransactionException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{transactionId}")
                .buildAndExpand(t.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Withdraw Successfully"));
    }


}
