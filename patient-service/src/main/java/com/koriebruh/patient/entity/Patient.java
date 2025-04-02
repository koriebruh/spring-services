package com.koriebruh.patient.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String name;

    @Column(length = 120)
    private String address;

    @Column(nullable = false)
    private String email;

    private String dateOfBirth;

    //if man false , girl true
    private String gender;

    @Column(nullable = false)
    private Long createdAt;

    private Long updatedAt;

    private Long deletedAt;

    public void softDeleted () {
        this.deletedAt = Instant.now().getEpochSecond();
    }
}

