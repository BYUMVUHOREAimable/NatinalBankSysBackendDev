package com.nationalbank.service;

import com.nationalbank.exception.ResourceNotFoundException;
import com.nationalbank.model.Customer;
import com.nationalbank.repository.BankingRepository;
import com.nationalbank.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BankingRepository bankingRepository;

    public Customer createCustomer(Customer customer) {
        customer.setLastUpdateDateTime(LocalDateTime.now());
        return customerRepository.save(customer);
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        customer.setFirstName(customerDetails.getFirstName());
        customer.setLastName(customerDetails.getLastName());
        customer.setEmail(customerDetails.getEmail());
        customer.setMobile(customerDetails.getMobile());
        customer.setDob(customerDetails.getDob());
        customer.setAccount(customerDetails.getAccount());
        customer.setBalance(customerDetails.getBalance());
        customer.setLastUpdateDateTime(LocalDateTime.now());

        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        if (bankingRepository.existsByCustomerId(id)) {
            throw new IllegalStateException("Cannot delete customer with existing banking transactions");
        }
        customerRepository.delete(customer);
    }
}