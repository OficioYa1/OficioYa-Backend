# 🛠️ OficioYa — Backend API REST

> **Plataforma integral de intermediación ágil, transparente y meritocrática para trabajadores independientes y contratantes.**

[![Java 21](https://img.shields.io/badge/Java-21_LTS-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot 3.4.1](https://img.shields.io/badge/Spring_Boot-3.4.1-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Security 6](https://img.shields.io/badge/Spring_Security-6.x-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)](https://spring.io/projects/spring-security)
[![JWT](https://img.shields.io/badge/JWT-jjwt_0.11.5-black?style=for-the-badge&logo=jsonwebtokens&logoColor=white)](https://jwt.io/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15%2B-316192?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![MongoDB](https://img.shields.io/badge/MongoDB-6.x-47A248?style=for-the-badge&logo=mongodb&logoColor=white)](https://www.mongodb.com/)
[![Docker](https://img.shields.io/badge/Docker-24%2B-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
[![Azure App Service](https://img.shields.io/badge/Azure-App_Service-0078D4?style=for-the-badge&logo=microsoftazure&logoColor=white)](https://azure.microsoft.com/)
[![JaCoCo Coverage](https://img.shields.io/badge/Coverage-96%25%2B-brightgreen?style=for-the-badge&logo=target)](https://www.jacoco.org/jacoco/)
[![Build Status](https://img.shields.io/badge/CI%2FCD-GitHub_Actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white)](https://github.com/)

---

## 📑 Tabla de Contenidos

1. [Descripción General](#-descripción-general)
2. [Propuesta de Valor y Características](#-propuesta-de-valor-y-características)
3. [Entornos y Enlaces Oficiales](#-entornos-y-enlaces-oficiales)
4. [Módulos Implementados](#-módulos-implementados)
5. [Matriz de Seguridad y Roles (RBAC)](#-matriz-de-seguridad-y-roles-rbac)
6. [Arquitectura del Sistema y Diagramas UML](#-arquitectura-del-sistema-y-diagramas-uml)
   - [Diagrama de Contexto](#diagrama-de-contexto)
   - [Diagrama de Componentes General](#diagrama-de-componentes-general)
   - [Diagrama de Componentes Específico](#diagrama-de-componentes-específico)
   - [Diagrama de Clases (Dominio)](#diagrama-de-clases)
   - [Diagrama de Despliegue en la Nube](#diagrama-de-despliegue-en-la-nube-infraestructura)
7. [Patrones de Diseño Implementados](#-patrones-de-diseño-implementados)
   - [Patrones Individuales](#1-patrones-de-diseño-individuales)
   - [Patrones Combinados](#2-patrones-combinados)
8. [Arquitectura de Base de Datos (Persistencia Mixta)](#-arquitectura-de-base-de-datos-persistencia-mixta)
   - [Modelo Entidad-Relación (PostgreSQL)](#modelo-entidad-relación-postgresql)
   - [Modelo Documental (MongoDB)](#modelo-de-documentos-mongodb)
9. [Stack Tecnológico](#-stack-tecnológico)
10. [Requisitos Previos](#-requisitos-previos)
11. [Instalación y Ejecución Local](#-instalación-y-ejecución-local)
    - [Opción A: Ejecución con Maven](#opción-a-ejecución-con-maven-desarrollo-local)
    - [Opción B: Ejecución con Docker Compose](#opción-b-ejecución-con-docker-compose-entorno-completo)
12. [Variables de Entorno y Configuración](#-variables-de-entorno-y-configuración)
13. [Documentación de la API (Swagger / OpenAPI)](#-documentación-de-la-api-swagger--openapi)
14. [Estrategia de Pruebas y Calidad de Código](#-estrategia-de-pruebas-y-calidad-de-código)
15. [Integración y Despliegue Continuo (CI/CD)](#-integración-y-despliegue-continuo-cicd)
16. [Estructura del Proyecto](#-estructura-del-proyecto)
17. [Repositorios Relacionados](#-repositorios-relacionados)
18. [Equipo de Desarrollo](#-equipo-de-desarrollo)
19. [Licencia](#-licencia)

---

## 📖 Descripción General

**OficioYa** es una solución tecnológica integral orientada a dignificar, visibilizar y conectar el trabajo informal y de oficios independientes (plomeros, carpinteros, electricistas, profesores particulares, cuidadores, entre otros) con contratantes que requieren servicios rápidos, confiables y calificados.

A diferencia de las plataformas tradicionales que priorizan a los proveedores mediante esquemas pagos de publicidad (*pay-to-win*), OficioYa basa su algoritmo de coincidencia estrictamente en **cercanía geográfica, reputación real y mérito profesional**.

Este repositorio aloja el **Backend Monolítico Modular**, diseñado y desarrollado con **Spring Boot 3**, siguiendo los principios de la Arquitectura Limpia, persistencia híbrida (SQL + NoSQL), seguridad sin estado con tokens JWT y control de acceso basado en roles (RBAC).

---

## 🚀 Propuesta de Valor y Características

* 📍 **Búsqueda meritocrática geolocalizada:** Ordenamiento transparente por distancia y reputación, libre de alteraciones por pago.
* ⚡ **Modelo Broadcast de Solicitudes:** Esquema bajo demanda (estilo Rappi/Uber) donde una solicitud puede ser atendida por el trabajador disponible más cercano.
* 🛡️ **Seguridad Integral y Confianza:** Sistema progresivo de verificación de identidad, insignias por mérito y módulo activo de moderación de reportes.
* 🗄️ **Persistencia Híbrida Especializada:** Garantía transaccional ACID para contratos y usuarios en PostgreSQL, y flexibilidad BSON para portafolios fotográficos en MongoDB.
* ⏱️ **Perfiles de Rápida Adopción:** Creación de perfil profesional en menos de 5 minutos, adaptado a múltiples oficios y especialidades.
* 🎁 **Ecosistema de Fidelización y Referidos:** Mecanismos de incentivo y recompensas mediante códigos de referido únicos.

---

## 🌐 Entornos y Enlaces Oficiales

| Recurso / Entorno | Tipo | Estado | Enlace |
| :--- | :--- | :---: | :--- |
| **Swagger UI (Local)** | Documentación interactiva | 🟢 Activo | [http://localhost:8082/swagger-ui/index.html](http://localhost:8082/swagger-ui/index.html) |
| **OpenAPI Docs (Local)** | Esquema JSON | 🟢 Activo | [http://localhost:8082/v3/api-docs](http://localhost:8082/v3/api-docs) |
| **API Backend (Azure Cloud)** | Staging / QA | 🟡 En Despliegue | `[ESPACIO PENDIENTE: https://oficioya-backend.azurewebsites.net]` |
| **Swagger UI (Azure Cloud)** | Staging / QA | 🟡 En Despliegue | `[ESPACIO PENDIENTE: https://oficioya-backend.azurewebsites.net/swagger-ui/index.html]` |
| **Frontend Web (Cliente)** | Interfaz de Usuario SPA | 🟢 Repositorio | [OficioYa-Frontend](https://github.com/OficioYa1/OficioYa-Frontend) |

---

## 🧩 Módulos Implementados

El backend de OficioYa está estructurado funcionalmente en módulos desacoplados:

### 1. 🔐 Seguridad y Autenticación
* **JWT Stateless:** Generación y validación de tokens de acceso criptográficos con biblioteca JJWT.
* **Autenticación Unificada:** Inicio de sesión (`POST /auth/login`), registro y alternancia de rol operativo (`POST /auth/toggle-role`).
* **Autorización granular:** Protección declarativa con `@PreAuthorize("hasRole('...')")` o `@PreAuthorize("hasAnyRole('...')")`.

### 2. 👤 Gestión de Usuarios y Perfiles
* **Separación de Perfiles:** Composición 1 a 1 entre la cuenta de `Usuario` y los perfiles de `PerfilContratante` y `PerfilTrabajador`.
* **Carga Multimedia Local (RF-59):** Almacenamiento y procesamiento multipart para fotos de perfil en `uploads/perfiles/`.
* **Especializaciones y Tarifas:** Catálogo dinámico de habilidades, métodos de pago aceptados y tarifas por hora.
* **Mantenimiento de Cuenta:** Baja lógica / eliminación voluntaria (`DELETE /usuarios/{id}`) y recuperación (`POST /usuarios/{id}/recuperar`).

### 3. 📋 Ciclo de Vida de Solicitudes (State Pattern)
* **Gestión de Estados:** Transiciones seguras y validadas: `PENDIENTE` ➔ `ACEPTADA` ➔ `EN_CURSO` ➔ `FINALIZADA` / `CANCELADA`.
* **Modelo Broadcast:** Solicitudes creadas con `trabajadorId` en nulo para postulación o reclamo dinámico por trabajadores cercanos.
* **Cancelación y Seguimiento:** Consulta en tiempo real del estado de servicio y reglas de negocio para cancelación en curso.

### 4. 🔍 Búsqueda Avanzada y Catálogo de Oficios
* **Filtros Dinámicos (JPA Specifications):** Consultas multicriterio por oficio, zona, tarifa y disponibilidad.
* **Normalización de Catálogo:** Administración y consulta de oficios formales estandarizados.

### 5. 🛡️ Moderación, Denuncias y Seguridad de Comunidad
* **Creación de Reportes:** Permite denunciar comportamientos inadecuados entre contratantes y trabajadores (`POST /moderacion/reportes`).
* **Panel de Moderación (Admin):** Revisión de reportes pendientes, resolución o descarte (`PUT /moderacion/reportes/{id}/estado`).
* **Sanciones y Bloqueo:** Suspensión preventiva y bloqueo de usuarios infractores.

### 6. 🎁 Sistema de Referidos e Incentivos
* **Generación de Códigos Únicos:** Algoritmo de generación de código alfanumérico para invitar nuevos usuarios (`POST /referidos/generar`).
* **Validación y Canje:** Acreditación de bonificaciones de visibilidad o créditos promocionales (`POST /referidos/canjear`).

### 7. 🖼️ Portafolios Multimedia (MongoDB)
* **Modelo Documental:** Almacenamiento no estructurado de colecciones de fotografías y evidencias de trabajos previos mediante Spring Data MongoDB.
* **Patrón Referenciado:** Enlace transparente mediante el identificador numérico relacional del perfil del trabajador.

### 8. 🌟 Reputación, Reseñas y Métricas
* **Calificación Mutua:** Evaluación bilateral (Contratante ⇄ Trabajador) con puntaje (1-5 estrellas) y comentarios.
* **Cálculo de Promedios:** Ponderación automática de reputación general y por categoría de oficio.
* **Métricas de Rendimiento:** Resumen de ingresos acumulados, trabajos finalizados y servicios más solicitados.

### 9. 🪪 Verificación de Identidad Progresiva
* **Elegibilidad Automática:** Detección de cumplimiento de metas de trabajos realizados.
* **Insignias de Confianza:** Otorgamiento del distintivo de identidad verificada y auditoría documental.

### 10. 💬 Mensajería y Notificaciones (Simuladas REST)
* **Mensajería Básica:** Intercambio de mensajes contextuales asociados a una solicitud activa.
* **Bandeja de Notificaciones:** Endpoint de lectura para consulta periódica de alertas de estado.

---

## 🔒 Matriz de Seguridad y Roles (RBAC)

La API implementa un modelo de control de acceso basado en roles (Role-Based Access Control) respaldado por Spring Security. Todas las solicitudes a rutas protegidas deben adjuntar la cabecera:

```http
Authorization: Bearer <TU_TOKEN_JWT>
```

| Módulo / Recurso | Endpoint | Método | Acceso Permitido | Descripción |
| :--- | :--- | :---: | :---: | :--- |
| **Autenticación** | `/auth/**` | POST | 🔓 `PÚBLICO` | Login, registro y renovación de tokens. |
| **Documentación** | `/swagger-ui/**`, `/v3/api-docs/**` | GET | 🔓 `PÚBLICO` | Interfaz y especificación OpenAPI. |
| **Oficios** | `/oficios` | GET | 🔓 `PÚBLICO` | Consulta del catálogo maestro de oficios. |
| **Búsqueda** | `/busqueda/**` | GET | 🔓 `PÚBLICO` | Búsqueda y filtrado general de trabajadores. |
| **Usuarios** | `/usuarios/{id}` | GET, PUT | 👤 `CONTRATANTE`, 🛠️ `TRABAJADOR`, 🛡️ `ADMIN` | Consulta y actualización de datos personales. |
| **Usuarios** | `/usuarios/{id}` | DELETE | 👤 `CONTRATANTE`, 🛠️ `TRABAJADOR` | Eliminación lógica voluntaria de cuenta. |
| **Usuarios** | `/usuarios/{id}/recuperar` | POST | 🛡️ `ADMIN` | Reactivación de cuenta desactivada. |
| **Perfil Trabajador** | `/perfil-trabajador/{id}` | GET | 🔓 `PÚBLICO` | Visualización pública del perfil profesional. |
| **Perfil Trabajador** | `/perfil-trabajador/{id}` | PUT | 🛠️ `TRABAJADOR` | Actualización de tarifas, disponibilidad y datos. |
| **Perfil Trabajador** | `/perfil-trabajador/{id}/foto` | POST | 🛠️ `TRABAJADOR` | Subida de foto de perfil (Multipart/form-data). |
| **Especializaciones** | `/perfil-trabajador/{id}/especializaciones` | GET, PUT | 🛠️ `TRABAJADOR`, 👤 `CONTRATANTE` | Gestión y consulta de especialidades. |
| **Solicitudes** | `/solicitudes` | POST | 👤 `CONTRATANTE` | Publicación de nueva solicitud de servicio. |
| **Solicitudes** | `/solicitudes/{id}` | GET | 👤 `CONTRATANTE`, 🛠️ `TRABAJADOR`, 🛡️ `ADMIN` | Detalle y seguimiento de la solicitud. |
| **Solicitudes** | `/solicitudes/{id}/estado` | PUT | 👤 `CONTRATANTE`, 🛠️ `TRABAJADOR` | Transición de estado (Aceptar, Iniciar, Finalizar, Cancelar). |
| **Moderación** | `/moderacion/reportes` | POST | 👤 `CONTRATANTE`, 🛠️ `TRABAJADOR` | Radicación de denuncias o reportes de conducta. |
| **Moderación** | `/moderacion/reportes` | GET | 🛡️ `ADMIN` | Listado y auditoría de reportes radicados. |
| **Moderación** | `/moderacion/reportes/{id}/estado` | PUT | 🛡️ `ADMIN` | Resolución administrativa de reportes. |
| **Referidos** | `/referidos/generar` | POST | 👤 `CONTRATANTE`, 🛠️ `TRABAJADOR` | Creación de código único de recomendación. |
| **Referidos** | `/referidos/canjear` | POST | 👤 `CONTRATANTE`, 🛠️ `TRABAJADOR` | Aplicación de código de referido en la cuenta. |
| **Reputación** | `/reputacion/**` | POST, GET | 👤 `CONTRATANTE`, 🛠️ `TRABAJADOR` | Calificaciones bilaterales y consulta de promedios. |
| **Estadísticas** | `/historial-estadisticas/**` | GET | 🛠️ `TRABAJADOR`, 🛡️ `ADMIN` | Métricas de ingresos y servicios completados. |
| **Portafolios** | `/portafolios/**` | GET, POST | 🛠️ `TRABAJADOR`, 👤 `CONTRATANTE` | Gestión de portafolio fotográfico en MongoDB. |

---

## 🏛️ Arquitectura del Sistema y Diagramas UML

El sistema está concebido bajo una **Arquitectura en Capas Lógicas (Multicapa)**, respetando la Inversión de Dependencias (IoC), la segregación de interfaces y la modularidad de dominio.

### Diagrama de Contexto

El Diagrama de Contexto ilustra el ecosistema macro de OficioYa, situando la plataforma como el puente de interacción entre el Contratante, el Trabajador Independiente y el Administrador, junto con sus integraciones externas (Mapbox, Firebase FCM, Cloudinary) y su persistencia híbrida.

![Diagrama de Contexto](docs/uml/DiagramContext.png)

---

### Diagrama de Componentes General

Expone la macroarquitectura Cliente-Servidor del sistema, evidenciando la separación entre la aplicación web (SPA), el monolito modular desarrollado en Spring Boot 3, el motor de persistencia relacional y el almacenamiento local de archivos estáticos.

![Diagrama de Componentes General](docs/images/DiagramGeneralComponent.png)

---

### Diagrama de Componentes Específico

Ilustra la arquitectura de "caja blanca" interna del backend y el flujo de llamadas entre capas lógicas:
1. **Controladores REST (`@RestController`):** Recepción y validación inicial de peticiones.
2. **Capa de Mapeo (MapStruct):** Conversión desacoplada entre Request/Response DTOs y Entidades.
3. **Capa de Servicios de Negocio (`@Service`):** Orquestación transaccional regida por interfaces formales.
4. **Capa de Validadores de Dominio:** Encapsulamiento de reglas estrictas de negocio.
5. **Capa de Persistencia (Spring Data JPA / MongoDB):** Abstracción de acceso a datos y consultas dinámicas con Criteria API.

![Diagrama de Componentes Específico](docs/images/DiagramEspecificComponent.png)

---

### Diagrama de Clases

Expone la estructura estática del modelo de dominio de OficioYa. La clase raíz `Usuario` compone los roles operativos (`PerfilContratante` y `PerfilTrabajador`), permitiendo la doble funcionalidad sin duplicidad de credenciales. La entidad `Solicitud` actúa como el corazón transaccional, orquestando el ciclo de vida del servicio.

![Diagrama de Clases](docs/images/DiagramClass.png)

<details>
  <summary><b>🔍 Ver detalle seccionado del Diagrama de Clases (Partes 1 a 5)</b></summary>
  <br>

  ![Parte 1](docs/uml/DiagramClassPart1.png)
  ![Parte 2](docs/uml/DiagramClassPart2.png)
  ![Parte 3](docs/uml/DiagramClassPart3.png)
  ![Parte 4](docs/uml/DiagramClassPart4.png)
  ![Parte 5](docs/uml/DiagramClassPart5.png)

</details>

---

### Diagrama de Despliegue en la Nube (Infraestructura)

![Diagrama de Despliegue](docs/uml/DiagramDeployment.png)

**Topología de Infraestructura:**
* **Cliente Web:** Navegador cliente ejecutando la SPA.
* **Azure App Service (Linux Web App for Containers):** Aloja el contenedor Docker con la aplicación Spring Boot compilada bajo Java 21 LTS (`oficioya-backend`).
* **Azure Database for PostgreSQL (Flexible Server):** Instancia gestionada para persistencia de datos relacionales, transacciones ACID y esquemas relacionales.
* **MongoDB Atlas / Contenedor NoSQL:** Cluster gestionado de documentos para portafolios y multimedia dinámica.
* **Servicios Cloud Complementarios:** Mapbox API (Geocodificación y cálculo de rutas), Firebase Cloud Messaging (Push notifications) y Cloudinary (CDN de activos binarios).

---

## 🎨 Patrones de Diseño Implementados

La solución implementa rigurosamente los patrones de diseño del catálogo GoF para garantizar alta cohesión, bajo acoplamiento y extensibilidad:

### 1. Patrones de Diseño Individuales

#### Patrón Creacional: Builder
* **Problema:** Los perfiles profesionales son heterogéneos según el oficio (un profesor requiere materias y niveles, un técnico marcas específicas y un plomero certificaciones de tuberías). Un constructor convencional generaría atributos nulos o constructores telescópicos inmanejables.
* **Solución:** Implementación de Builder en `PerfilTrabajador`, construyendo el perfil paso a paso según el oficio seleccionado.
* **Sustento Técnico:** Cumple con el Principio de Responsabilidad Única (SRP) y viabiliza el requerimiento funcional de creación ágil de perfiles en menos de 5 minutos desde dispositivos móviles.

#### Patrón de Comportamiento: Observer
* **Problema:** Al emitirse una solicitud de servicio urgente o programada, múltiples componentes deben reaccionar (notificar a trabajadores cercanos, actualizar tableros, registrar auditoría) sin acoplar la creación de la solicitud con cada canal.
* **Solución:** La `SolicitudServicio` emite eventos (*Publisher*) que son consumidos por observadores independientes (*Subscribers*) encargados de la entrega de alertas (ej. Firebase Cloud Messaging).
* **Sustento Técnico:** Cumple con el Principio Abierto/Cerrado (OCP); permite conectar canales adicionales (WhatsApp, correo o SMS) sin tocar la lógica del núcleo de solicitudes.

#### Patrón de Comportamiento: Strategy
* **Problema:** El sistema maneja múltiples algoritmos de búsqueda y ordenamiento: búsqueda por mérito y cercanía, búsqueda de urgencias ('disponible ahora') y búsqueda por especialidades, con la restricción contractual de que jamás se ordenará por pago publicitario.
* **Solución:** Encapsulamiento de las variantes algorítmicas en estrategias independientes (`BusquedaPorMeritoStrategy`, `BusquedaUrgenciaStrategy`).
* **Sustento Técnico:** Elimina estructuras condicionales anidadas en la capa de servicios y blinda la regla de negocio de posicionamiento meritocrático.

#### Patrón Estructural: Decorator
* **Problema:** Los trabajadores evolucionan dinámicamente: obtienen distintivos como 'Identidad Verificada' tras alcanzar 10 trabajos satisfactorios, o adquieren suscripciones 'Pro' con estadísticas avanzadas.
* **Solución:** Envoltura dinámica del objeto base (`PerfilBasico`) con decoradores (`PerfilVerificadoDecorator`, `PerfilProDecorator`) que añaden responsabilidades en tiempo de ejecución.
* **Sustento Técnico:** Evita la explosión combinatoria de subclases (`UsuarioBasico`, `UsuarioVerificadoPro`, etc.) y simplifica la monetización futura sin impactar la base gratuita.

#### Patrón Comportamental: Chain of Responsibility
* **Problema:** Al buscar un profesional en un sector exacto sin coincidencias, la plataforma debe ampliar gradualmente el radio geográfico (Barrio exacto ➔ Localidad / Sector ➔ Ciudad metropolitana).
* **Solución:** Cadena de eslabones (`HandlerBarrioExacto` ➔ `HandlerRadioAmpliado` ➔ `HandlerCiudad`) que procesan la solicitud o la delegan al siguiente nivel si la lista está vacía.
* **Sustento Técnico:** Reduce el acoplamiento y permite reconfigurar o añadir nuevos umbrales de búsqueda de manera transparente.

#### Patrón Comportamental: State
* **Problema:** El ciclo de vida de una `Solicitud` (`PENDIENTE` ➔ `ACEPTADA` ➔ `EN_CURSO` ➔ `FINALIZADA` / `CANCELADA`) exige transiciones estrictas. Validarlas con bloques `if/else` conduce a estados corruptos (ej. cancelar un servicio ya liquidado).
* **Solución:** La entidad `Solicitud` delega sus transiciones a jerarquías polimórficas que extienden de `AbstractSolicitudState`. La clase `SolicitudStateFactory` valida y orquesta los cambios de estado legales.
* **Sustento Técnico:** Lanza excepciones explícitas (`EstadoInvalidoException`) ante transiciones ilegales, blindando la integridad del dominio.

#### Patrón Estructural: Adapter
* **Problema:** El sistema requiere consumir pasarelas de pago y proveedores de cartografía externos. Conectar el código directamente a sus SDKs ata la arquitectura a APIs volátiles de terceros.
* **Solución:** Definición de interfaces de dominio internas (puertos) e implementación de adaptadores que traducen las llamadas al formato específico del proveedor.
* **Sustento Técnico:** Desacoplamiento total; permite alternar proveedores o inyectar mocks durante pruebas automatizadas sin alterar las reglas de negocio.

#### Patrón Estructural: Composite
* **Problema:** Los usuarios filtran simultáneamente por combinaciones arbitrarias de Categoría, Rango Tarifario, Reputación y Cobertura.
* **Solución:** Cada filtro implementa una especificación común (`ISpecification`). El Composite compone estos criterios mediante operadores lógicos (`AND` / `OR`) integrados con la API Criteria de JPA.
* **Sustento Técnico:** Garantiza consultas SQL nativas altamente eficientes en lugar de iteraciones pesadas en memoria, evitando sentencias condicionales dispersas.

---

### 2. Patrones Combinados

#### Builder + Decorator
* **Justificación:** Resuelve de forma limpia la creación y posterior evolución de perfiles profesionales.
* **Mecánica:** El **Builder** ensambla la entidad inicial paso a paso según el oficio seleccionado (evitando constructores gigantes con atributos nulos). Posteriormente, el **Decorator** envuelve en memoria al perfil base para conferirle atributos dinámicos (insignias, permisos premium) a medida que acumula mérito en la plataforma.

#### Observer + Strategy + Factory
* **Justificación:** Brinda un canal de notificaciones desacoplado y reactivo de nivel empresarial.
* **Mecánica:** Al cambiar el estado de una solicitud, el servicio emite un evento genérico mediante **Observer**. Un listener intercepta el evento y utiliza una **Factory** para determinar el canal preferido por el destinatario (Push, SMS o Correo), la cual entrega una **Strategy** concreta que ejecuta el método de envío correspondiente.

---

## 🗄️ Arquitectura de Base de Datos (Persistencia Mixta)

El proyecto OficioYa segmenta su persistencia para aprovechar de manera óptima las fortalezas de los modelos relacional y no relacional:

| Motor de Base de Datos | Tipo | Entidades y Datos Almacenados | Justificación Técnica |
| :--- | :--- | :--- | :--- |
| **PostgreSQL 15+** | Relacional (SQL) | Usuarios, Perfiles (Contratante y Trabajador), Oficios, Solicitudes de Servicio, Moderación/Reportes y Referidos. | Cumplimiento estricto de propiedades **A.C.I.D.** para acuerdos transaccionales, consistencia de datos, integridad referencial y llaves foráneas. |
| **MongoDB 6+** | NoSQL (Documentos) | Portafolios multimedia, galerías fotográficas y evidencias de trabajos anteriores. | Esquema dinámico y flexible de documentos BSON, almacenamiento ágil de colecciones dinámicas y consultas directas sin penalizaciones por `JOIN`. |

Para conectar ambas bases de datos se utiliza el **Patrón Referenciado**: dentro de la colección de portafolios en MongoDB se almacena el campo `perfilTrabajadorId`, el cual actúa como referencia lógica hacia la entidad del perfil en PostgreSQL, manteniendo el desacoplamiento y la alta velocidad de lectura.

### ¿Por qué PostgreSQL para el Core?
* **Cumplimiento ACID estricto:** Una solicitud de servicio constituye un acuerdo contractual entre dos usuarios con implicaciones económicas y de reputación. Si ocurre un fallo en una transición o asignación, PostgreSQL garantiza que la transacción se revierte por completo (*rollback*).
* **Modelo Broadcast:** Permite registrar solicitudes con `contratante_id` obligatorio y `trabajador_id` inicialmente nulo, dejándolas disponibles en el mercado para ser tomadas concurrentemente por trabajadores cercanos.

### ¿Por qué MongoDB para los Portafolios?
* **Esquema dinámico y liviano:** Los portafolios varían ampliamente según el oficio (fotos, enlaces, metadatos).
* **Eficiencia en BSON:** Evita tablas accesorias y consultas complejas `JOIN` en el motor relacional.
* **Integración transparente:** El documento se vincula al perfil relacional mediante su identificador numérico.

---

### Modelo Entidad-Relación (PostgreSQL)

![Modelo Entidad Relación](docs/uml/DiagramEntityRelation.png)

#### Explicación
Para entender este esquema físico, la tabla `USUARIOS` se ubica en el centro del modelo. Al registrarse, se asocia automáticamente un perfil de contratante (relación 1:1) y, de forma opcional, un perfil de trabajador si el usuario desea ofrecer sus servicios. Las tablas accesorias gestionan colecciones de datos como `PERFIL_DISPONIBILIDAD` y `PERFIL_METODOS_PAGO`. La relación de muchos a muchos entre trabajadores y oficios se resuelve con la tabla intermedia `TRABAJADOR_OFICIOS`. Finalmente, la tabla de `SOLICITUDES` se vincula al usuario permitiendo el modelo broadcast con trabajador asignable dinámicamente.

---

### Modelo de Documentos (MongoDB)

![Modelo Documental MongoDB](docs/uml/DiagramModel-Document.png)

#### Explicación
Este modelo en MongoDB almacena el historial y portafolio fotográfico de los trabajadores como arreglos de documentos BSON aislados. Mediante el patrón referenciado (`perfilTrabajadorId`), se crea el enlace lógico hacia el perfil profesional en PostgreSQL sin necesidad de sobrecargar el esquema relacional con tablas de imágenes.

---

## 💻 Stack Tecnológico

| Capa / Dominio | Tecnología | Versión | Propósito |
| :--- | :--- | :---: | :--- |
| **Lenguaje** | Java | 21 LTS | Plataforma de desarrollo principal con virtual threads y tipos modernos. |
| **Framework Base** | Spring Boot | 3.4.1 | Orquestación general, inyección de dependencias y autoconfiguración. |
| **Seguridad** | Spring Security | 6.x | Control de acceso, filtros de seguridad y autorización por métodos. |
| **Tokens Criptográficos**| JJWT (io.jsonwebtoken) | 0.11.5 | Generación, firma HMAC-SHA y validación de tokens JWT sin estado. |
| **Persistencia SQL** | Spring Data JPA / Hibernate | 3.4.1 | Mapeo objeto-relacional y abstracción de repositorios relacionales. |
| **Base de Datos SQL** | PostgreSQL | 15+ / 16 | Motor de base de datos relacional transaccional para producción/staging. |
| **BD En Memoria** | H2 Database | 2.x | Persistencia rápida en memoria para testing y desarrollo local aislado. |
| **Persistencia NoSQL**| Spring Data MongoDB | 3.4.1 | Manejo de documentos y colecciones no estructuradas de portafolios. |
| **Base de Datos NoSQL**| MongoDB | 6.x | Motor de documentos BSON de alto rendimiento. |
| **Mapeo de DTOs** | MapStruct | 1.5.5 | Generación de mapeadores tipados en tiempo de compilación. |
| **Boilerplate Reduction**| Project Lombok | 1.18.48 | Eliminación de getters, setters, constructores y builders repetitivos. |
| **Documentación** | Springdoc OpenAPI UI | 2.8.4 | Generador automático de interfaz Swagger UI 3 y esquemas OpenAPI. |
| **Pruebas Unitarias** | JUnit 5 & Mockito | Integrado | Framework de pruebas unitarias y creación de dobles de prueba (mocks). |
| **Auditoría Cobertura** | JaCoCo Maven Plugin | 0.8.11 | Auditoría de cobertura con umbral mínimo obligatorio del 96%. |
| **Contenedores** | Docker & Docker Compose | 24+ / 3.8 | Empaquetamiento de servicios en contenedores multicapa optimizados. |
| **Nube / Hosting** | Microsoft Azure App Service | Linux | Plataforma como Servicio (PaaS) para despliegue de contenedores. |

---

## 📋 Requisitos Previos

Antes de ejecutar el proyecto localmente, asegúrate de contar con:

* **Java Development Kit (JDK):** Versión 21 o superior ([Eclipse Temurin](https://adoptium.net/) recomendado).
* **Apache Maven:** Versión 3.9 o superior (o utilizar el wrapper `./mvnw` incluido).
* **Git:** Para clonar el repositorio y gestionar ramas.
* **Docker & Docker Compose (Recomendado):** Para levantar PostgreSQL y MongoDB con un solo comando.
* *(Opcional)* Clientes de base de datos como DBeaver, pgAdmin y MongoDB Compass.

---

## ⚙️ Instalación y Ejecución Local

### Paso 1: Clonar el Repositorio

```bash
git clone https://github.com/OficioYa1/OficioYa-Backend.git
cd OficioYa-Backend
```

---

### Opción A: Ejecución con Maven (Desarrollo Local)

#### 1. Iniciar las Bases de Datos (PostgreSQL & MongoDB)
Si dispones de Docker instalado, puedes iniciar únicamente los motores de base de datos mediante:

```bash
docker compose up -d postgres mongodb
```

> **Credenciales por defecto:**
> * **PostgreSQL:** Puerto `5432` | DB: `oficioyadb` | Usuario: `root` | Clave: `rootpassword`
> * **MongoDB:** Puerto `27017` | DB: `oficioyadb_nosql` | Usuario: `root` | Clave: `rootpassword`

#### 2. Compilar y Ejecutar el Backend
Utiliza el Maven Wrapper para compilar y ejecutar la aplicación en el puerto `8082`:

```bash
# En Windows (PowerShell / CMD)
.\mvnw.cmd clean spring-boot:run

# En Linux / macOS
./mvnw clean spring-boot:run
```

---

### Opción B: Ejecución con Docker Compose (Entorno Completo)

Para levantar la solución completa (PostgreSQL, MongoDB y el Backend de Spring Boot contenerizado):

```bash
# Construir la imagen y levantar todos los servicios en segundo plano
docker compose up --build -d

# Consultar los logs de la aplicación
docker compose logs -f backend

# Detener todos los servicios
docker compose down
```

Una vez levantado, la aplicación estará accesible en:
* **Backend API:** `http://localhost:8080` (en Docker Compose) o `http://localhost:8082` (ejecución directa con Maven).
* **Swagger UI:** `http://localhost:8080/swagger-ui/index.html` o `http://localhost:8082/swagger-ui/index.html`.

---

## 🔧 Variables de Entorno y Configuración

El proyecto permite sobreescribir sus propiedades principales mediante variables de entorno en el sistema o en el archivo de despliegue:

| Variable de Entorno | Valor por Defecto (`application.properties`) | Valor en `docker-compose.yml` | Descripción |
| :--- | :--- | :--- | :--- |
| `SERVER_PORT` | `8082` | `8080` | Puerto HTTP expuesto por el servidor embebido Tomcat. |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/oficioya_db` | `jdbc:postgresql://postgres:5432/oficioyadb` | Cadena de conexión JDBC a PostgreSQL. |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | `root` | Usuario con permisos en la base de datos relacional. |
| `SPRING_DATASOURCE_PASSWORD` | `postgres` | `rootpassword` | Contraseña del usuario de PostgreSQL. |
| `SPRING_DATA_MONGODB_URI` | `mongodb://localhost:27017/oficioya_mongo_db` | `mongodb://root:rootpassword@mongodb:27017/oficioyadb_nosql?authSource=admin` | URI de conexión a la instancia de MongoDB. |
| `JWT_SECRET` | *(Clave por defecto interna)* | `[ESPACIO/SECRETO EN NUBE]` | Clave secreta para la firma y validación de tokens HMAC. |
| `JWT_EXPIRATION_MS` | `86400000` (24 horas) | `86400000` | Tiempo de vida del token de acceso en milisegundos. |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `update` | `update` | Modo de gestión de esquema DDL de Hibernate. |

---

## 📚 Documentación de la API (Swagger / OpenAPI)

El backend incorpora **Springdoc OpenAPI 3**, el cual genera y actualiza en tiempo real la documentación interactiva de todos los controladores y DTOs expuestos.

### Acceso a la Interfaz Gráfica
* **Swagger UI:** [http://localhost:8082/swagger-ui/index.html](http://localhost:8082/swagger-ui/index.html)
* **Especificación JSON OpenAPI:** [http://localhost:8082/v3/api-docs](http://localhost:8082/v3/api-docs)

### Flujo de Prueba de Endpoints Protegidos en Swagger UI
1. Dirígete a la sección `auth-controller` y ejecuta la petición `POST /auth/login` con credenciales válidas.
2. Copia la cadena del campo `token` que retorna la respuesta JSON.
3. En la parte superior derecha de la interfaz de Swagger UI, haz clic en el botón verde **Authorize 🔓**.
4. En el campo emergente, ingresa el prefijo y el token:
   ```text
   Bearer TU_TOKEN_JWT_AQUI
   ```
5. Haz clic en **Authorize** y luego en **Close**. Ahora todas las peticiones a endpoints con `@PreAuthorize` enviarán la cabecera de autenticación automáticamente.

---

## 🧪 Estrategia de Pruebas y Calidad de Código

El proyecto sigue una política rigurosa de calidad de software y pruebas automatizadas:

### Ejecución de Pruebas Unitarias
Para correr la suite de pruebas con JUnit 5 y Mockito:

```bash
.\mvnw.cmd test
```

### Verificación de Cobertura con JaCoCo (Umbral 96%+)
El archivo `pom.xml` tiene configurada una regla de calidad estricta mediante el plugin `jacoco-maven-plugin`: la compilación falla automáticamente si la cobertura general de instrucciones cae por debajo del **96%**:

```bash
# Validar la regla de cobertura mínima
.\mvnw.cmd jacoco:check

# Generar el reporte interactivo HTML de JaCoCo
.\mvnw.cmd jacoco:report
```

Una vez generado el reporte, puedes inspeccionar el desglose detallado abriendo en tu navegador el archivo:
`target/site/jacoco/index.html`

> **Evidencia Gráfica de Cobertura:**
> 
> ![cobertura jacoco](docs/images/coberturaJacoco.png)

---

## 🚀 Integración y Despliegue Continuo (CI/CD)

El repositorio implementa un flujo automatizado de CI/CD mediante **GitHub Actions** en el archivo [`.github/workflows/azure-deploy.yml`](.github/workflows/azure-deploy.yml), el cual se activa ante cualquier *push* o *pull request* a las ramas `develop` y `main`:

### Etapas del Pipeline de CI/CD:
1. **Build & Test:**
   - Configuración del entorno con JDK 21 Temurin.
   - Compilación completa y ejecución de suite de pruebas unitarias (`./mvnw clean test`).
   - Verificación estricta del umbral de cobertura JaCoCo (`./mvnw jacoco:check >= 96%`).
2. **Docker Build & Push:**
   - Autenticación segura en Docker Hub.
   - Construcción de imagen de contenedor optimizada multicapa basada en Alpine JRE 21.
   - Publicación de la imagen con tags versionados (`${{ github.sha }}`) y `latest`.
3. **Despliegue Continuo a Azure:**
   - Despliegue automatizado hacia **Azure Web App for Containers** mediante perfil de publicación seguro.

### Secretos Requeridos en GitHub Actions
Para el correcto aprovisionamiento del pipeline, se requieren los siguientes secretos en el repositorio:
* `DOCKER_USERNAME`: Usuario oficial de Docker Hub.
* `DOCKER_PASSWORD`: Token de acceso personal o clave de Docker Hub.
* `AZURE_WEBAPP_PUBLISH_PROFILE`: Perfil de publicación XML descargado desde el portal de Microsoft Azure.

---

## 📂 Estructura del Proyecto

```text
OficioYa-Backend/
├── .github/
│   └── workflows/
│       └── azure-deploy.yml         # Pipeline automatizado de CI/CD
├── docs/                            # Documentación de diseño y diagramas UML
│   ├── images/                      # Diagramas de componentes y clases
│   └── uml/                         # Diagramas de contexto, entidad-relación y documentos
├── src/
│   ├── main/
│   │   ├── java/com/oficioya/
│   │   │   ├── config/              # Configuraciones de Swagger, CORS y beans globales
│   │   │   ├── controller/          # Controladores REST (@RestController)
│   │   │   │   └── docs/            # Interfaces OpenAPI y documentación de endpoints
│   │   │   ├── exception/           # Excepciones de negocio y GlobalExceptionHandler
│   │   │   ├── model/
│   │   │   │   ├── dto/             # Objetos de transferencia (Requests y Responses)
│   │   │   │   └── mapper/          # Mapeadores compilados con MapStruct
│   │   │   ├── persistence/
│   │   │   │   ├── document/        # Documentos NoSQL (MongoDB - Portafolios)
│   │   │   │   └── entity/          # Entidades relacionales JPA (PostgreSQL)
│   │   │   ├── repository/          # Repositorios Spring Data JPA y Mongo
│   │   │   ├── security/            # Configuración de Spring Security, filtros JWT y provider
│   │   │   ├── service/             # Interfaces de servicio e implementaciones de negocio
│   │   │   └── validator/           # Validadores desacoplados de reglas de negocio
│   │   └── resources/
│   │       ├── application.properties # Configuración principal de la aplicación
│   │       └── static/uploads/        # Almacenamiento local de archivos estáticos
│   └── test/java/com/oficioya/      # Pruebas unitarias de controladores y servicios
├── uploads/perfiles/                # Directorio montado para subida de fotos
├── docker-compose.yml               # Orquestación de contenedores (Postgres, Mongo, Backend)
├── Dockerfile                       # Construcción multicapa con Java 21 Temurin
├── pom.xml                          # Configuración de dependencias Maven y JaCoCo
└── README.md                        # Documentación central del repositorio
```

---

## 🔗 Repositorios Relacionados

* **Frontend Web (SPA):** [OficioYa-Frontend](https://github.com/OficioYa1/OficioYa-Frontend) — Desarrollado en React + Vite.
* **Organización del Proyecto:** [GitHub OficioYa1](https://github.com/OficioYa1)

---

## 👥 Equipo de Desarrollo

Proyecto desarrollado en el marco del curso de **Diseño y Operación de Soluciones Web (DOSW)** — Escuela Colombiana de Ingeniería Julio Garavito.

| Integrante | Correo Institucional | GitHub |
| :--- | :--- | :--- |
| **Carlos Sánchez** | `carlos.sanchez-j@mail.escuelaing.edu.co` | [@Carlossj8](https://github.com/Carlossj8) |
| **Oscar Lasso** | `oscar.lasso-m@mail.escuelaing.edu.co` | [@Oscar10lm](https://github.com/Oscar10lm) |
| **Cristian Santiago Moreno Ruiz** | `cristian.moreno-r@mail.escuelaing.edu.co` | [@cristianmorenor](https://github.com/cristianmorenor) |
| **José Alejandro Martínez Arias** | `jose.martinez-ar@mail.escuelaing.edu.co` | [@JoseMartinez883](https://github.com/JoseMartinez883) |
| **Juan Diego Gaitán** | `juan.gaitan-b@mail.escuelaing.edu.co` | [@mamagege](https://github.com/mamagege) |

---

## 📄 Licencia

Este proyecto se encuentra bajo los lineamientos y propósitos académicos del programa de Ingeniería de Sistemas de la Escuela Colombiana de Ingeniería Julio Garavito. Todos los derechos reservados © 2026 OficioYa Team.