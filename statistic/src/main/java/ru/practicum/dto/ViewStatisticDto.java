package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewStatisticDto {
    @NotBlank
    private String app;

    @NotBlank
    private String uri;

    @NotBlank
    private int hits;
}
