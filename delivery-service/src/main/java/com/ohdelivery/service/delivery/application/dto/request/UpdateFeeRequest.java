package com.ohdelivery.service.delivery.application.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateFeeRequest {

    @NotNull
    private Integer fee;
}
