package com.javareact.spring6restmvc.controller;


import com.javareact.spring6restmvc.model.CustomerDTO;
import com.javareact.spring6restmvc.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("api/customer")
@RestController
@RequiredArgsConstructor
public class CustomerController {

    private CustomerService customerService;

    @PatchMapping("{customerId}")
    public ResponseEntity patchCustomerById(@PathVariable("customerId") UUID customerId,
                                            @RequestBody CustomerDTO customer){

        customerService.patchCustomerById(customerId, customer);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{customerId}")
    public ResponseEntity deleteCustomerById(@PathVariable("customerId")UUID customerId){

        customerService.deleteCustomer(customerId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{customerId}")
    public ResponseEntity updateCustomer(@PathVariable("customerId") UUID customerId, @RequestBody CustomerDTO customer){
        customerService.updateCustomer(customerId, customer);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity handleNewCustomer(@RequestBody CustomerDTO customer) {
        CustomerDTO savedCustomer = customerService.saveNewCustomer(customer);

        //create a header object for new customer
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/customer/" + savedCustomer.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }


    @RequestMapping(method = RequestMethod.GET)
    public  List<CustomerDTO> listAllCustomers() {
        return customerService.getAllCustomers();
    }

    @RequestMapping(value = "{customerId}", method = RequestMethod.GET)
    public CustomerDTO getCustomerById(@PathVariable("customerId") UUID id) {
        return customerService.getCustomerByID(id);
    }
}
