package com.sanbox.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(
        name = "company",
        uniqueConstraints = @UniqueConstraint(name = "uk_company_name",columnNames = "name")
)
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank
    @Column(nullable = false,unique = true,length = 255)
    private String name;


    @Column(nullable = false)
    private int employeeCount;
}
