package com.flipkart.grpc.jexpress.service;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Test {
    @Id
    Long id;
    String name;
}
