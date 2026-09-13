package ru.practicum.shareit.user;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@EqualsAndHashCode(of = {"email"})
@Builder
public class User {
    private long id;
    private String name;
    private String email;
}
