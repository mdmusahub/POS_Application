package com.mecaps.posDev.ServiceImplementation;

import com.mecaps.posDev.Entity.Customer;
import com.mecaps.posDev.Exception.CustomerAlreadyExist;
import com.mecaps.posDev.Exception.CustomerNotFound;
import com.mecaps.posDev.Repository.CustomerRepository;
import com.mecaps.posDev.Request.CustomerRequest;
import com.mecaps.posDev.Response.CustomerResponse;
import com.mecaps.posDev.Service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for managing customer operations.
 * Handles creating, updating, fetching, and deleting customers.
 */
@Service
public class CustomerServiceImplementation implements CustomerService {

    private final CustomerRepository customerRepository;

    /**
     * Constructor for injecting CustomerRepository.
     *
     * @param customerRepository repository for customer data operations
     */
    public CustomerServiceImplementation(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Creates a new customer.
     * <p>
     * Validates duplicate phone numbers before saving.
     *
     * @param customerRequest customer details such as phone and email
     * @return success message after creation
     * @throws CustomerAlreadyExist if the phone number already exists
     */
    @Override
    public String createCustomer(CustomerRequest customerRequest) {
        customerRepository.findByPhoneNumber(customerRequest.getPhoneNumber())
                .ifPresent(present -> {
                    throw new CustomerAlreadyExist("This customer is already exist " + customerRequest.getPhoneNumber());
                });

        Customer customer = new Customer();
        customer.setEmail(customerRequest.getEmail());
        customer.setPhoneNumber(customerRequest.getPhoneNumber());
        customerRepository.save(customer);

        return "Customer create successfully";
    }

    /**
     * Updates an existing customer’s information.
     *
     * @param id customer ID
     * @param customerRequest new email and phone number values
     * @return success message after update
     * @throws CustomerNotFound if the customer ID does not exist
     */
    @Override
    public String updateCustomer(long id, CustomerRequest customerRequest) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFound("This customer is not found " + id));

        customer.setEmail(customerRequest.getEmail());
        customer.setPhoneNumber(customerRequest.getPhoneNumber());
        customerRepository.save(customer);

        return "This customer is successfully updated";
    }

    /**
     * Retrieves all customers.
     *
     * @return list of CustomerResponse objects
     */
    @Override
    public List<CustomerResponse> getAll() {
        List<Customer> customerList = customerRepository.findAll();
        return customerList.stream().map(CustomerResponse::new).toList();
    }

    /**
     * Deletes a customer by ID.
     *
     * @param id customer ID
     * @return success message after deletion
     * @throws CustomerNotFound if the customer does not exist
     */
    @Override
    public String deleteCustomer(long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFound("This customer is not found " + id));

        customerRepository.delete(customer);
        return "This customer is successfully deleted";
    }

    /**
     * Fetches a customer by ID.
     *
     * @param id customer ID
     * @return CustomerResponse for the requested customer
     * @throws CustomerNotFound if the customer ID does not exist
     */
    @Override
    public CustomerResponse getById(long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFound("This customer is not found " + id));
        return new CustomerResponse(customer);
    }
}
