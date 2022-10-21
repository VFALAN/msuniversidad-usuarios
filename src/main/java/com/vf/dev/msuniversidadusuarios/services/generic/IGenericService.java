package com.vf.dev.msuniversidadusuarios.services.generic;

import com.vf.dev.msuniversidadusuarios.model.dto.PaginationObject;
import com.vf.dev.msuniversidadusuarios.utils.exception.MsUniversidadException;

import java.util.List;

public interface IGenericService<R, D, T> {
    R findById(Integer pId) throws MsUniversidadException;

    R saveFromDto(D dto) throws MsUniversidadException;

    R save(R pEntity);

    void delete(R entity);

    R update(R entity);

    List<R> finaAll();

    PaginationObject<T> paginar(int size, int page, String column, String orden);
}
