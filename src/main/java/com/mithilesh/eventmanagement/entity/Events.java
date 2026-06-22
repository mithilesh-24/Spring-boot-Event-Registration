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
    private States state;
    private long popularity_score;
    private LocalDate event_date;
    private LocalDate registration_end_date;
    private String description;
    private String venue;

    @ManyToMany(mappedBy = "reg_events")
    private List<Users> reg_users;

    @ManyToMany(mappedBy = "fav_events")
    private List<Users> fav_users;
}
