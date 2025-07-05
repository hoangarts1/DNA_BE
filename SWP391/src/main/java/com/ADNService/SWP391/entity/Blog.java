package com.ADNService.SWP391.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String title;

    @Lob
    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String contentHtml;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String titleImageBase64;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String blogType;

    private LocalDate blogDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
