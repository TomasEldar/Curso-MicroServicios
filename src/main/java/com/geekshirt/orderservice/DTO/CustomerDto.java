package com.geekshirt.orderservice.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;

}
