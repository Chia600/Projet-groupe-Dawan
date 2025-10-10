package com.dawanproject.booktracker.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

@Entity
@Table(name = "user")
public class User extends Person {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(length = 100, nullable = false, unique=true)
    @NotBlank
    private String username;

    @Column(length=100, nullable = false)
    @NotBlank
    private String password;

    @Column(length=100, nullable = false, unique=true)
    private String email;

    @ToString.Exclude
    private String picture;

    @Temporal(TemporalType.DATE)
    private LocalDate subscriptionDate;

    @Column(nullable = false)
    private boolean isSubscribed;

    @ToString.Exclude
    @ManyToMany
    private Set<Book> books = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Set<Review> reviews = new HashSet<>();
}
