package com.mecaps.posDev.Controller;

import com.mecaps.posDev.Request.CustomerRequest;
import com.mecaps.posDev.Response.CustomerResponse;
import com.mecaps.posDev.Service.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Customer")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/createCustomer")
    public String createCustomer(@RequestBody CustomerRequest request) {
        return customerService.createCustomer(request);

    }

    @PutMapping("/updateCustomer")
    public String updateCustomer(@PathVariable long id, @RequestBody CustomerRequest customerRequest) {
        return customerService.updateCustomer(id, customerRequest);
    }

    @GetMapping("/getCustomer")
    public List<CustomerResponse> getCustomer() {
        return customerService.getAll();
    }

    @GetMapping("/getById/{id}")
    public CustomerResponse getById(@PathVariable long id) {
        return customerService.getById(id);
    }

    @DeleteMapping("deleteCustomer/{id}")
    public String deleteCustomer(@PathVariable long id) {
        return customerService.deleteCustomer(id);
    }
}
