package com.sanbox.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "plan", uniqueConstraints = @UniqueConstraint(name = "uk_plan_code",columnNames = "planCode"))
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true,nullable = false,length = 20)
    private String planCode;

    @NotBlank
    @Column(nullable = false)
    private String name;


//    active (default true)
    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer monthlyPremium;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;
}
