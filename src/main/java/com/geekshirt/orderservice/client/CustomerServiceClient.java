package com.geekshirt.orderservice.client;

import com.geekshirt.orderservice.DTO.AccountDto;
import com.geekshirt.orderservice.DTO.AddressDto;
import com.geekshirt.orderservice.DTO.CreditCardDto;
import com.geekshirt.orderservice.DTO.CustomerDto;
import com.geekshirt.orderservice.config.OrderServiceConfig;
import com.geekshirt.orderservice.util.AccountStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
@Slf4j
public class CustomerServiceClient {

    private RestTemplate restTemplate;

    @Autowired
    private OrderServiceConfig config;

    public CustomerServiceClient (RestTemplateBuilder builder){
        restTemplate = builder.build();
    }

    public Optional<AccountDto> findAccountById(String accountId){
        Optional<AccountDto> result = Optional.empty();
        try{
            result = Optional.ofNullable(restTemplate.getForObject(config.getCustomerServiceUrl() + "/{id}",AccountDto.class,accountId));
        }
        catch (HttpClientErrorException ex) {
            if(ex.getStatusCode() != HttpStatus.NOT_FOUND){
                throw ex;
            }
        }

        return result;
    }

    public AccountDto createDummyAccount(){
        AddressDto address = AddressDto.builder()
                .street("Mariano Otero")
                .city("Guadalajara")
                .state("Jalisco")
                .country("Mexico")
                .zipCode("12131")
                .build();
        CustomerDto customer = CustomerDto.builder()
                .lastName("Madero")
                .firstName("Juan")
                .email("JuanMadero@gmail.com")
                .build();
        CreditCardDto creditCard = CreditCardDto.builder()
                .nameOnCard("Juan Madero")
                .number("1425786941524875")
                .expirationMonth("03")
                .expirationYear("2023")
                .type("VISA")
                .ccv("754")
                .build();
        AccountDto account = AccountDto.builder()
                .customer(customer)
                .adress(address)
                .creditCard(creditCard)
                .status(AccountStatus.ACTIVE)
                .build();
        return account;
    }

    public AccountDto createAccount(AccountDto account){
        return restTemplate.postForObject(config.getCustomerServiceUrl(),account,AccountDto.class);
    }

    public AccountDto createAccountBody(AccountDto account){
        ResponseEntity<AccountDto> responseAccount = restTemplate.postForEntity(config.getCustomerServiceUrl(),account,AccountDto.class);
        log.info("Response: " + responseAccount.getHeaders());
        return responseAccount.getBody();
    }

    public void updateAccount(AccountDto account){
        restTemplate.put(config.getCustomerServiceUrl() + "/{id}",account,account.getId());
    }

    public void deleteAccount(AccountDto account){
        restTemplate.delete(config.getCustomerServiceUrl() + "/{id}",account.getId());
    }

}
