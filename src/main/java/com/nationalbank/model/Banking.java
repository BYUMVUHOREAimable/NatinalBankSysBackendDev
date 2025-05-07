package com.nationalbank.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "banking")
public class Banking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private String account;
    private Double amount;
    private String type;

    @Column(name = "banking_date_time")
    private LocalDateTime bankingDateTime;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public String getAccount() { return account; }
    public void setAccount(String account) { this.account = account; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public LocalDateTime getBankingDateTime() { return bankingDateTime; }
    public void setBankingDateTime(LocalDateTime bankingDateTime) { this.bankingDateTime = bankingDateTime; }
}
