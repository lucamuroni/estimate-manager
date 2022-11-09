package com.project.webapp.estimatemanager.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "options")
public class Opt implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, updatable = false)
    private Long id;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private String name;
    @ManyToMany(mappedBy = "options")
    //@JsonBackReference
    @ToString.Exclude
    private Set<Estimate> estimates = new HashSet<>();
}
