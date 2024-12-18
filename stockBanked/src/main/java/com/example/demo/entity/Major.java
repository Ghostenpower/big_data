package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Major {
    @JsonProperty(value = "MajorName")
    private String MajorName;
    @JsonProperty(value = "MajorCode")
    private String MajorCode;
}
