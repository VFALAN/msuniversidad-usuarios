package com.vf.dev.msuniversidadusuarios.services.usuario;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vf.dev.msuniversidadusuarios.model.dto.DisponivilidadResponseDTO;
import com.vf.dev.msuniversidadusuarios.model.dto.PaginationObject;
import com.vf.dev.msuniversidadusuarios.model.dto.usuario.IUsuarioDetalle;
import com.vf.dev.msuniversidadusuarios.model.dto.usuario.UsuarioDTO;
import com.vf.dev.msuniversidadusuarios.model.dto.usuario.UsuarioTableDTO;
import com.vf.dev.msuniversidadusuarios.model.entity.*;
import com.vf.dev.msuniversidadusuarios.repository.IUsuarioRepository;
import com.vf.dev.msuniversidadusuarios.services.asentamiento.IAsentamientoService;
import com.vf.dev.msuniversidadusuarios.services.carreras.ICarreraService;
import com.vf.dev.msuniversidadusuarios.services.direccion.IDireccionService;
import com.vf.dev.msuniversidadusuarios.services.estado.IEstadoService;
import com.vf.dev.msuniversidadusuarios.services.estatus.IEstatusService;
import com.vf.dev.msuniversidadusuarios.services.msexcelexport.IMsExcelExport;
import com.vf.dev.msuniversidadusuarios.services.municipio.IMunicipioService;
import com.vf.dev.msuniversidadusuarios.services.perfil.IPerfilService;
import com.vf.dev.msuniversidadusuarios.services.plantel.IPlantelService;
import com.vf.dev.msuniversidadusuarios.utils.TupleUtils;
import com.vf.dev.msuniversidadusuarios.utils.cosnts.*;
import com.vf.dev.msuniversidadusuarios.utils.exception.MsUniversidadException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class UsuarioServiceImpl implements IUsuarioService {
    private static final String UNSELECT = "unselect";
    private static final String ASC = "asc";
    @Value("${msuniversidad.queue.expor-usuario-list}")
    private String routingKey;
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
    @Autowired
    private IEstadoService iEstadoService;
    @Autowired
    private IMunicipioService iMunicipioService;
    @Autowired
    private IMsExcelExport iMsExcelExport;
    @Autowired
    RabbitTemplate rabbitTemplate;
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
        mUsuarioEntity.setIdEstatus(mEstatusEntity);
        mUsuarioEntity.setIdPerfil(mPerfilEntity);
        mUsuarioEntity.setMatricula(dto.getIdPerfil() == EPerfiles.ID_ALUMNO ? this.buildMatricula(mUsuarioEntity) : null);
        mUsuarioEntity.setPassword(this.passwordEncoder.encode(dto.getPassword()));
        mUsuarioEntity.setIndActivo(true);
        mUsuarioEntity.setFechaAlta(new Date());
        mUsuarioEntity.setIdDireccion(mDireccionEntity);
        mUsuarioEntity.setIdPlantel(mPlantelEntity);
        mUsuarioEntity.setIdCarrera(mCarreraEntity);
        mUsuarioEntity.setMatricula(buildMatricula(mUsuarioEntity));
        return this.save(mUsuarioEntity);
    }

    @Override
    @Async
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
    public IUsuarioDetalle getDetail(Integer pId) throws MsUniversidadException {
        return this.iUsuarioRepository.getDetail(pId);
    }

    @Override
    public PaginationObject<UsuarioTableDTO> paginar(int size, int page, String column, Map<String, Object> orden, Map<String, Object> filters) {
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
        List<Order> mOrder = this.buildOrder(builder, mUsuarioRoot, orden);
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
                    mUsuarioRoot.get(UsuarioConstants.idEstatus).get(EstatusConstans.nombre).alias(EstatusConstans.ID_ESTATUS),
                    mUsuarioRoot.get(UsuarioConstants.idPerfil).get(PerfilConstans.nombre).alias(PerfilConstans.ID_PERFIL),
                    mUsuarioRoot.get(UsuarioConstants.folio).alias(UsuarioConstants.FOLIO),
                    mUsuarioRoot.get(UsuarioConstants.idDireccion)
                            .get(DireccionCosntans.IDASENTAMIENTO).get(DireccionCosntans.NOMBRE).alias("ASENTAMIENTO"),
                    mUsuarioRoot.get(UsuarioConstants.idDireccion)
                            .get(DireccionCosntans.IDASENTAMIENTO).get(DireccionCosntans.CODIGOPOSTAL).alias("CODIGO_POSTAL"),
                    mUsuarioRoot.get(UsuarioConstants.idDireccion)
                            .get(DireccionCosntans.IDASENTAMIENTO).get(DireccionCosntans.IDMUNICIPIO).get(DireccionCosntans.NOMBRE).alias("MUNICIPIO"),
                    mUsuarioRoot.get(UsuarioConstants.idDireccion)
                            .get(DireccionCosntans.IDASENTAMIENTO).get(DireccionCosntans.IDMUNICIPIO)
                            .get(DireccionCosntans.IDESTADO).get(DireccionCosntans.NOMBRE).alias("ESTADO")
            ).where(mPredicateList.toArray(new Predicate[0])).orderBy(mOrder);
            //     Query temp = entityManager.createQuery(tupleCriteriaQuery);
            //         String queryString = temp.unwrap(org.hibernate.query.Query.class).getQueryString().;
