package com.ricardopassarella.nbrown.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class PageableResponse<T> {

    private final T data;
    private final Integer total;
}
