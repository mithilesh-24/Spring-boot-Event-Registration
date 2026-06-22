package com.mithilesh.eventmanagement.entity;

import com.mithilesh.eventmanagement.enums.Gender;
import com.mithilesh.eventmanagement.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long user_id;

    private String first_name;
    private String last_name;
    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String email;
    private String Password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @ManyToMany
    @JoinTable(
            name = "registration",
            joinColumns =  @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Events> reg_event;

    @ManyToMany
    @JoinTable(
            name = "faviorts",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Events> fav_event;
}