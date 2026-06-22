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
    @GeneratedValue(strategy = GenerationType.AUTO)
    long state_id;

    String state_name;

    @OneToMany(mappedBy = "state")
    List<Events> events;
}
