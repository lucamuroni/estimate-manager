package com.project.webapp.estimatemanager.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "options")
public class Opt implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, updatable = false)
    private Long id;
    @Column(nullable = false)
    private String type;
    @Column(unique = true, nullable = false)
    private String name;
    @ManyToMany(mappedBy = "options")
    private Set<Estimate> estimates = new HashSet<>();
}
