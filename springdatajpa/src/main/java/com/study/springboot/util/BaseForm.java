package com.study.springboot.util;

import lombok.Data;

@Data
public class BaseForm<ID> {
    private ID id;
    private String search;
}
