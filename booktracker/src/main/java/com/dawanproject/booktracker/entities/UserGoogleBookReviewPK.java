package com.dawanproject.booktracker.entities;

import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class UserGoogleBookReviewPK implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;

    private String googleBookId;
}
