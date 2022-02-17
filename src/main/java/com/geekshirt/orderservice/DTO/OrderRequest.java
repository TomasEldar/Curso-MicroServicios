package com.geekshirt.orderservice.DTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@ApiModel(description = "Class representing an order to processed")
public class OrderRequest {

    @NotNull
    @NotBlank
    @ApiModelProperty(notes = "Account ID", example = "999885551452",required = true)
    private String accountId;

    @ApiModelProperty(notes = "Items included in the order",required = true)
    private List<LineItem> items;
}
