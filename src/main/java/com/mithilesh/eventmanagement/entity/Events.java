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
@Table(name = "events")
public class Events {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long eventId;

    private String eventName;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private States state;

    private long popularityScores = 0;
    private LocalDate eventDate;
    private LocalDate registrationEndDate;
    private String description;
    private String venue;

    @OneToMany(mappedBy = "event")
    private List<Registration> registrations;

    @OneToMany(mappedBy = "event")
    private List<Favorites> favUsers;

    @OneToMany(mappedBy = "event")
    private List<EventViews> views;
}
