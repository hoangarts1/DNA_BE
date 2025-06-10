package com.ADNService.SWP391.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffDTO {
    private Long id;
    private Long accountId;
    private String fingerprint;
    private String role;
}
