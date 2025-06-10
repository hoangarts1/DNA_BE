package com.ADNService.SWP391.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {
    private Long id;
    private Long accountId;
    private String address;
    private String gender;
    private Date dateOfBirth;
    private String documentType;
    private String placeOfIssue;
    private Date dateOfIssue;
    private String fingerprint;
}
