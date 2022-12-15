package com.vf.dev.msuniversidadusuarios.services.usuario;

import com.vf.dev.msuniversidadusuarios.model.dto.DisponivilidadResponseDTO;
import com.vf.dev.msuniversidadusuarios.model.dto.PaginationObject;
import com.vf.dev.msuniversidadusuarios.model.dto.usuario.UsuarioDTO;
import com.vf.dev.msuniversidadusuarios.model.dto.usuario.UsuarioTableDTO;
import com.vf.dev.msuniversidadusuarios.model.entity.*;
import com.vf.dev.msuniversidadusuarios.repository.ICarreraRepository;
import com.vf.dev.msuniversidadusuarios.repository.IUsuarioRepository;
import com.vf.dev.msuniversidadusuarios.services.asentamiento.IAsentamientoService;
import com.vf.dev.msuniversidadusuarios.services.carreras.ICarreraService;
import com.vf.dev.msuniversidadusuarios.services.direccion.IDireccionService;
import com.vf.dev.msuniversidadusuarios.services.estatus.IEstatusService;
import com.vf.dev.msuniversidadusuarios.services.perfil.IPerfilService;
import com.vf.dev.msuniversidadusuarios.services.plantel.IPlantelService;
import com.vf.dev.msuniversidadusuarios.utils.PageUtils;
import com.vf.dev.msuniversidadusuarios.utils.cosnts.*;
import com.vf.dev.msuniversidadusuarios.utils.exception.MsUniversidadException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UsuarioServiceImpl implements IUsuarioService {
    @Autowired
    private IUsuarioRepository iUsuarioRepository;
    @Autowired
    private IDireccionService iDireccionService;
    @Autowired
    private IPerfilService iPerfilService;
    @Autowired
    private IEstatusService iEstatusService;
    @Autowired
    private IAsentamientoService iAsentamientoService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IPlantelService iPlantelService;
    @Autowired
    private ICarreraService iCarreraService;

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public UsuarioEntity findById(Integer pId) throws MsUniversidadException {
        Optional<UsuarioEntity> optionalUsuarioEntity = this.iUsuarioRepository.findById(pId);
        if (optionalUsuarioEntity.isPresent()) {
            return optionalUsuarioEntity.get();
        } else {
            throw new MsUniversidadException("No Se encontro ningun Usuario con el Id: " + pId, "ED001");
        }

    }

    @Override
    public UsuarioEntity saveFromDto(UsuarioDTO dto) throws MsUniversidadException {
        UsuarioEntity mUsuarioEntity = modelMapper.map(dto, UsuarioEntity.class);
        DireccionEntity mDireccionEntity = modelMapper.map(dto, DireccionEntity.class);
        EstatusEntity mEstatusEntity = this.iEstatusService.findById(dto.getIdEstatus());
        PerfilEntity mPerfilEntity = this.iPerfilService.findById(dto.getIdPerfil());
        CarreraEntity mCarreraEntity = (dto.getIdPerfil() == EPerfiles.ID_ALUMNO || dto.getIdPerfil() == EPerfiles.ID_JEFE_CARRERA) ? this.iCarreraService.findById(dto.getIdCarrera()) : null;
        PlantelEntity mPlantelEntity = dto.getIdPerfil() != EPerfiles.ID_ADMIN ? this.iPlantelService.findById(dto.getIdPlantel()) : null;
        AsentamientoEntity mAsentamientoEntity = this.iAsentamientoService.findById(dto.getIdAsentamiento());
        mDireccionEntity.setFechaAlta(new Date());
        mDireccionEntity.setIndActivo(true);
        mDireccionEntity.setIdAsentamiento(mAsentamientoEntity);
        mDireccionEntity = this.iDireccionService.save(mDireccionEntity);
        mUsuarioEntity.setEstatus(mEstatusEntity);
        mUsuarioEntity.setPerfil(mPerfilEntity);
        mUsuarioEntity.setMatricula(dto.getIdPerfil() == EPerfiles.ID_ALUMNO ? this.buildMatricula(mUsuarioEntity) : null);
        mUsuarioEntity.setPassword(this.passwordEncoder.encode(dto.getPassword()));
        mUsuarioEntity.setIndActivo(true);
        mUsuarioEntity.setFechaAlta(new Date());
        mUsuarioEntity.setIdDireccion(mDireccionEntity);
        mUsuarioEntity.setPlantel(mPlantelEntity);
        mUsuarioEntity.setCarrera(mCarreraEntity);
        return this.save(mUsuarioEntity);
    }

    @Override
    public UsuarioEntity save(UsuarioEntity pEntity) {
        return this.iUsuarioRepository.save(pEntity);
    }

    @Override
    public void delete(UsuarioEntity entity) {
        entity.setIndActivo(false);
        entity.setFechaBaja(new Date());
        this.save(entity);
    }

    @Override
    public UsuarioEntity update(UsuarioEntity entity) {
        entity.setFechaActualizacion(new Date());
        return this.save(entity);
    }

    @Override
    public List<UsuarioEntity> finaAll() {
        return this.iUsuarioRepository.findAll();
    }

    @Override
    public UsuarioDTO getDetail(Integer pId) throws MsUniversidadException {
        UsuarioEntity mUsuarioEntity = this.findById(pId);
        return UsuarioDTO.builder()
                .idUsuario(mUsuarioEntity.getIdUsuario())
                .nombreUsuario(mUsuarioEntity.getNombreUsuario())
                .apellidoPaterno(mUsuarioEntity.getApellidoPaterno())
                .apellidoMaterno(mUsuarioEntity.getApellidoMaterno())
                .correo(mUsuarioEntity.getCorreo())
                .curp(mUsuarioEntity.getCurp())
                .fechaNacimiento(mUsuarioEntity.getFechaNacimiento())
                .edad(mUsuarioEntity.getEdad())
                .genero(mUsuarioEntity.getGenero())
                .desGenero(mUsuarioEntity.getDesGenero())
                .matricula(mUsuarioEntity.getMatricula())
                .folio(mUsuarioEntity.getFolio())
                .idEstatus(mUsuarioEntity
                        .getEstatus().
                        getIdEstatus()
                )
                .idPerfil(mUsuarioEntity
                        .getPerfil()
                        .getIdPerfil()
                )
                .idDireccion(mUsuarioEntity.getIdDireccion()
                        .getIdDireccion()
                )
                .calle(mUsuarioEntity.getIdDireccion()
                        .getCalle()
                )
                .desFachada(mUsuarioEntity.getIdDireccion()
                        .getDesFachada()
                )
                .idAsentamiento(mUsuarioEntity.getIdDireccion()
                        .getIdAsentamiento()
                        .getIdAsentamientos()
                )
                .asentamiento(mUsuarioEntity.getIdDireccion().getIdAsentamiento().getNombre()
                )
                .idEstado(mUsuarioEntity.getIdDireccion()
                        .getIdAsentamiento()
                        .getIdMunicipio()
                        .getIdEstado().getIdEstado()
                )
                .estado(mUsuarioEntity.getIdDireccion()
                        .getIdAsentamiento()
                        .getIdMunicipio()
                        .getIdEstado()
                        .getNombre()
                )
                .idMunicipio(mUsuarioEntity.getIdDireccion()
                        .getIdAsentamiento()
                        .getIdMunicipio()
                        .getIdMunicipio()
                )
                .municipio(mUsuarioEntity.getIdDireccion()
                        .getIdAsentamiento()
                        .getIdMunicipio()
                        .getNombre()
                )
                .numeroExterior(mUsuarioEntity.getIdDireccion()
                        .getNumeroExterior()
                )
                .numeroInterior(mUsuarioEntity.getIdDireccion()
                        .getNumeroInterior()
                )
                .build();
    }

    @Override
    public PaginationObject<UsuarioTableDTO> paginar(int size, int page, String column, String orden, Map<String, Object> filters) {
        int totalPages = 0;
        int firstRow;
        int limit;
        PaginationObject<UsuarioTableDTO> mPagination = new PaginationObject<>();
        List<UsuarioTableDTO> usuarioTableDTOS = new ArrayList<>();
        Long totalRecords;
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> tupleCriteriaQuery = builder.createTupleQuery();
        Root<UsuarioEntity> mUsuarioRoot = tupleCriteriaQuery.from(UsuarioEntity.class);
        List<Predicate> mPredicateList = this.buildPredicates(filters, mUsuarioRoot, builder);
        Order mOrder = this.buildOrder(builder, mUsuarioRoot, column, orden);
        totalRecords = coutQuery(filters);
        if (totalRecords > 0) {
            totalPages = (int) (totalRecords / size);
            if (totalRecords % size != 0) {
                totalPages++;
            }
            firstRow = (page > 0) ? page * size : 0;

            tupleCriteriaQuery.multiselect(
                    mUsuarioRoot.get(UsuarioConstants.idUsuario).alias(UsuarioConstants.ID_USUARIO),
                    mUsuarioRoot.get(GenericConstans.indActivo).alias(GenericConstans.IND_ACTIVO),
                    mUsuarioRoot.get(UsuarioConstants.nombre).alias(UsuarioConstants.NOMBRE),
                    mUsuarioRoot.get(UsuarioConstants.apellidoPaterno).alias(UsuarioConstants.APELLIDO_PATERNO),
                    mUsuarioRoot.get(UsuarioConstants.apellidoMaterno).alias(UsuarioConstants.APELLIDO_MATERNO),
                    mUsuarioRoot.get(UsuarioConstants.matricula).alias(UsuarioConstants.MATRICULA),
                    mUsuarioRoot.get(UsuarioConstants.estatus).get(EstatusConstans.nombre).alias(EstatusConstans.ID_ESTATUS),
                    mUsuarioRoot.get(UsuarioConstants.perfil).get(PerfilConstans.nombre).alias(PerfilConstans.ID_PERFIL),
                    mUsuarioRoot.get(UsuarioConstants.folio).alias(UsuarioConstants.FOLIO),
                    mUsuarioRoot.get(UsuarioConstants.idDireccion)
                            .get("idAsentamiento").get("nombre").alias("ASENTAMIENTO"),
                    mUsuarioRoot.get(UsuarioConstants.idDireccion)
                            .get("idAsentamiento").get("codigoPostal").alias("CODIGO_POSTAL"),
                    mUsuarioRoot.get(UsuarioConstants.idDireccion)
                            .get("idAsentamiento").get("idMunicipio").get("nombre").alias("MUNICIPIO"),
                    mUsuarioRoot.get(UsuarioConstants.idDireccion)
                            .get("idAsentamiento").get("idMunicipio")
                            .get("idEstado").get("nombre").alias("ESTADO")
            ).where(mPredicateList.toArray(new Predicate[0])).orderBy(mOrder);

            List<Tuple> mTupleList = entityManager.createQuery(tupleCriteriaQuery)
                    .setMaxResults(size).setFirstResult(firstRow).getResultList();
            if (!mTupleList.isEmpty()) {
                mTupleList.forEach(t -> {
                    usuarioTableDTOS.add(PageUtils.fromTuple(t));
                });
            }
        }
        mPagination.setData(usuarioTableDTOS);
        mPagination.setPage(page);
        mPagination.setTotalPages(totalPages);
        mPagination.setTotalRecords(Math.toIntExact(totalRecords));
        return mPagination;

    }

    private Long coutQuery(Map<String, Object> pMapFilters) {
        CriteriaBuilder mCriteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> mCriteriaQueryLong = mCriteriaBuilder.createQuery(Long.class);
        Root<UsuarioEntity> mRootUsuarioEntity = mCriteriaQueryLong.from(UsuarioEntity.class);
        List<Predicate> mListPredicate = this.buildPredicates(pMapFilters, mRootUsuarioEntity, mCriteriaBuilder);
        mCriteriaQueryLong.select(mCriteriaBuilder.count(mRootUsuarioEntity)).where(mListPredicate.toArray(new Predicate[0]));
        return this.entityManager.createQuery(mCriteriaQueryLong).getSingleResult();
    }

    private Order buildOrder(CriteriaBuilder pBuilder, Root<UsuarioEntity> pUsaurioEntityRoot, String column, String order) {
        boolean isAsc = order.toLowerCase().equals("asc");
        Order mOrder = null;
        switch (column) {
            case FiltersConst.NOMBRE ->
                    mOrder = isAsc ? pBuilder.asc(pUsaurioEntityRoot.get(UsuarioConstants.nombre)) : pBuilder.desc(pUsaurioEntityRoot.get(UsuarioConstants.nombre));
            case FiltersConst.PATERNO ->
                    mOrder = isAsc ? pBuilder.asc(pUsaurioEntityRoot.get(UsuarioConstants.apellidoPaterno)) : pBuilder.desc(pUsaurioEntityRoot.get(UsuarioConstants.apellidoPaterno));
            case FiltersConst.MATERNO ->
                    mOrder = isAsc ? pBuilder.asc(pUsaurioEntityRoot.get(UsuarioConstants.apellidoMaterno)) : pBuilder.desc(pUsaurioEntityRoot.get(UsuarioConstants.apellidoMaterno));
            case FiltersConst.PERFIL ->
                    mOrder = isAsc ? pBuilder.asc(pUsaurioEntityRoot.join(UsuarioConstants.idPerfil, JoinType.INNER).get(PerfilConstans.nombre)) : pBuilder.desc(pUsaurioEntityRoot.join(UsuarioConstants.idPerfil, JoinType.INNER).get(PerfilConstans.nombre));
            case FiltersConst.ESTATUS ->
                    mOrder = isAsc ? pBuilder.asc(pUsaurioEntityRoot.join(UsuarioConstants.estatus, JoinType.INNER).get(EstatusConstans.nombre)) : pBuilder.desc(pUsaurioEntityRoot.join(UsuarioConstants.estatus, JoinType.INNER).get(EstatusConstans.nombre));

            case FiltersConst.FOLIO ->
                    mOrder = isAsc ? pBuilder.asc(pUsaurioEntityRoot.get(UsuarioConstants.folio)) : pBuilder.desc(pUsaurioEntityRoot.get(UsuarioConstants.folio));
            case FiltersConst.ASENTAMINETO -> mOrder = isAsc ? pBuilder.asc(pBuilder.upper(
                    pUsaurioEntityRoot.get(UsuarioConstants.idDireccion)
                            .get("idAsentamiento")
                            .get("nombre")
            )) : pBuilder.desc(pBuilder.upper(
                    pUsaurioEntityRoot.get(UsuarioConstants.idDireccion)
                            .get("idAsentamiento")
                            .get("nombre")
            ));
            case FiltersConst.CODIGOPOSTAL -> mOrder = isAsc ? pBuilder.asc(pUsaurioEntityRoot.get(
                    UsuarioConstants.idDireccion).get("idAsentamiento").get("codigoPostal")
            ) : pBuilder.desc(pUsaurioEntityRoot.get(
                    UsuarioConstants.idDireccion).get("idAsentamiento").get("codigoPostal")
            );
            case FiltersConst.MUNICIPIO ->
                    mOrder = isAsc ? pBuilder.asc(pUsaurioEntityRoot.get(UsuarioConstants.idDireccion)
                            .get("idAsentamiento")
                            .get("idMunicipio")
                            .get("nombre")) : pBuilder.desc(pUsaurioEntityRoot.get(UsuarioConstants.idDireccion)
                            .get("idAsentamiento")
                            .get("idMunicipio")
                            .get("nombre"));
            case FiltersConst.ESTADO ->
                    mOrder = isAsc ? pBuilder.asc(pUsaurioEntityRoot.get(UsuarioConstants.idDireccion)
                            .get("idAsentamiento")
                            .get("idMunicipio")
                            .get("idEstado")
                            .get("nombre")) : pBuilder.desc(pUsaurioEntityRoot.get(UsuarioConstants.idDireccion)
                            .get("idAsentamiento")
                            .get("idMunicipio")
                            .get("idEstado")
                            .get("nombre"));
            default -> mOrder = pBuilder.asc(pUsaurioEntityRoot.get(UsuarioConstants.idUsuario));
        }

        return mOrder;
    }

    private List<Predicate> buildPredicates(Map<String, Object> filters, Root<UsuarioEntity> pRoot, CriteriaBuilder builder) {
        List<Predicate> mListPredicateList = new ArrayList<>();
        if (filters != null && !filters.isEmpty()) {
            if (filters.containsKey(FiltersConst.NOMBRE) && filters.get(FiltersConst.NOMBRE) != null) {
                String mStringFilter = (String) filters.get(FiltersConst.NOMBRE);
                mListPredicateList.add(builder.like(pRoot.get(UsuarioConstants.nombre), "%" + mStringFilter.toUpperCase() + "%"));
            }
            if (filters.containsKey(FiltersConst.FOLIO) && filters.get(FiltersConst.FOLIO) != null) {
                String mStringFilter = (String) filters.get(FiltersConst.FOLIO);
                mListPredicateList.add(builder.like(pRoot.get(UsuarioConstants.folio), "%" + mStringFilter.toUpperCase() + "%"));
            }
            if (filters.containsKey(FiltersConst.PATERNO) && filters.get(FiltersConst.PATERNO) != null) {
                String mStringFilter = (String) filters.get(FiltersConst.PATERNO);
                mListPredicateList.add(builder.like(pRoot.get(UsuarioConstants.apellidoPaterno), "%" + mStringFilter.toUpperCase() + "%"));
            }
            if (filters.containsKey(FiltersConst.MATERNO) && filters.get(FiltersConst.MATERNO) != null) {
                String mStringFilter = (String) filters.get(FiltersConst.MATERNO);
                mListPredicateList.add(builder.like(pRoot.get(UsuarioConstants.apellidoMaterno), "%" + mStringFilter.toUpperCase() + "%"));
            }
            if (filters.containsKey(FiltersConst.PERFIL) && filters.get(FiltersConst.PERFIL) != null) {
                String mStringFilter = (String) filters.get(FiltersConst.PERFIL);
                mListPredicateList.add(builder.like(pRoot.join(UsuarioConstants.idPerfil, JoinType.INNER).get(PerfilConstans.nombre), "%" + mStringFilter.toUpperCase() + "%"));
            }
            if (filters.containsKey(FiltersConst.ESTATUS) && filters.get(FiltersConst.ESTATUS) != null) {
                String mStringFilter = (String) filters.get(FiltersConst.ESTATUS);
                mListPredicateList.add(builder.like(pRoot.join(UsuarioConstants.estatus, JoinType.INNER).get(EstatusConstans.nombre), "%" + mStringFilter.toUpperCase() + "%"));
            }
            if (filters.containsKey(FiltersConst.ASENTAMINETO) && filters.get(FiltersConst.ASENTAMINETO) != null) {
                String mStringFilter = (String) filters.get(FiltersConst.ASENTAMINETO);
                mListPredicateList.add(
                        builder.like(
                                builder.upper(
                                        pRoot.get(UsuarioConstants.idDireccion)
                                                .get("idAsentamiento")
                                                .get("nombre")
                                )
                                , "%" + mStringFilter.toUpperCase() + "%"
                        )
                );
            }
            if (filters.containsKey(FiltersConst.CODIGOPOSTAL) && filters.get(FiltersConst.CODIGOPOSTAL) != null) {
                String mStringFilter = (String) filters.get(FiltersConst.CODIGOPOSTAL);
                mListPredicateList.add(builder.like(pRoot.get(UsuarioConstants.idDireccion).get("idAsentamiento").get("codigoPostal"), "%" + mStringFilter));
            }
            if (filters.containsKey(FiltersConst.MUNICIPIO) && filters.get(FiltersConst.MUNICIPIO) != null) {
                String mStringFilter = (String) filters.get(FiltersConst.MUNICIPIO);
                mListPredicateList.add(
                        builder.like(
                                builder.upper(pRoot.get(UsuarioConstants.idDireccion)
                                        .get("idAsentamiento")
                                        .get("idMunicipio")
                                        .get("nombre")
                                ), "%" + mStringFilter.toUpperCase() + "%"
                        )
                );
            }
            if (filters.containsKey(FiltersConst.ESTADO) && filters.get(FiltersConst.ESTADO) != null) {
                String mStringFilter = (String) filters.get(FiltersConst.ESTADO);
                mListPredicateList.add(
                        builder.like(
                                builder.upper(pRoot.get(UsuarioConstants.idDireccion)
                                        .get("idAsentamiento")
                                        .get("idMunicipio")
                                        .get("idEstado")
                                        .get("nombre")
                                ), "%" + mStringFilter.toUpperCase() + "%"
                        )
                );
            }
        }

        return mListPredicateList;
    }

    @Override
    public DisponivilidadResponseDTO validarUsernameAndCorreo(String pUsername, String pCorreo) {
        String mMessage = "";
        boolean mIsDisponible = true;
        boolean mIsDiponibleCorreo = true;
        boolean mIsDisponibleUsername = true;
        List<UsuarioEntity> mUsuarioList = this.iUsuarioRepository.findByCorreo(pCorreo);
        if (!mUsuarioList.isEmpty()) {
            mMessage = mMessage.concat(" El correo : " + pCorreo + " ya esta registrado \n");
            mIsDiponibleCorreo = false;
        }
        mUsuarioList = this.iUsuarioRepository.findByNombreUsuario(pUsername);

        if (!mUsuarioList.isEmpty()) {
            mMessage = mMessage.concat(" El Username: " + pUsername + "no esta disponible");
            mIsDisponibleUsername = false;

        }
        if (!mIsDiponibleCorreo || !mIsDisponibleUsername) {
            mIsDisponible = false;
        }


        return DisponivilidadResponseDTO.builder()
                .isDisponible(mIsDisponible)
                .isCorreoDisponible(mIsDiponibleCorreo)
                .isUsernameDisponible(mIsDisponibleUsername)
                .message(mMessage)
                .build();
    }

    private String buildMatricula(UsuarioEntity pUsuarioEntity) {
        String mStringMatricula = "";
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("YYYY/MM/dd");
        mStringMatricula += mSimpleDateFormat.format(new Date()).replaceAll("/", "");
        mStringMatricula += pUsuarioEntity.getPlantel().getClave();
        mStringMatricula += this.consecutivo(pUsuarioEntity.getPlantel(), pUsuarioEntity.getCarrera());
        return mStringMatricula;
    }


    private String consecutivo(PlantelEntity pPlantelEntity, CarreraEntity pCarreraEntity) {
        CriteriaBuilder mCriteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> mLongCriteriaQuery = mCriteriaBuilder.createQuery(Long.class);
        Root<UsuarioEntity> mUsuarioEntityRoot = mLongCriteriaQuery.from(UsuarioEntity.class);
        List<Predicate> predicateList = new ArrayList<>();
        predicateList.add(mCriteriaBuilder.equal(mUsuarioEntityRoot.get("plantel"), pPlantelEntity));
        predicateList.add(mCriteriaBuilder.equal(mUsuarioEntityRoot.get("carrera"), pCarreraEntity));
        mLongCriteriaQuery.select(mCriteriaBuilder.count(mUsuarioEntityRoot)).where(predicateList.toArray(new Predicate[0]));
        Long count = entityManager.createQuery(mLongCriteriaQuery).getSingleResult();
        count = count == 0 ? count++ : count;
        return String.format("%06d", count);
    }
}
