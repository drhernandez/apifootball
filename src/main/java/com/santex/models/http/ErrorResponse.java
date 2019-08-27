package com.santex.models.http;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    @JsonAlias({"error", "errorCode"})
    private int error;
    private String message;
}
