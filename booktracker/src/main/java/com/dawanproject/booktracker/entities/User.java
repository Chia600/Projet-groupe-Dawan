package com.dawanproject.booktracker.entities;

import jakarta.persistence.*;
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

    @Column(length = 100, nullable = false)
    private String username;

    @Column(length=100, nullable = false)
    private String password;

    @Column(length=100, nullable = false)
    private String email;

    @ToString.Exclude
    private String picture;

    @Temporal(TemporalType.DATE)
    private LocalDate subscriptionDate;

    @Column(nullable = false)
    private boolean subscribed;

    @ToString.Exclude
    @ManyToMany
    private Set<Book> bookCollection = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    private Set<Review> reviews = new HashSet<>();
}
