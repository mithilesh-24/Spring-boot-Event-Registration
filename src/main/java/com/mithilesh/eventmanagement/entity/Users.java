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
    long user_id;

    String first_name;
    String last_name;
    LocalDate dob;

    @Enumerated(EnumType.STRING)
    Gender gender;
    String email;
    String Password;

    @Enumerated(EnumType.STRING)
    Role role = Role.USER;

    @ManyToMany
    @JoinTable(
            name = "registration",
            joinColumns =  @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    List<Events> reg_event;

    @ManyToMany
    @JoinTable(
            name = "faviorts",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    List<Events> fav_event;
}