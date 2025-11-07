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

@Service
public class CustomerServiceImplementation implements CustomerService {

    private final CustomerRepository customerRepository ;


    public CustomerServiceImplementation(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public String createCustomer(CustomerRequest customerRequest){
        customerRepository.findByPhoneNumber(customerRequest.getPhoneNumber())
        .ifPresent(present->{throw new CustomerAlreadyExist("This customer is already exist " + customerRequest.getPhoneNumber());});
        Customer customer = new Customer();
        customer.setEmail(customerRequest.getEmail());
        customer.setPhoneNumber(customerRequest.getPhoneNumber());
        customerRepository.save(customer);
        return "Customer create successfully" ;

    }


    public String updateCustomer(long id , CustomerRequest customerRequest){
        Customer customer = customerRepository.findById(id).orElseThrow(()->new CustomerNotFound("This customer is not found " + id)) ;
        customer.setEmail(customerRequest.getEmail());
        customer.setPhoneNumber(customerRequest.getPhoneNumber());
        customerRepository.save(customer);
        return "This customer is successfully updated " ;


    }

     public List<CustomerResponse> getAll(){
      List<Customer> customerList=customerRepository.findAll();
      return customerList.stream().map(CustomerResponse::new).toList() ;

     }

     public String deleteCustomer(long id ){
     Customer customer = customerRepository.findById(id).orElseThrow(()->new CustomerNotFound("This customer is not found " + id)) ;
     customerRepository.delete(customer);
     return "This customer is successfully deleted" ;

   }

   public CustomerResponse getById(long id){
   Customer customer = customerRepository.findById(id).orElseThrow(()->new CustomerNotFound("This customer is not found " + id)) ;
   return new CustomerResponse(customer) ;
   }

   }