//            log.info(queryString);
//            var q2 = entityManager.createQuery(queryString, Tuple.class).getResultList();

            List<Tuple> mTupleList = entityManager.createQuery(tupleCriteriaQuery)
                    .setMaxResults(size).setFirstResult(firstRow).getResultList();
            if (!mTupleList.isEmpty()) {
                mTupleList.forEach(t -> {
                    usuarioTableDTOS.add(TupleUtils.buildUsuarioTableFromDTOfromTuple(t));
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

    private CriteriaQuery buildQuery(CriteriaQuery query, Root root) {
        return query.multiselect(
                root.get(UsuarioConstants.idUsuario).alias(UsuarioConstants.ID_USUARIO),
                root.get(GenericConstans.indActivo).alias(GenericConstans.IND_ACTIVO),
                root.get(UsuarioConstants.nombre).alias(UsuarioConstants.NOMBRE),
                root.get(UsuarioConstants.apellidoPaterno).alias(UsuarioConstants.APELLIDO_PATERNO),
                root.get(UsuarioConstants.apellidoMaterno).alias(UsuarioConstants.APELLIDO_MATERNO),
                root.get(UsuarioConstants.matricula).alias(UsuarioConstants.MATRICULA),
                root.get(UsuarioConstants.idEstatus).get(EstatusConstans.nombre).alias(EstatusConstans.ID_ESTATUS),
                root.get(UsuarioConstants.idPerfil).get(PerfilConstans.nombre).alias(PerfilConstans.ID_PERFIL),
                root.get(UsuarioConstants.folio).alias(UsuarioConstants.FOLIO),
                root.get(UsuarioConstants.idDireccion)
                        .get(DireccionCosntans.IDASENTAMIENTO).get(DireccionCosntans.NOMBRE).alias("ASENTAMIENTO"),
                root.get(UsuarioConstants.idDireccion)
                        .get(DireccionCosntans.IDASENTAMIENTO).get(DireccionCosntans.CODIGOPOSTAL).alias("CODIGO_POSTAL"),
                root.get(UsuarioConstants.idDireccion)
                        .get(DireccionCosntans.IDASENTAMIENTO).get(DireccionCosntans.IDMUNICIPIO).get(DireccionCosntans.NOMBRE).alias("MUNICIPIO"),
                root.get(UsuarioConstants.idDireccion)
                        .get(DireccionCosntans.IDASENTAMIENTO).get(DireccionCosntans.IDMUNICIPIO)
                        .get(DireccionCosntans.IDESTADO).get(DireccionCosntans.NOMBRE).alias("ESTADO")
        );
    }

    private List<Order> buildOrder(CriteriaBuilder pBuilder, Root<UsuarioEntity> pUsaurioEntityRoot, Map<String, Object> order) {
        List<Order> mOrder = new ArrayList<>();

        if (order != null && !order.isEmpty()) {
            if (order.containsKey(FiltersConst.NOMBRE) && !Objects.equals(order.get(FiltersConst.NOMBRE), UNSELECT)) {
                boolean isAsc = order.get(FiltersConst.NOMBRE).equals(ASC);
                var path = pUsaurioEntityRoot.get(UsuarioConstants.nombre);
                mOrder.add(isAsc ? pBuilder.asc(path) : pBuilder.desc(path));
            }
            if (order.containsKey(FiltersConst.PATERNO) && !Objects.equals(order.get(FiltersConst.PATERNO), UNSELECT)) {
                boolean isAsc = order.get(FiltersConst.PATERNO).equals(ASC);
                var path = pUsaurioEntityRoot.get(UsuarioConstants.apellidoPaterno);
                mOrder.add(isAsc ? pBuilder.asc(path) : pBuilder.desc(path));
            }
            if (order.containsKey(FiltersConst.MATERNO) && !Objects.equals(order.get(FiltersConst.PATERNO), UNSELECT)) {
                boolean isAsc = order.get(FiltersConst.MATERNO).equals(ASC);
                var path = pUsaurioEntityRoot.get(UsuarioConstants.apellidoMaterno);
                mOrder.add(isAsc ? pBuilder.asc(path) : pBuilder.desc(path));
            }
            if (order.containsKey(FiltersConst.PERFIL) && !Objects.equals(order.get(FiltersConst.PERFIL), UNSELECT)) {
                boolean isAsc = order.get(FiltersConst.PERFIL).equals(ASC);
                var path = pUsaurioEntityRoot.join(UsuarioConstants.idPerfil, JoinType.INNER).get(PerfilConstans.nombre);
                mOrder.add(isAsc ? pBuilder.asc(path) : pBuilder.desc(path));
            }
            if (order.containsKey(FiltersConst.ESTATUS) && !Objects.equals(order.get(FiltersConst.ESTATUS), UNSELECT)) {
                boolean isAsc = order.get(FiltersConst.ESTATUS).equals(ASC);
                var path = pUsaurioEntityRoot.join(UsuarioConstants.estatus, JoinType.INNER).get(EstatusConstans.nombre);
                mOrder.add(isAsc ? pBuilder.asc(path) : pBuilder.desc(path));
            }
            if (order.containsKey(FiltersConst.ASENTAMINETO) && !Objects.equals(order.get(FiltersConst.ASENTAMINETO), UNSELECT)) {
                boolean isAsc = order.get(FiltersConst.ASENTAMINETO).equals(ASC);
                var path = pBuilder.upper(
                        pUsaurioEntityRoot.get(UsuarioConstants.idDireccion)
                                .get(DireccionCosntans.IDASENTAMIENTO)
                                .get(DireccionCosntans.NOMBRE)
                );
                mOrder.add(isAsc ? pBuilder.asc(path) : pBuilder.desc(path));
            }
            if (order.containsKey(FiltersConst.CODIGOPOSTAL) && !Objects.equals(order.get(FiltersConst.CODIGOPOSTAL), UNSELECT)) {
                boolean isAsc = order.get(FiltersConst.CODIGOPOSTAL).equals(ASC);
                var path = pUsaurioEntityRoot.get(
                        UsuarioConstants.idDireccion).get(DireccionCosntans.IDASENTAMIENTO).get(DireccionCosntans.CODIGOPOSTAL);
                mOrder.add(isAsc ? pBuilder.asc(path) : pBuilder.desc(path));
            }
            if (order.containsKey(FiltersConst.MUNICIPIO) && !Objects.equals(order.get(FiltersConst.MUNICIPIO), UNSELECT)) {
                boolean isAsc = order.get(FiltersConst.MUNICIPIO).equals(ASC);
                var path = pUsaurioEntityRoot.get(UsuarioConstants.idDireccion)
                        .get(DireccionCosntans.IDASENTAMIENTO)
                        .get(DireccionCosntans.IDMUNICIPIO)
                        .get(DireccionCosntans.NOMBRE);
                mOrder.add(isAsc ? pBuilder.asc(path) : pBuilder.desc(path));
            }
            if (order.containsKey(FiltersConst.ESTADO) && !Objects.equals(order.get(FiltersConst.ESTADO), UNSELECT)) {
                boolean isAsc = order.get(FiltersConst.ESTADO).equals(ASC);
                var path = pUsaurioEntityRoot.get(UsuarioConstants.idDireccion)
                        .get(DireccionCosntans.IDASENTAMIENTO)
                        .get(DireccionCosntans.IDMUNICIPIO)
                        .get(DireccionCosntans.IDESTADO)
                        .get(DireccionCosntans.NOMBRE);
                mOrder.add(isAsc ? pBuilder.asc(path) : pBuilder.desc(path));
            }
        }
        //  boolean isAsc = order.toLowerCase().equals("asc");

        return mOrder;
    }

    private List<Predicate> buildPredicates(Map<String, Object> filters, Root<UsuarioEntity> pRoot, CriteriaBuilder builder) {
        List<Predicate> mListPredicateList = new ArrayList<>();
        if (filters != null && !filters.isEmpty()) {
            if (filters.containsKey(FiltersConst.NOMBRE) && filters.get(FiltersConst.NOMBRE) != null) {
                String mStringFilter = (String) filters.get(FiltersConst.NOMBRE);
                mListPredicateList.add(builder.like(pRoot.get(UsuarioConstants.nombre), mStringFilter.toUpperCase() + "%"));
            }
            if (filters.containsKey(FiltersConst.FOLIO) && filters.get(FiltersConst.FOLIO) != null) {
                String mStringFilter = (String) filters.get(FiltersConst.FOLIO);
                mListPredicateList.add(builder.like(pRoot.get(UsuarioConstants.folio), mStringFilter.toUpperCase() + "%"));
            }
            if (filters.containsKey(FiltersConst.PATERNO) && filters.get(FiltersConst.PATERNO) != null) {
                String mStringFilter = (String) filters.get(FiltersConst.PATERNO);
                mListPredicateList.add(builder.like(pRoot.get(UsuarioConstants.apellidoPaterno), mStringFilter.toUpperCase() + "%"));
            }
            if (filters.containsKey(FiltersConst.MATERNO) && filters.get(FiltersConst.MATERNO) != null) {
                String mStringFilter = (String) filters.get(FiltersConst.MATERNO);
                mListPredicateList.add(builder.like(pRoot.get(UsuarioConstants.apellidoMaterno), mStringFilter.toUpperCase() + "%"));
            }
            if (filters.containsKey(FiltersConst.PERFIL) && filters.get(FiltersConst.PERFIL) != null) {
                String mStringFilter = (String) filters.get(FiltersConst.PERFIL);
                mListPredicateList.add(builder.like(pRoot.join(UsuarioConstants.idPerfil, JoinType.INNER).get(PerfilConstans.nombre), mStringFilter.toUpperCase() + "%"));
            }
            if (filters.containsKey(FiltersConst.ESTATUS) && filters.get(FiltersConst.ESTATUS) != null) {
                String mStringFilter = (String) filters.get(FiltersConst.ESTATUS);
                mListPredicateList.add(builder.like(pRoot.join(UsuarioConstants.estatus, JoinType.INNER).get(EstatusConstans.nombre), mStringFilter.toUpperCase() + "%"));
            }
            if (filters.containsKey(FiltersConst.ASENTAMINETO) && filters.get(FiltersConst.ASENTAMINETO) != null) {
                String mStringFilter = (String) filters.get(FiltersConst.ASENTAMINETO);
                mListPredicateList.add(
                        builder.like(
                                builder.upper(
                                        pRoot.get(UsuarioConstants.idDireccion)
                                                .get(DireccionCosntans.IDASENTAMIENTO)
                                                .get(DireccionCosntans.NOMBRE)
                                )
                                , "%" + mStringFilter.toUpperCase() + "%"
                        )
                );
            }
            if (filters.containsKey(FiltersConst.CODIGOPOSTAL) && filters.get(FiltersConst.CODIGOPOSTAL) != null) {
                String mStringFilter = (String) filters.get(FiltersConst.CODIGOPOSTAL);
                mListPredicateList.add(builder.like(pRoot.get(UsuarioConstants.idDireccion).get(DireccionCosntans.IDASENTAMIENTO).get(DireccionCosntans.CODIGOPOSTAL), "%" + mStringFilter));
            }
            if (filters.containsKey(FiltersConst.MUNICIPIO) && filters.get(FiltersConst.MUNICIPIO) != null) {
                String mStringFilter = (String) filters.get(FiltersConst.MUNICIPIO);
                mListPredicateList.add(
                        builder.like(
                                builder.upper(pRoot.get(UsuarioConstants.idDireccion)
                                        .get(DireccionCosntans.IDASENTAMIENTO)
                                        .get(DireccionCosntans.IDMUNICIPIO)
                                        .get(DireccionCosntans.NOMBRE)
                                ), "%" + mStringFilter.toUpperCase() + "%"
                        )
                );
            }
            if (filters.containsKey(FiltersConst.ESTADO) && filters.get(FiltersConst.ESTADO) != null) {
                String mStringFilter = (String) filters.get(FiltersConst.ESTADO);
                mListPredicateList.add(
                        builder.like(
                                builder.upper(pRoot.get(UsuarioConstants.idDireccion)
                                        .get(DireccionCosntans.IDASENTAMIENTO)
                                        .get(DireccionCosntans.IDMUNICIPIO)
                                        .get(DireccionCosntans.IDESTADO)
                                        .get(DireccionCosntans.NOMBRE)
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
        if (!pUsuarioEntity.getIdPerfil().getIdPerfil().equals(EPerfiles.ID_ADMIN)) {
            mStringMatricula += mSimpleDateFormat.format(new Date()).replace("/", "");
            mStringMatricula += pUsuarioEntity.getIdPlantel().getClave();
            //  mStringMatricula += this.consecutivo(pUsuarioEntity.getIdPlantel(), pUsuarioEntity.getIdCarrera());
        }
        return mStringMatricula;
    }


    private String consecutivo(PlantelEntity pPlantelEntity, CarreraEntity pCarreraEntity) {
        CriteriaBuilder mCriteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> mLongCriteriaQuery = mCriteriaBuilder.createQuery(Long.class);
        Root<UsuarioEntity> mUsuarioEntityRoot = mLongCriteriaQuery.from(UsuarioEntity.class);
        List<Predicate> predicateList = new ArrayList<>();
        predicateList.add(mCriteriaBuilder.equal(mUsuarioEntityRoot.get("idPlantel"), pPlantelEntity));
        predicateList.add(mCriteriaBuilder.equal(mUsuarioEntityRoot.get("idCarrera"), pCarreraEntity));
        mLongCriteriaQuery.select(mCriteriaBuilder.count(mUsuarioEntityRoot)).where(predicateList.toArray(new Predicate[0]));
        Long count = entityManager.createQuery(mLongCriteriaQuery).getSingleResult();
        count = count == 0 ? count++ : count;
        return String.format("%06d", count);
    }

    @Override
    public String exportatInformacion(String pFilters, String pOrders, Integer pIdUSuario) throws MsUniversidadException {
        //todo obtener el query
        Map<String, Object> mapFilters = pFilters != null ? new Gson().fromJson(pFilters, Map.class) : null;
        Map<String, Object> mapOrders = pFilters != null ? new Gson().fromJson(pOrders, Map.class) : null;
        CriteriaBuilder pCriteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> tupleCriteriaQuery = pCriteriaBuilder.createTupleQuery();
        Root<UsuarioEntity> root = tupleCriteriaQuery.from(UsuarioEntity.class);
        var prdicates = this.buildPredicates(mapFilters, root, pCriteriaBuilder);
        var orders = this.buildOrder(pCriteriaBuilder, root, mapOrders);
        tupleCriteriaQuery = this.buildQuery(tupleCriteriaQuery, root);
        tupleCriteriaQuery.orderBy(orders).where(prdicates.toArray(new Predicate[0]));
        var tupleQueryResult = this.entityManager.createQuery(tupleCriteriaQuery).getResultList();
        this.preparedData((List<Tuple>) tupleQueryResult, pIdUSuario);
        return "Se Notificara Cuando el Archivo Este listo para descargar";
    }

    @Async
    void preparedData(List<Tuple> tupleQueryResult, Integer pIdUsuario) {
        List<UsuarioTableDTO> usuarioTableDTOList = new ArrayList<>();
        JsonArray jsonArray = new JsonArray();
        tupleQueryResult.forEach(t -> {
            JsonObject jsonElement = new JsonObject();
            t.getElements().forEach(element -> {
                jsonElement.addProperty(element.getAlias(), String.valueOf(t.get(element.getAlias())));
            });
            jsonArray.add(jsonElement);
        });
        JsonObject finalJsonObject = new JsonObject();
        finalJsonObject.addProperty("UsuarioDestino", pIdUsuario);
        finalJsonObject.addProperty("usuarios", new Gson().toJson(jsonArray));
        this.rabbitTemplate.convertAndSend(this.routingKey, new Gson().toJson(finalJsonObject));
    }

}
