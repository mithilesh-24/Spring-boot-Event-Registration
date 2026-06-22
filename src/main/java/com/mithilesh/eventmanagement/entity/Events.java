package com.mithilesh.eventmanagement.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Events {

    @Id
    long event_id;

    @ManyToOne
    @JoinColumn(name = "state_id")
    States state;
    long popularity_score;
    LocalDate event_date;
    LocalDate registration_end_date;
    String description;
    String venue;

    @ManyToMany(mappedBy = "reg_events")
    List<Users> reg_users;

    @ManyToMany(mappedBy = "fav_events")
    List<Users> fav_users;
}
