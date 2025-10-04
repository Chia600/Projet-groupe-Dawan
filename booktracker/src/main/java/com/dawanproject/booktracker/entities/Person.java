package com.dawanproject.booktracker.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString

@MappedSuperclass
public abstract class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Version
    private int version;

    @Column(length=100, nullable = false)
    private String firstname;

    @Column(length=100, nullable = false)
    private String lastname;

}
