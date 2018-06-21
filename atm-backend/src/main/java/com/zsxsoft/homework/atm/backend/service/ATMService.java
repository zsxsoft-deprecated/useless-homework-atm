package com.zsxsoft.homework.atm.backend.service;

import com.github.javafaker.Bool;
import com.github.javafaker.CreditCardType;
import com.github.javafaker.Faker;
import com.zsxsoft.homework.atm.backend.exception.BadRequestException;
import com.zsxsoft.homework.atm.backend.exception.TransactionException;
import com.zsxsoft.homework.atm.backend.model.*;
import com.zsxsoft.homework.atm.backend.payload.*;
import com.zsxsoft.homework.atm.backend.repository.*;
import com.zsxsoft.homework.atm.backend.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Service
public class ATMService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final TransactionRepository transactionRepository;
    private final RechargeRepository rechargeRepository;
    private final WithdrawRepository withdrawRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(ATMService.class);

    @Autowired
    public ATMService(CardRepository cardRepository, UserRepository userRepository, CurrencyRepository currencyRepository, TransactionRepository transactionRepository, RechargeRepository rechargeRepository, WithdrawRepository withdrawRepository, PasswordEncoder passwordEncoder) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.currencyRepository = currencyRepository;
        this.transactionRepository = transactionRepository;
        this.rechargeRepository = rechargeRepository;
        this.withdrawRepository = withdrawRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Boolean checkCardIsBelongToUser(Card card, User user) {
        return card.getUser().getId().equals(user.getId());
    }

    public void checkCardIsBelongToUserOrThrow(Card card, User user) {
        if (!checkCardIsBelongToUser(card, user) && !user.hasRole(RoleName.ROLE_ADMIN)) {
            throw new BadRequestException("Card not belong to this user");
        }
    }

    public PagedResponse<TransactionResponse> getTransactionsByUser(User user, int page, int size) {
        validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        try {
            Page<Transaction> transactions = transactionRepository.findByUserId(user.getId(), pageable);
            List<TransactionResponse> response = transactions.map(TransactionResponse::new).getContent();
            return new PagedResponse<>(response, transactions.getNumber(),
                    transactions.getSize(), transactions.getTotalElements(), transactions.getTotalPages(), transactions.isLast());

        } catch (NullPointerException e) {
            return new PagedResponse<>(Collections.emptyList(), 0, 0, 0, 0, true);
        }

    }

    public PagedResponse<TransactionResponse> getTransactionsByCard(Card card, int page, int size) {
        validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        try {
            Page<Transaction> transactions = transactionRepository.findByFromCardOrToCard(card, card, pageable);
            List<TransactionResponse> response = transactions.map(TransactionResponse::new).getContent();
            return new PagedResponse<>(response, transactions.getNumber(),
                    transactions.getSize(), transactions.getTotalElements(), transactions.getTotalPages(), transactions.isLast());

        } catch (NullPointerException e) {
            return new PagedResponse<>(Collections.emptyList(), 0, 0, 0, 0, true);
        }

    }
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Transaction createTransaction(Card fromCard, Card toCard, TransactionRequest transactionRequest) {

        Currency CNYCurrency = currencyRepository.findByCode("CNY").orElseThrow(() -> new TransactionException("CNY Not Found"));
        // Force to reload balance
        if (toCard.getId().equals(fromCard.getId())) throw new TransactionException("Cannot self transact");
        if (toCard.getUser().getRoles().stream().anyMatch(p -> p.getName().equals(RoleName.ROLE_SYSTEM))) throw new TransactionException("User Not Found");

        Transaction t = new Transaction();
        t.setFromCard(fromCard);
        t.setTax(new BigDecimal(0));
        t.setToCard(toCard);
        t.setAmount(transactionRequest.getAmount());
        t.setCurrency(CNYCurrency);
        t.setRemark(transactionRequest.getRemark());
        t.setType(TransactionType.TRANSACTION_TRANSFER);
        t.updateCardBalances();

        cardRepository.save(fromCard);
        cardRepository.save(toCard);
        return transactionRepository.save(t);

    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Transaction recharge(Card card, TransactionRequest transactionRequest) {

        Currency CNYCurrency = currencyRepository.findByCode("CNY").orElseThrow(() -> new TransactionException("CNY Not Found"));
        card = cardRepository.findById(card.getId()).orElseThrow(() -> new TransactionException("User Not Found"));
        Currency currency;
        if (transactionRequest.getCurrencyId() == null) {
            currency = CNYCurrency;
        } else {
            currency = currencyRepository.findById(Long.parseLong(transactionRequest.getCurrencyId())).orElseThrow(() -> new TransactionException("Currency Not Found"));
        }

        Card chargeCard = cardRepository.findByType(CardType.BANKCARDTYPE_CHARGE);
        if (chargeCard.getId().equals(card.getId())) throw new TransactionException("Cannot self transact");

        Recharge c = new Recharge();
        c.setCurrency(currency);
        c.setCard(card);
        c.setAmount(transactionRequest.getAmount());
        chargeCard.setAvailableBalance(c.getActualAmount());

        Transaction t = new Transaction();
        t.setFromCard(chargeCard);
        t.setTax(new BigDecimal(0));
        t.setToCard(card);
        t.setAmount(c.getActualAmount());
        t.setCurrency(CNYCurrency);
        t.setRemark("Recharge ￥" + c.getActualAmount().setScale(2, BigDecimal.ROUND_DOWN).toString());
        t.setType(TransactionType.TRANSACTION_RECHARGE);
        t.setRecharge(c);
        t.updateCardBalances();
        c.setTransaction(t);
        chargeCard.setAvailableBalance(new BigDecimal(0));

        cardRepository.save(card);
        // cardRepository.save(chargeCard);
        rechargeRepository.save(c);
        return transactionRepository.save(t);

    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Transaction withdraw(Card card, TransactionRequest transactionRequest) {

        Currency CNYCurrency = currencyRepository.findByCode("CNY").orElseThrow(() -> new TransactionException("CNY Not Found"));
        card = cardRepository.findById(card.getId()).orElseThrow(() -> new TransactionException("User Not Found"));
        Currency currency;
        if (transactionRequest.getCurrencyId() == null) {
            currency = CNYCurrency;
        } else {
            currency = currencyRepository.findById(Long.parseLong(transactionRequest.getCurrencyId())).orElseThrow(() -> new TransactionException("Currency Not Found"));
        }

        Card withdrawCard = cardRepository.findByType(CardType.BANKCARDTYPE_WITHDRAW);
        if (withdrawCard.getId().equals(card.getId())) throw new TransactionException("Cannot self transact");

        Withdraw c = new Withdraw();
        c.setCurrency(currency);
        c.setCard(card);
        c.setAmount(transactionRequest.getAmount());

        Transaction t = new Transaction();
        t.setFromCard(card);
        t.setTax(new BigDecimal(0));
        t.setToCard(withdrawCard);
        t.setAmount(c.getActualAmount());
        t.setCurrency(CNYCurrency);
        t.setRemark("Withdraw ￥" + c.getActualAmount().setScale(2, BigDecimal.ROUND_DOWN).toString());
        t.setType(TransactionType.TRANSACTION_WITHDRAW);
        t.setWithdraw(c);
        t.updateCardBalances();
        c.setTransaction(t);
        withdrawCard.setAvailableBalance(new BigDecimal(0));

        cardRepository.save(card);
        // cardRepository.save(chargeCard);
        withdrawRepository.save(c);
        return transactionRepository.save(t);


    }

    public User resetPassword (User user) {
        return updatePassword(user, "123456");
    }

    public User updatePassword (User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    public User checkAndUpdatePassword (User user, String oldPassword, String newPassword) {
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadRequestException("Original Password Error");
        }
        return updatePassword(user, newPassword);
    }

    public Card createNewCard (User user) {
        Card c = new Card();
        Faker f = new Faker();
        c.setUser(user);
        c.setCardNumber(f.finance().creditCard(CreditCardType.MASTERCARD).replaceAll("-", ""));
        return cardRepository.save(c);
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }


}
