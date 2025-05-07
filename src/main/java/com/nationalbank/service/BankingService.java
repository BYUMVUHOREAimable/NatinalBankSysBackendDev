package com.nationalbank.service;

import com.nationalbank.dto.TransferRequest;
import com.nationalbank.exception.InsufficientBalanceException;
import com.nationalbank.exception.ResourceNotFoundException;
import com.nationalbank.model.Banking;
import com.nationalbank.model.Customer;
import com.nationalbank.repository.BankingRepository;
import com.nationalbank.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class BankingService {
    private static final Logger logger = LoggerFactory.getLogger(BankingService.class);

    @Autowired
    private BankingRepository bankingRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private EmailService emailService;

    @Transactional
    public Banking saveMoney(Long customerId, Double amount) {
        try {
            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be positive");
            }
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

            customer.setBalance(customer.getBalance() + amount);
            customer.setLastUpdateDateTime(LocalDateTime.now());
            customerRepository.save(customer);

            Banking banking = new Banking();
            banking.setCustomer(customer);
            banking.setAccount(customer.getAccount());
            banking.setAmount(amount);
            banking.setType("saving");
            banking.setBankingDateTime(LocalDateTime.now());

            Banking savedBanking = bankingRepository.save(banking);

            String message = String.format("Dear %s %s, Your saving of %s on your account %s has been Completed Successfully.",
                    customer.getFirstName(), customer.getLastName(), amount, customer.getAccount());
            try {
                emailService.sendEmail(customer.getEmail(), "Saving Transaction", message);
            } catch (MailException e) {
                logger.error("Failed to send email for saving transaction to {}: {}", customer.getEmail(), e.getMessage());
            }

            return savedBanking;
        } catch (Exception e) {
            logger.error("Error in saveMoney for customerId {}: {}", customerId, e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public Banking withdrawMoney(Long customerId, Double amount) {
        try {
            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be positive");
            }
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

            if (customer.getBalance() < amount) {
                throw new InsufficientBalanceException("Insufficient balance for withdrawal");
            }

            customer.setBalance(customer.getBalance() - amount);
            customer.setLastUpdateDateTime(LocalDateTime.now());
            customerRepository.save(customer);

            Banking banking = new Banking();
            banking.setCustomer(customer);
            banking.setAccount(customer.getAccount());
            banking.setAmount(amount);
            banking.setType("withdraw");
            banking.setBankingDateTime(LocalDateTime.now());

            Banking savedBanking = bankingRepository.save(banking);

            String message = String.format("Dear %s %s, Your withdraw of %s on your account %s has been Completed Successfully.",
                    customer.getFirstName(), customer.getLastName(), amount, customer.getAccount());
            try {
                emailService.sendEmail(customer.getEmail(), "Withdraw Transaction", message);
            } catch (MailException e) {
                logger.error("Failed to send email for withdraw transaction to {}: {}", customer.getEmail(), e.getMessage());
            }

            return savedBanking;
        } catch (Exception e) {
            logger.error("Error in withdrawMoney for customerId {}: {}", customerId, e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public Banking transferMoney(TransferRequest request) {
        try {
            if (request.getAmount() <= 0) {
                throw new IllegalArgumentException("Amount must be positive");
            }
            Customer fromCustomer = customerRepository.findById(request.getFromCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("From Customer not found with id: " + request.getFromCustomerId()));
            Customer toCustomer = customerRepository.findById(request.getToCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("To Customer not found with id: " + request.getToCustomerId()));

            if (fromCustomer.getBalance() < request.getAmount()) {
                throw new InsufficientBalanceException("Insufficient balance for transfer");
            }

            fromCustomer.setBalance(fromCustomer.getBalance() - request.getAmount());
            fromCustomer.setLastUpdateDateTime(LocalDateTime.now());
            toCustomer.setBalance(toCustomer.getBalance() + request.getAmount());
            toCustomer.setLastUpdateDateTime(LocalDateTime.now());
            customerRepository.save(fromCustomer);
            customerRepository.save(toCustomer);

            Banking banking = new Banking();
            banking.setCustomer(fromCustomer);
            banking.setAccount(fromCustomer.getAccount());
            banking.setAmount(request.getAmount());
            banking.setType("transfer");
            banking.setBankingDateTime(LocalDateTime.now());

            Banking savedBanking = bankingRepository.save(banking);

            String message = String.format("Dear %s %s, Your transfer of %s on your account %s has been Completed Successfully.",
                    fromCustomer.getFirstName(), fromCustomer.getLastName(), request.getAmount(), fromCustomer.getAccount());
            try {
                emailService.sendEmail(fromCustomer.getEmail(), "Transfer Transaction", message);
            } catch (MailException e) {
                logger.error("Failed to send email for transfer transaction to {}: {}", fromCustomer.getEmail(), e.getMessage());
            }

            return savedBanking;
        } catch (Exception e) {
            logger.error("Error in transferMoney from {} to {}: {}", request.getFromCustomerId(), request.getToCustomerId(), e.getMessage(), e);
            throw e;
        }
    }
}