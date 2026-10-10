# Plan de Trabajo Backend - Sprint 3 (OficioYa) - VERSIÓN DEFINITIVA Y DETALLADA

Este documento establece el cronograma de ejecución ultra-detallado y la división del trabajo para el equipo backend durante el Sprint 3. El objetivo es entregar el **MVP al 100%**, asegurizado con JWT, contenerizado en Docker, desplegado en Azure y documentado a la perfección.

*Nota de la Guía S3:* Los requerimientos de "Comunicación en tiempo real" (OFY-51) y "Notificaciones Push" (OFY-61) **SÍ se implementan en este Sprint, pero SIN WebSockets**. Se deben construir como endpoints REST normales (POST para enviar, GET para leer) y el frontend tendrá que hacer "Refresh/F5" para ver los cambios. La verdadera asincronía en tiempo real se dejará para el Corte 3.

---

## 👥 Equipo y Ramas (GitFlow Real)
*Para seguir buenas prácticas, **NUNCA** usen una sola rama larga por persona. Cada ticket de Jira genera su propia rama separada y se une a `develop` mediante Pull Requests.*

*   **Dev A (Seguridad, Nube e Infraestructura):** Nomenclatura sugerida: `feature/OFY-[ID]-security` o `feature/infra-docker`
*   **Dev B (Core de Negocio, Consultas Complejas y Calidad):** Nomenclatura sugerida: `feature/OFY-[ID]-business`
*   **Líder Técnico [LT] (Gestión, Arquitectura, Documentación y Módulos Menores):** Nomenclatura sugerida: `feature/OFY-[ID]-management` o `docs/readme-final`

---

## 📅 SESIÓN 1: Bases de Seguridad y Lógica Independiente (Días 1 a 4)
*Objetivo: Habilitar JWT lo antes posible para no bloquear pruebas y construir la lógica fuerte del negocio.*

*   **Dev A:**
    *   **Seguridad Integral (JWT):** Instalar `spring-boot-starter-security` y `jjwt`.
    *   **Autenticación y Roles:** Crear endpoint `POST /auth/login` (OFY-50) y "Alternar rol de usuario" (OFY-55).
    *   **Configuración CORS:** Crear `CorsConfig.java` para permitir que el frontend consuma la API (Paso 08 de la Guía).

*   **Dev B:**
    *   **Historial y estadísticas:** Consultar trabajos completados (OFY-44), Calcular ingresos (OFY-45), Servicios más solicitados (OFY-46), Trabajadores anteriores (OFY-47) y Volver a contactar (OFY-48).
    *   **Verificación de Identidad:** Verificar correo/teléfono (OFY-40), Detectar elegibilidad (OFY-41), Solicitar verificación (OFY-42) y Otorgar distintivo (OFY-43).
    *   **Mensajería Básica:** Comunicación entre trabajador y contratante (OFY-51) implementada con REST normal (POST/GET sin tiempo real).

*   **Líder Técnico (LT):**
    *   **Gestión Ágil:** Cerrar Sprint 2 en Jira, migrar deuda técnica y asignar tareas.
    *   **Autenticación y Mantenimiento:** Eliminar cuenta propia (OFY-58) y Recuperar cuenta (OFY-75).
    *   **Seguimiento de Solicitudes y Alertas:** Consultar estado (OFY-72), Cancelar servicio en curso (OFY-76), Cambiar estado del servicio (OFY-78), y Recibir Notificaciones Push (OFY-61) de manera simulada (endpoint GET para listar alertas).

---

## 📅 SESIÓN 2: Proteger la API y Preparar Contenedores (Días 5 a 10)
*Objetivo: Integrar roles en todos los controladores y empaquetar el proyecto.*

*   **Dev A:**
    *   **Autorización Estricta por Roles:** Agregar anotaciones `@PreAuthorize` en absolutamente **todos** los controladores de la aplicación.
    *   **Dockerización:** Construir `Dockerfile` optimizado, crear `docker-compose.yml` (Postgres + Mongo) y publicar la imagen en **Docker Hub**.

*   **Dev B:**
    *   **Sistema de Reputación:** Calificar trabajador (OFY-34), Calificar contratante (OFY-35), Publicar reseña (OFY-36), Calcular promedio (OFY-37), Reportar reseña (OFY-38), Pausar trabajador automático (OFY-39), Reputación por oficio (OFY-56) y Consultar por oficio (OFY-71).

*   **Líder Técnico (LT):**
    *   **Perfiles Avanzados y Referidos:** Métodos de pago (OFY-60), Consultar especializaciones (OFY-66), Editar especializaciones (OFY-67), Registrar referido (OFY-68) e Insignia referidos (OFY-69).
    *   **Moderación y Transparencia:** Moderar usuarios (OFY-54), Reportar comportamiento (OFY-64) y Consultar estado de reporte (OFY-73).

---

## 📅 SESIÓN 3: Despliegue en la Nube y Documentación Exhaustiva (Días 11 a 13)
*Objetivo: Subir el código a Azure, asegurar 96% de cobertura y dejar el repositorio impecable.*

*   **⚠️ REGLA DE CODE FREEZE:** Antes de pasar a Azure, la rama `develop` debe estar estable, la base de datos debe inicializar correctamente y `mvn test` debe pasar todo en verde.

*   **Dev A:**
    *   **Integración y Entrega Continua (CI/CD):** Crear el workflow en **GitHub Actions** para hacer test automático y despliegue en Azure App Service (Entorno de QA). Administrar secretos.

*   **Dev B:**
    *   **Auditoría de Calidad:** Asegurar mediante JaCoCo que todas las líneas de negocio nuevas tengan 96%+ de cobertura de pruebas unitarias. 
    *   **Auditoría de Swagger:** Garantizar que todos los nuevos endpoints aparezcan en Swagger e incluir configuración de seguridad (`bearerAuth`).

*   **Líder Técnico (LT):**
    *   **Diagrama de Despliegue:** Construir el diagrama oficial de infraestructura.
    *   **Construcción del Súper-README Backend:** 
        *   Título, propósito y escudo de cobertura (JaCoCo).
        *   **URLs Oficiales:** Links funcionales directos a Azure.
        *   **Matriz de Roles:** Tabla en Markdown explicando qué hace cada rol y a qué endpoints accede.
        *   **Instrucciones de Ejecución Local:** Pasos exactos con Maven y con Docker.
        *   **Variables de Entorno necesarias.**
        *   **Patrones de Diseño:** Explicación técnica de los patrones usados (Strategy, Adapter).
        *   **Imágenes incrustadas:** Modelo de BD y Diagrama de Despliegue.

---

## 📅 SESIÓN 4: Cierre, Verificación y Presentación (Día 14 y 15)
*Objetivo: Simulacros y preparación para el Sprint Review.*

*   **LT, Dev A y Dev B (Conjunto):**
    *   **Prueba de Fuego en Nube:** Conectarse al Swagger alojado en Azure y ejecutar el flujo completo real.

*   **Líder Técnico (LT) [Obligatorio]:**
    *   **Métricas Ágiles:** Consolidar métricas para la Retro.
    *   **Sprint Review Final:** Preparar la presentación oficial. Mostrar el MVP completado corriendo en Azure sin fallos de seguridad y documentado con rigor.
