package com.sanbox.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.context.annotation.Primary;

@Entity
@Table(name = "employee",uniqueConstraints = @UniqueConstraint(name = "uk_employee_email",columnNames = "email"))
@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
@Builder
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "company_id",nullable = false)
    private Long companyId;

    @NotBlank
    @Column(name = "first_name",nullable = false)
    private String firstName;

    @NotBlank
    @Column(name="last_name",nullable = false)
    private String lastName;

    @NotBlank
    @Email
    @Column(nullable = false,unique = true)
    private String email;


}
