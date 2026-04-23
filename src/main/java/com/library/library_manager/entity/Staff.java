package com.library.library_manager.entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity

public class Staff {
    @Id private String staff_id;
    private String position;
    @OneToOne @JoinColumn(name = "user_id")
    private User user;
}
