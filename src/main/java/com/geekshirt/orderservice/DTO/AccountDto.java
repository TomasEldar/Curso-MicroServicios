package com.geekshirt.orderservice.DTO;

import com.geekshirt.orderservice.util.AccountStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDto {

    private Long id;
    private AddressDto address;
    private CustomerDto customer;
    private CreditCardDto creditCard;
    private AccountStatus status;
}
