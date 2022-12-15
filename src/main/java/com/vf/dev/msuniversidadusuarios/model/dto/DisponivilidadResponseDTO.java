package com.vf.dev.msuniversidadusuarios.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisponivilidadResponseDTO implements Serializable {
    @JsonProperty
    private boolean isDisponible;
    @JsonProperty
    private boolean isCorreoDisponible;
    @JsonProperty
    private boolean isUsernameDisponible;
    @JsonProperty
    private String message;


}
