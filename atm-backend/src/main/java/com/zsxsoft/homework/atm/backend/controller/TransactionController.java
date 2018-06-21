package com.zsxsoft.homework.atm.backend.controller;

import com.zsxsoft.homework.atm.backend.exception.ResourceNotFoundException;
import com.zsxsoft.homework.atm.backend.exception.TransactionException;
import com.zsxsoft.homework.atm.backend.model.Card;
import com.zsxsoft.homework.atm.backend.model.Transaction;
import com.zsxsoft.homework.atm.backend.model.User;
import com.zsxsoft.homework.atm.backend.repository.CardRepository;
import com.zsxsoft.homework.atm.backend.repository.UserRepository;
import com.zsxsoft.homework.atm.backend.security.CurrentUser;
import com.zsxsoft.homework.atm.backend.security.UserPrincipal;
import com.zsxsoft.homework.atm.backend.service.ATMService;
import com.zsxsoft.homework.atm.backend.util.AppConstants;
import com.zsxsoft.homework.atm.backend.payload.*;
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
@RequestMapping("/api/transactions")
public class TransactionController {

    private final UserRepository userRepository;
    private final CardRepository cardRepository;

    private final ATMService atmService;

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    public TransactionController(ATMService atmService, UserRepository userRepository, CardRepository cardRepository) {
        this.atmService = atmService;
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public PagedResponse<TransactionResponse> getTransactions(@CurrentUser UserPrincipal currentUser,
                                                              @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                              @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return atmService.getTransactionsByUser(currentUser.getUser(), page, size);
    }

    @GetMapping("/card/{cardNumber}")
    @PreAuthorize("hasRole('USER')")
    public PagedResponse<TransactionResponse> getCardTransactions(@CurrentUser UserPrincipal currentUser,
                                                              @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                              @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
                                                              @PathVariable(value = "cardNumber") String cardNumber) {
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "card", cardNumber));

        atmService.checkCardIsBelongToUserOrThrow(card, currentUser.getUser());
        return atmService.getTransactionsByCard(card, page, size);
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public PagedResponse<TransactionResponse> getUserTransactions(@CurrentUser UserPrincipal currentUser,
                                                                  @PathVariable(value = "username") String username,
                                                                  @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                                  @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return atmService.getTransactionsByUser(user, page, size);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createTransaction(Authentication authentication, @Valid @RequestBody TransactionRequest transactionRequest) {
        User user = ((UserPrincipal) authentication.getPrincipal()).getUser();
        Transaction t;
        try {
            Card fromCard = cardRepository.findByCardNumber(transactionRequest.getFromCardNumber())
                    .orElseThrow(() -> new ResourceNotFoundException("Card", "card", transactionRequest.getFromCardNumber()));
            Card toCard = cardRepository.findByCardNumber(transactionRequest.getToCardNumber())
                    .orElseThrow(() -> new ResourceNotFoundException("Card", "card", transactionRequest.getToCardNumber()));
            atmService.checkCardIsBelongToUserOrThrow(fromCard, user);
            t = atmService.createTransaction(fromCard, toCard, transactionRequest);
        } catch (TransactionException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{transactionId}")
                .buildAndExpand(t.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Transaction Created Successfully"));
    }


}
