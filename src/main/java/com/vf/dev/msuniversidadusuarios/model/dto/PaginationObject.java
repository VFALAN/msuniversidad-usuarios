package com.vf.dev.msuniversidadusuarios.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaginationObject<T> implements Serializable {

    List<T> data;
    Integer totalRecords;
    Integer page;
    Integer totalPages;
}
