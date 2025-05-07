package com.nationalbank.controller;

import com.nationalbank.dto.TransferRequest;
import com.nationalbank.model.Banking;
import com.nationalbank.service.BankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/banking")
@Tag(name = "Banking Operations", description = "APIs for managing banking transactions")
public class BankingController {
    @Autowired
    private BankingService bankingService;

    @PostMapping("/save/{customerId}")
    @Operation(summary = "Save money to customer account", description = "Deposits a specified amount to the customer's account")
    public ResponseEntity<Banking> saveMoney(@PathVariable Long customerId, @RequestParam Double amount) {
        return ResponseEntity.ok(bankingService.saveMoney(customerId, amount));
    }

    @PostMapping("/withdraw/{customerId}")
    @Operation(summary = "Withdraw money from customer account", description = "Withdraws a specified amount from the customer's account")
    public ResponseEntity<Banking> withdrawMoney(@PathVariable Long customerId, @RequestParam Double amount) {
        return ResponseEntity.ok(bankingService.withdrawMoney(customerId, amount));
    }

    @PostMapping("/transfer")
    @Operation(summary = "Transfer money between customers", description = "Transfers a specified amount from one customer to another")
    public ResponseEntity<Banking> transferMoney(@RequestBody TransferRequest request) {
        return ResponseEntity.ok(bankingService.transferMoney(request));
    }
}