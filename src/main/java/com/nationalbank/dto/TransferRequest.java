package com.nationalbank.dto;

public class TransferRequest {
    private Long fromCustomerId;
    private Long toCustomerId;
    private Double amount;

    // Getters and Setters
    public Long getFromCustomerId() { return fromCustomerId; }
    public void setFromCustomerId(Long fromCustomerId) { this.fromCustomerId = fromCustomerId; }
    public Long getToCustomerId() { return toCustomerId; }
    public void setToCustomerId(Long toCustomerId) { this.toCustomerId = toCustomerId; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}