package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.Customer;
import com.mecaps.posDev.Repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImplementation {

    private final CustomerRepository customerRepository;

    public CustomerServiceImplementation(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(){
        customerRepository.findById()

    }
}
