package com.oficioya.config;

import com.oficioya.model.domain.EstadoVerificacion;
import com.oficioya.model.domain.MetodoPago;
import com.oficioya.persistence.entity.FranjaDisponibilidadEmbeddable;
import com.oficioya.persistence.entity.OficioEntity;
import com.oficioya.persistence.entity.PerfilContratanteEntity;
import com.oficioya.persistence.entity.PerfilTrabajadorEntity;
import com.oficioya.persistence.entity.RolUsuario;
import com.oficioya.persistence.entity.UsuarioEntity;
import com.oficioya.repository.OficioRepository;
import com.oficioya.repository.PerfilContratanteRepository;
import com.oficioya.repository.PerfilTrabajadorRepository;
import com.oficioya.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final OficioRepository oficioRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final PerfilTrabajadorRepository perfilTrabajadorRepository;
    private final PerfilContratanteRepository perfilContratanteRepository;
    private final Environment environment;

    public DataSeeder(OficioRepository oficioRepository) {
        this(oficioRepository, null, null, null, null, null);
    }

    @Autowired
    public DataSeeder(OficioRepository oficioRepository,
                      @Autowired(required = false) UsuarioRepository usuarioRepository,
                      @Autowired(required = false) PasswordEncoder passwordEncoder,
                      @Autowired(required = false) PerfilTrabajadorRepository perfilTrabajadorRepository,
                      @Autowired(required = false) PerfilContratanteRepository perfilContratanteRepository,
                      @Autowired(required = false) Environment environment) {
        this.oficioRepository = oficioRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.perfilTrabajadorRepository = perfilTrabajadorRepository;
        this.perfilContratanteRepository = perfilContratanteRepository;
        this.environment = environment;
    }

    @Override
    public void run(String... args) throws Exception {
        cargarOficiosBase();
        cargarUsuariosDemo();
        cargarPerfilesDemo();
    }

    private void cargarUsuariosDemo() {
        if (usuarioRepository == null) {
            return;
        }

        String encodedPass = passwordEncoder != null ? passwordEncoder.encode("password123") : "password123";

        obtenerOCrearUsuario("admin@oficioya.com", "3000000001", encodedPass, "Admin", "Sistema", RolUsuario.ADMIN);
        obtenerOCrearUsuario("trabajador@oficioya.com", "3000000002", encodedPass, "Carlos", "Plomero", RolUsuario.TRABAJADOR);
        obtenerOCrearUsuario("contratante@oficioya.com", "3000000003", encodedPass, "Laura", "Cliente", RolUsuario.CONTRATANTE);
        log.info("Usuarios base verificados/creados exitosamente (admin@oficioya.com, trabajador@oficioya.com, contratante@oficioya.com).");
    }

    private void cargarPerfilesDemo() {
        boolean isDemoActive = environment != null && (
                Arrays.asList(environment.getActiveProfiles()).contains("local") ||
                Arrays.asList(environment.getActiveProfiles()).contains("docker") ||
                Arrays.asList(environment.getActiveProfiles()).contains("prod") ||
                Boolean.parseBoolean(environment.getProperty("oficioya.seed.demo", "false"))
        );

        if (!isDemoActive) {
            log.info("DataSeeder: Perfil no es local/docker/prod. Omitiendo carga de perfiles demo de trabajadores.");
            return;
        }

        String encodedPass = passwordEncoder != null ? passwordEncoder.encode("password123") : "password123";

        // Perfil contratante
        if (perfilContratanteRepository != null && perfilContratanteRepository.count() == 0) {
            UsuarioEntity contratante = obtenerOCrearUsuario("contratante@oficioya.com", "3000000003", encodedPass, "Laura", "Cliente", RolUsuario.CONTRATANTE);
            if (contratante != null) {
                PerfilContratanteEntity perfilCont = PerfilContratanteEntity.builder()
                        .usuario(contratante)
                        .calificacionPromedio(5.0)
                        .serviciosSolicitados(2)
                        .descripcion("Contratante en Suba, Bogotá.")
                        .build();
                perfilContratanteRepository.save(perfilCont);
                log.info("Perfil de contratante demo inicializado para: Laura Cliente.");
            }
        }

        // Perfiles de trabajadores (mínimo 8 trabajadores variados - BE-05)
        if (perfilTrabajadorRepository != null && perfilTrabajadorRepository.count() == 0) {
            log.info("Inicializando los 8 perfiles de trabajadores de demostración...");

            List<FranjaDisponibilidadEmbeddable> semanaCompleta = crearFranjasSemana(true);
            List<FranjaDisponibilidadEmbeddable> lunesAViernes = crearFranjasSemana(false);

            Set<MetodoPago> metodosComunes = new HashSet<>(Arrays.asList(MetodoPago.EFECTIVO, MetodoPago.NEQUI));
            Set<MetodoPago> metodosTodos = new HashSet<>(Arrays.asList(MetodoPago.EFECTIVO, MetodoPago.NEQUI, MetodoPago.DAVIPLATA));

            // 1. Carlos Plomero
            UsuarioEntity u1 = obtenerOCrearUsuario("trabajador@oficioya.com", "3000000002", encodedPass, "Carlos", "Plomero", RolUsuario.TRABAJADOR);
            crearPerfilTrabajador(u1, "plomer", "Suba, Usaquén, Bogotá", new BigDecimal("35000.00"), 4.8, 12, true,
                    "Experto en plomería residencial, arreglo de tuberías, goteras y filtraciones críticas.",
                    "Servicio disponible con herramientas especializadas y respuesta rápida en Suba.",
                    semanaCompleta, metodosComunes);

            // 2. Andrés Electricista
            UsuarioEntity u2 = obtenerOCrearUsuario("andres.electricista@oficioya.com", "3000000004", encodedPass, "Andrés", "Electricista", RolUsuario.TRABAJADOR);
            crearPerfilTrabajador(u2, "electric", "Chapinero, Teusaquillo, Bogotá", new BigDecimal("45000.00"), 4.9, 28, true,
                    "Técnico electricista certificado CONTE. Instalaciones, cableado estructurado, cortos y tableros.",
                    "Atención inmediata para emergencias eléctricas residenciales y comerciales.",
                    semanaCompleta, metodosTodos);

            // 3. Mateo Cerrajero
            UsuarioEntity u3 = obtenerOCrearUsuario("mateo.cerrajero@oficioya.com", "3000000005", encodedPass, "Mateo", "Cerrajero", RolUsuario.TRABAJADOR);
            crearPerfilTrabajador(u3, "cerraj", "Kennedy, Puente Aranda, Bogotá", new BigDecimal("30000.00"), 4.7, 15, true,
                    "Apertura técnica de puertas sin dañar cerraduras, cambio de guardas y cerrojos de alta seguridad.",
                    "Servicio 24 horas para emergencias de apertura en el occidente y sur de Bogotá.",
                    semanaCompleta, metodosComunes);

            // 4. Jorge Carpintero
            UsuarioEntity u4 = obtenerOCrearUsuario("jorge.carpintero@oficioya.com", "3000000006", encodedPass, "Jorge", "Carpintero", RolUsuario.TRABAJADOR);
            crearPerfilTrabajador(u4, "carpint", "Fontibón, Engativá, Bogotá", new BigDecimal("40000.00"), 4.6, 9, false,
                    "Diseño, fabricación y reparación de muebles de madera, closets, puertas y acabados finos.",
                    "Presupuestos a la medida y restauración de muebles en taller o a domicilio.",
                    lunesAViernes, metodosComunes);

            // 5. Diego Pintor
            UsuarioEntity u5 = obtenerOCrearUsuario("diego.pintor@oficioya.com", "3000000007", encodedPass, "Diego", "Pintor", RolUsuario.TRABAJADOR);
            crearPerfilTrabajador(u5, "pintur", "Suba, Barrios Unidos, Bogotá", new BigDecimal("28000.00"), 4.5, 18, true,
                    "Pintor profesional de interiores y exteriores. Tratamiento de humedades, estuco y molduras.",
                    "Limpieza garantizada post-obra y aplicación de pinturas lavables de alta calidad.",
                    semanaCompleta, metodosComunes);

            // 6. Camilo Jardinero
            UsuarioEntity u6 = obtenerOCrearUsuario("camilo.jardinero@oficioya.com", "3000000008", encodedPass, "Camilo", "Jardinero", RolUsuario.TRABAJADOR);
            crearPerfilTrabajador(u6, "jardin", "Usaquén, Suba, Bogotá", new BigDecimal("32000.00"), 4.8, 22, false,
                    "Diseño y mantenimiento de jardines, poda de césped, control de plagas y fertilización orgánica.",
                    "Equipos propios de corte y fumigación para jardines residenciales en el norte de Bogotá.",
                    semanaCompleta, metodosComunes);

            // 7. María Limpieza
            UsuarioEntity u7 = obtenerOCrearUsuario("maria.limpieza@oficioya.com", "3000000009", encodedPass, "María", "Limpieza", RolUsuario.TRABAJADOR);
            crearPerfilTrabajador(u7, "limpieza", "Chapinero, Usaquén, Bogotá", new BigDecimal("25000.00"), 4.9, 34, true,
                    "Limpieza profunda residencial y oficinas, lavado de muebles y desinfección a vapor.",
                    "Insumos biodegradables y personal calificado con puntualidad garantizada.",
                    semanaCompleta, metodosTodos);

            // 8. Felipe Técnico
            UsuarioEntity u8 = obtenerOCrearUsuario("felipe.tecnico@oficioya.com", "3000000010", encodedPass, "Felipe", "Técnico", RolUsuario.TRABAJADOR);
            crearPerfilTrabajador(u8, "soporte", "Teusaquillo, Chapinero, Bogotá", new BigDecimal("50000.00"), 4.7, 16, true,
                    "Configuración de redes Wi-Fi empresariales y hogareñas, soporte a PC/Mac y cámaras de seguridad.",
                    "Soporte presencial y remoto con diagnóstico rápido y garantizado.",
                    lunesAViernes, metodosTodos);

            log.info("Los 8 perfiles de trabajadores de demostración han sido insertados exitosamente.");
        }
    }

    private UsuarioEntity obtenerOCrearUsuario(String correo, String telefono, String encodedPass, String nombre, String apellido, RolUsuario rol) {
        if (usuarioRepository == null) return null;
        return usuarioRepository.findByCorreo(correo).orElseGet(() -> {
            UsuarioEntity u = UsuarioEntity.builder()
                    .correo(correo)
                    .telefono(telefono)
                    .contrasena(encodedPass)
                    .nombre(nombre)
                    .apellido(apellido)
                    .rol(rol)
                    .correoVerificado(true)
                    .telefonoVerificado(true)
                    .activo(true)
                    .build();
            return usuarioRepository.save(u);
        });
    }

    private List<FranjaDisponibilidadEmbeddable> crearFranjasSemana(boolean incluyeFinDeSemana) {
        List<FranjaDisponibilidadEmbeddable> franjas = new ArrayList<>();
        List<DayOfWeek> dias = new ArrayList<>(Arrays.asList(
                DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY
        ));
        if (incluyeFinDeSemana) {
            dias.add(DayOfWeek.SATURDAY);
        }
        for (DayOfWeek dia : dias) {
            franjas.add(FranjaDisponibilidadEmbeddable.builder()
                    .dia(dia)
                    .horaInicio(LocalTime.of(8, 0))
                    .horaFin(LocalTime.of(18, 0))
                    .build());
        }
        return franjas;
    }

    private void crearPerfilTrabajador(
            UsuarioEntity usuario,
            String nombreOficioSubstr,
            String zona,
            BigDecimal tarifa,
            Double calificacion,
            int completados,
            boolean disponibleAhora,
            String descripcion,
            String detalles,
            List<FranjaDisponibilidadEmbeddable> franjas,
            Set<MetodoPago> metodos) {

        if (usuario == null || perfilTrabajadorRepository == null) return;

        OficioEntity oficio = oficioRepository.findAll().stream()
                .filter(o -> o.getNombre() != null && o.getNombre().toLowerCase().contains(nombreOficioSubstr.toLowerCase()))
                .findFirst()
                .orElse(null);

        PerfilTrabajadorEntity perfil = PerfilTrabajadorEntity.builder()
                .usuario(usuario)
                .zonaCobertura(zona)
                .tarifaPorHora(tarifa)
                .calificacionPromedio(calificacion)
                .trabajosCompletados(completados)
                .disponibleAhora(disponibleAhora)
                .descripcion(descripcion)
                .detallesEspecificos(detalles)
                .estadoVerificacion(EstadoVerificacion.APROBADO)
                .oficioPrincipal(oficio)
                .oficios(oficio != null ? List.of(oficio) : List.of())
                .disponibilidadSemanal(new ArrayList<>(franjas))
                .metodosPago(metodos != null ? new HashSet<>(metodos) : new HashSet<>(List.of(MetodoPago.EFECTIVO, MetodoPago.NEQUI)))
                .build();

        perfilTrabajadorRepository.save(perfil);
    }

    private void cargarOficiosBase() {
        if (oficioRepository.count() == 0) {
            log.info("La tabla de oficios está vacía. Procediendo a cargar el catálogo base...");

            List<OficioEntity> oficios = Arrays.asList(
                    // Emergencias y Reparaciones Críticas
                    new OficioEntity("Plomería", "Emergencias y Reparaciones Críticas", "Atención de fugas, destape de cañerías, instalación de grifería y sanitarios."),
                    new OficioEntity("Electricidad", "Emergencias y Reparaciones Críticas", "Solución de cortocircuitos, instalación de tomacorrientes, lámparas y revisión de cableados."),
                    new OficioEntity("Cerrajería", "Emergencias y Reparaciones Críticas", "Apertura de puertas de emergencia por pérdida de llaves, cambio de guardas e instalación de cerraduras de seguridad."),

                    // Mantenimiento y Reparación Técnica
                    new OficioEntity("Técnico de electrodomésticos (Línea Blanca)", "Mantenimiento y Reparación Técnica", "Reparación y mantenimiento de lavadoras, neveras, estufas y calentadores a gas."),
                    new OficioEntity("Técnico de línea menor", "Mantenimiento y Reparación Técnica", "Arreglo de televisores, microondas, licuadoras y equipos de sonido."),
                    new OficioEntity("Soporte tecnológico", "Mantenimiento y Reparación Técnica", "Configuración de redes Wi-Fi, mantenimiento preventivo de computadores e instalación de software."),

                    // Obra Blanca, Acabados y Espacios
                    new OficioEntity("Albañilería", "Obra Blanca, Acabados y Espacios", "Impermeabilización de techos (goteras), instalación de baldosas/enchapes y resanes de paredes."),
                    new OficioEntity("Carpintería", "Obra Blanca, Acabados y Espacios", "Reparación de sillas, camas, instalación de puertas, repisas y gabinetes a la medida."),
                    new OficioEntity("Pintura", "Obra Blanca, Acabados y Espacios", "Aplicación de pintura en interiores, fachadas y tratamiento de humedades."),
                    new OficioEntity("Armado de muebles", "Obra Blanca, Acabados y Espacios", "Ensamblaje de muebles modulares en caja (tipo Homecenter/Tugo) y soportes de pared para televisores."),
                    new OficioEntity("Jardinería", "Obra Blanca, Acabados y Espacios", "Poda de césped, mantenimiento de zonas verdes y jardines interiores."),

                    // Asistencia Cotidiana y Especializada
                    new OficioEntity("Costura y Modistería", "Asistencia Cotidiana y Especializada", "Arreglos rápidos de prendas, cambio de cierres, ruedos y ajustes a la medida."),
                    new OficioEntity("Limpieza especializada", "Asistencia Cotidiana y Especializada", "Lavado en seco de muebles, tapetes, colchones y cojinería de vehículos a domicilio."),
                    new OficioEntity("Cuidado de mascotas", "Asistencia Cotidiana y Especializada", "Paseadores de perros, baño a domicilio y cuidadores temporales."),
                    new OficioEntity("Profesores particulares", "Asistencia Cotidiana y Especializada", "Tutorías académicas por horas (matemáticas, idiomas, nivelación escolar)."),

                    // Cuidado Personal y Belleza a Domicilio
                    new OficioEntity("Peluquería y Barbería", "Cuidado Personal y Belleza a Domicilio", "Cortes de cabello para hombres, mujeres y niños, perfilado de barba, peinados y tintes en la comodidad del hogar."),
                    new OficioEntity("Manicura y Pedicura", "Cuidado Personal y Belleza a Domicilio", "Arreglo de uñas tradicional y semipermanente."),
                    new OficioEntity("Maquillaje", "Cuidado Personal y Belleza a Domicilio", "Maquillaje social, para eventos o grados.")
            );

            oficioRepository.saveAll(oficios);
            log.info("Se han insertado {} oficios exitosamente en la base de datos.", oficios.size());
        } else {
            log.info("La tabla de oficios ya contiene datos. No se requiere inicialización.");
        }
    }
}
