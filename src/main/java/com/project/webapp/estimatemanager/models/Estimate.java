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
@Builder
public class Estimate implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, updatable = false)
    private Long id;
    private Float price;
    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
    private UserEntity client;
    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id",nullable = false)
    private UserEntity employee;
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id",nullable = false)
    private Product product;
    @ManyToMany
    @JoinTable(
            name = "estimate_options",
            joinColumns = @JoinColumn(name = "estimate_id"),
            inverseJoinColumns = @JoinColumn(name = "option_id")
    )
    private Set<Opt> options = new HashSet<>();
}
