package com.oficioYa.config;

import com.oficioYa.persistence.entity.OficioEntity;
import com.oficioYa.repository.OficioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final OficioRepository oficioRepository;

    @Override
    public void run(String... args) throws Exception {
        cargarOficiosBase();
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
