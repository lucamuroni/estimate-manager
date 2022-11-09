package com.project.webapp.estimatemanager.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Estimate implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, updatable = false)
    private Long id;
    private Float price;
    //private Date;
    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Client client;
    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id",nullable = false)
    @JsonBackReference
    private Employee employee;
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id",nullable = false)
    private Product product;
    @ManyToMany
    @JoinTable(
            name = "estimate_options",
            joinColumns = @JoinColumn(name = "estimate_id"),
            inverseJoinColumns = @JoinColumn(name = "option_id")
    )
    @ToString.Exclude
    private Set<Opt> options = new HashSet<>();
}
