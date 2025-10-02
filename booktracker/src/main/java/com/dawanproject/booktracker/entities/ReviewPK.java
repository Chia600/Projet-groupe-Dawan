package com.dawanproject.booktracker.entities;

import jakarta.persistence.Embeddable;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

@Embeddable
public class ReviewPK {

    private long userId;

    private long bookId;
}
