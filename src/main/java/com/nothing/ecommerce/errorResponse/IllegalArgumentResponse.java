package com.nothing.ecommerce.errorResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IllegalArgumentResponse {
    private int status;
    private String message;
    private Long timeStamp;
}
