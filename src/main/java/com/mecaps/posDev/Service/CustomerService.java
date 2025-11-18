package com.mecaps.posDev.Service;

import com.mecaps.posDev.Request.CustomerRequest;
import com.mecaps.posDev.Response.CustomerResponse;

import java.util.List;

public interface CustomerService {
    String createCustomer(CustomerRequest request);

    String updateCustomer(long id, CustomerRequest customerRequest);

    List<CustomerResponse> getAll();

    String deleteCustomer(long id);

    CustomerResponse getById(long id);
}
