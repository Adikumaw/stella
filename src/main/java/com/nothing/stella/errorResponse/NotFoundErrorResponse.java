package com.nothing.stella.errorResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotFoundErrorResponse {
    private int status;
    private String message;
    private Long timeStamp;
}
