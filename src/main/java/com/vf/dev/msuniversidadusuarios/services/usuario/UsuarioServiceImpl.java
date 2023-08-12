package com.vf.dev.msuniversidadusuarios.services.usuario;

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
import com.vf.dev.msuniversidadusuarios.services.municipio.IMunicipioService;
import com.vf.dev.msuniversidadusuarios.services.perfil.IPerfilService;
import com.vf.dev.msuniversidadusuarios.services.plantel.IPlantelService;
import com.vf.dev.msuniversidadusuarios.utils.GeneradorDeClaves;
import com.vf.dev.msuniversidadusuarios.utils.PageUtils;
import com.vf.dev.msuniversidadusuarios.utils.UsuarioUtils;
import com.vf.dev.msuniversidadusuarios.utils.cosnts.*;
import com.vf.dev.msuniversidadusuarios.utils.exception.MsUniversidadException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;

@Service
@Slf4j
public class UsuarioServiceImpl implements IUsuarioService {
    private static final String UNSELECT = "unselect";
    private static final String ASC = "asc";
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
        List<Order> mOrder = this.buildOrder(builder, mUsuarioRoot, column, orden);
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


    private List<Order> buildOrder(CriteriaBuilder pBuilder, Root<UsuarioEntity> pUsaurioEntityRoot, String column, Map<String, Object> order) {
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
            mStringMatricula += mSimpleDateFormat.format(new Date()).replaceAll("/", "");
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

    private String[] getArray(MultipartFile file) throws IOException {
        List<String> list = new ArrayList<>();
        InputStream inputStream = file.getInputStream();
        new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines()
                .forEach(line -> {
                    if (!line.equals("")) {
                        list.add(line);
                    }
                });
        return list.toArray(new String[0]);
    }

    @Async
    public void addMasive(MultipartFile hombres, MultipartFile mujeres, MultipartFile pApellidos, Integer idEstado) throws Exception {
        GeneradorDeClaves GENERARDOR = new GeneradorDeClaves();
        var nombresHombre = this.getArray(hombres);
        var nombresMujer = this.getArray(mujeres);
        var apellidos = this.getArray(pApellidos);
        String mStrResult = "";
        // var e = this.iEstadoService.findById(idEstado);
        var estados = this.iEstadoService.getAllEstados();
        var perfil = this.iPerfilService.findById(2);
        var estatus = this.iEstatusService.findById(1);
        estados.forEach(e -> {
            try {
                Runnable task = () -> {
                    try {
                        this.addNewUser(e, perfil, estatus, nombresHombre, nombresMujer, apellidos);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                };
                Thread thread = new Thread(task);
                thread.start();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });


    }


    private void addNewUser(EstadoEntity estado, PerfilEntity perfil, EstatusEntity estatus, String[] nombresHombre, String[] nombresMujer, String[] apellidos) throws Exception {
        log.info("Inciando con el estado: " + estado.getNombre());

        var planteles = this.iPlantelService.findAllByMunicipio(estado);
        int i = 1;
        for (var plantel : planteles) {
            List<UsuarioEntity> usuarioEntities = new ArrayList<>();
            var asentamientos = this.iAsentamientoService.listAllByMunicipio(plantel.getIdMunicipio());
            for (var asentamiento : asentamientos) {

                log.info("@MSU-----> " + asentamiento.getNombre());


                for (int j = 1; j < 4; j++) {
                    try {

                        var direccion = DireccionEntity.builder()
                                .desFachada("n/a")
                                .numeroExterior("n/a")
                                .numeroInterior("b/a")
                                .idAsentamiento(asentamiento)
                                .calle("normal")
                                .build();
                        //  direccion = this.iDireccionService.save(direccion);
                        var nombre = (j % 2 == 0) ? nombresHombre[UsuarioUtils.getRamndomNumberRange(1, nombresHombre.length)] : nombresMujer[UsuarioUtils.getRamndomNumberRange(1, nombresMujer.length)];
                        var aPaterno = apellidos[UsuarioUtils.getRamndomNumberRange(1, apellidos.length)];
                        var genero = (j % 2 == 0) ? 1 : 2;
                        var desGenero = (genero == 1) ? "MASCULINO" : "FEMENINO";
                        var aMaterno = apellidos[UsuarioUtils.getRamndomNumberRange(1, apellidos.length)];
                        var fechaNacimiento = UsuarioUtils.generateRandomDate();
                        var edad = UsuarioUtils.getEdad(fechaNacimiento);
                        var random = GeneradorDeClaves.generar(4, 0, 0, 0);
                        var curp = UsuarioUtils.getCurp(nombre, aPaterno, aMaterno, fechaNacimiento, estado.getNombreAbrebiado(), random, i);
                        var carrera = this.iCarreraService.findById(UsuarioUtils.getRamndomNumberRange(1, 6));
                        var password = GeneradorDeClaves.generar(12, 30, 30, 40);
                        var usuario = UsuarioEntity.builder()
                                .nombre(nombre)
                                .apellidoMaterno(aMaterno)
                                .genero(genero)
                                .desGenero(desGenero)
                                .apellidoPaterno(aPaterno)
                                .correo(curp.concat("@gmail.com"))
                                .curp(curp)
                                .fechaNacimiento(fechaNacimiento)
                                .edad(edad)
                                .idDireccion(direccion)
                                .idPerfil(perfil)
                                .idPlantel(plantel)
                                .idEstatus(estatus)
                                .idCarrera(carrera)
                                .build();
                        usuario.setMatricula(this.buildMatricula(usuario) + curp);
                        usuario.setNombreUsuario(usuario.getCurp().concat(usuario.getMatricula()));
                        usuario.setFechaAlta(new Date());
                        usuario.setIndActivo(true);
                        usuario.setPassword(this.passwordEncoder.encode(password));
                        //  log.info(+j +"asentamiento: " + asentamiento.getNombre() + " " + " Usuario creado: " + " nombreUsuario: " + usuario.getNombreUsuario() + " password: " + password);
                        usuarioEntities.add(usuario);
                        //   this.save(usuario);
                    } catch (Exception e) {
                        log.info(e.getMessage());
                    }
                }


                //   this.addAll(usuarioEntities);
                //    log.info("guardando: " + usuarioEntities.size() + " del estado: " + estado.getNombre());
            }

            log.info("guardando: " + usuarioEntities.size());
            this.addAll(usuarioEntities);
            //  this.addAll(usuarioEntities);
        }

    }

    @Async
    public void addAll(List<UsuarioEntity> list) {
        this.iUsuarioRepository.saveAll(list);
        log.info("Guardados");
    }
}
