package com.project.webapp.estimatemanager.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

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
    //@JsonBackReference
    private Client client;
    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id",nullable = false)
    //@JsonBackReference
    private Employee employee;
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id",nullable = false)
    private Product product;
}
