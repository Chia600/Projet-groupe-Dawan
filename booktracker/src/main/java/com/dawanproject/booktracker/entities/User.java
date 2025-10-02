package com.dawanproject.booktracker.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

@Entity
@Table(name = "user")
public class User extends Person implements Serializable {

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
    @OneToMany
    private Set<Book> bookCollection;

    @ToString.Exclude
    @OneToMany
    private Set<Review> reviews;
}
