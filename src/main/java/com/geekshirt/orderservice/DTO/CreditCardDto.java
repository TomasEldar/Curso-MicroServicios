package com.geekshirt.orderservice.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreditCardDto {

    private Long id;
    private String nameOnCard;
    private String number;
    private String type;
    private String expirationMonth;
    private String expirationYear;
    private String ccv;

}
