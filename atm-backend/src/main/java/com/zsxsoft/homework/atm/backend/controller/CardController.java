package com.zsxsoft.homework.atm.backend.controller;

import com.zsxsoft.homework.atm.backend.exception.ResourceNotFoundException;
import com.zsxsoft.homework.atm.backend.model.Card;
import com.zsxsoft.homework.atm.backend.model.CardStatus;
import com.zsxsoft.homework.atm.backend.model.RoleName;
import com.zsxsoft.homework.atm.backend.model.User;
import com.zsxsoft.homework.atm.backend.payload.CardProfile;
import com.zsxsoft.homework.atm.backend.payload.UserProfile;
import com.zsxsoft.homework.atm.backend.repository.CardRepository;
import com.zsxsoft.homework.atm.backend.repository.UserRepository;
import com.zsxsoft.homework.atm.backend.security.CurrentUser;
import com.zsxsoft.homework.atm.backend.security.UserPrincipal;
import com.zsxsoft.homework.atm.backend.service.ATMService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CardController {

    private final UserRepository userRepository;
    private final CardRepository cardRepository;

    private final ATMService atmService;

    private static final Logger logger = LoggerFactory.getLogger(CardController.class);

    @Autowired
    public CardController(UserRepository userRepository, CardRepository cardRepository, ATMService atmService) {
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
        this.atmService = atmService;
    }

    @GetMapping("/cards/card/{cardNumber}")
    @PreAuthorize("hasRole('USER')")
    public CardProfile getCardProfile(@CurrentUser UserPrincipal currentUser, @PathVariable(value = "cardNumber") String cardNumber) {
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "cardNumber", cardNumber));
        if (!card.getStatus().equals(CardStatus.BANKCARDSTATUS_ENABLED)) throw new ResourceNotFoundException("Card", "cardNumber", cardNumber);
        return new CardProfile(card,currentUser != null && (currentUser.getUser().hasRole(RoleName.ROLE_ADMIN) || currentUser.getId().equals(card.getUser().getId())), true);
    }

    @PostMapping("/cards/new")
    @PreAuthorize("hasRole('USER')")
    public CardProfile createNewCard(@CurrentUser UserPrincipal currentUser) {
        return new CardProfile(atmService.createNewCard(currentUser.getUser()));
    }
}
