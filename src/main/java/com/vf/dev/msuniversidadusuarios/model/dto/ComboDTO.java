package com.vf.dev.msuniversidadusuarios.model.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComboDTO implements Serializable {
    private String label;
    private String value;
}
