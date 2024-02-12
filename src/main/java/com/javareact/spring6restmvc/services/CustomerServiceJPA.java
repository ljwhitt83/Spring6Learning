package com.javareact.spring6restmvc.services;

import com.javareact.spring6restmvc.mappers.CustomerMapper;
import com.javareact.spring6restmvc.model.CustomerDTO;
import com.javareact.spring6restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJPA implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    @Override
    public CustomerDTO getCustomerByID(UUID uuid) {
        return null;
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return null;
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customer) {

        return customerMapper.customerToCustomerDto(customerRepository.save(customerMapper.customerDtoToCustomer(customer)));
    }

    @Override
    public Optional<CustomerDTO> updateCustomer(UUID customerId, CustomerDTO customer) {
        AtomicReference<Optional<CustomerDTO>> customerDTO = new AtomicReference<>();

        customerRepository.findById(customerId).ifPresentOrElse(customerToUpdate -> {
            customerToUpdate.setCustomerName(customer.getCustomerName());
            customerDTO.set(Optional.of(customerMapper.customerToCustomerDto(customerRepository.save(customerToUpdate))));
        }, () -> {
            customerDTO.set(Optional.empty());
        });
        return customerDTO.get();

    }

    @Override
    public Boolean deleteCustomer(UUID customerId) {
        if (customerRepository.existsById(customerId)) {
            customerRepository.deleteById(customerId);
            return true;
        }
        return false;
    }

    @Override
    public Optional<CustomerDTO> patchCustomerById(UUID customerId, CustomerDTO customer) {
        AtomicReference<Optional<CustomerDTO>> customerDTO = new AtomicReference<>();

        customerRepository.findById(customerId).ifPresentOrElse(customerToPatch -> {
            if (StringUtils.hasText(customer.getCustomerName())) {
                customerToPatch.setCustomerName(customer.getCustomerName());
            }
            customerDTO.set(Optional.of(customerMapper.customerToCustomerDto(customerRepository.save(customerToPatch))));
        }, () -> {
            customerDTO.set(Optional.empty());
    });
        return customerDTO.get();
    }
}
