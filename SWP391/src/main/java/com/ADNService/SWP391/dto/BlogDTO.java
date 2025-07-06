package com.ADNService.SWP391.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogDTO {
    private Long id;
    private String title;
    private String contentHtml;
    private String titleImageBase64;
    private String blogType;
    private String blogDate; // dạng chuỗi yyyy-MM-dd

    @JsonProperty("isActive")
    private boolean isActive;
}
