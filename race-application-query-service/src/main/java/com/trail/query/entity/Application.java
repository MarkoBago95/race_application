package com.trail.query.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Table(name = "application")
@NoArgsConstructor
@AllArgsConstructor
public class Application {
    @Id
    private UUID id;

    @Column(nullable = false, length = 255)
    private String firstName;

    @Column(nullable = false, length = 255)
    private String lastName;

    @Column(length = 255)
    private String club;

    @ManyToOne
    @JoinColumn(name = "race_id", nullable = false)
    private Race race;
}