package ru.klimov.currencyexchange.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Currency {

    private Long currencyId;

    private String code;

    private String fullName;

    private String sign;
}
