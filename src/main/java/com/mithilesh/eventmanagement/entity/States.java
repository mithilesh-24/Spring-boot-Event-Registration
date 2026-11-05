package com.mithilesh.eventmanagement.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "states")
public class States {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long stateId;

    private String stateName;

    @OneToMany(mappedBy = "state")
    private List<Events> events;

    @OneToOne(mappedBy = "states")
    private CovidAffectedArea covidAffectedArea;
}
