package com.javareact.spring6restmvc.mappers;

import com.javareact.spring6restmvc.entities.Customer;
import com.javareact.spring6restmvc.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

 Customer customerDtoToCustomer(CustomerDTO dto);

 CustomerDTO customerToCustomerDto(Customer customer);
}
