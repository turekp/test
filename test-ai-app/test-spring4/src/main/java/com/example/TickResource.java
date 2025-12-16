package com.example;

import org.springframework.hateoas.ResourceSupport;

/**
 * Created by piotr on 15/11/16.
 */
public class TickResource extends ResourceSupport {

    private final Double price;

    public TickResource(Double price) {
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }
}
