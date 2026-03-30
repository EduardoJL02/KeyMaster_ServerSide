<div align="center">
  <img src="src/main/resources/docs/assets/logo.png" alt="KeyMaster Center Logo" width="500"/>

  # KeyMaster Center
  
  **Sistema Integral de Gestión y Trazabilidad de Llaves Físicas**

  [![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.oracle.com/java/)
  [![JavaFX](https://img.shields.io/badge/JavaFX-UI-007396?style=for-the-badge&logo=java&logoColor=white)](https://openjfx.io/)
  [![Spring Boot](https://img.shields.io/badge/Spring_Boot-Backend-6DB33F?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/projects/spring-boot)
  [![MySQL](https://img.shields.io/badge/MySQL-Database-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
</div>

---

## Descripción del Proyecto

**KeyMaster Center** es una aplicación de escritorio diseñada para digitalizar, controlar y auditar el préstamo y devolución de llaves en centros educativos. Desarrollado como Proyecto Intermodular para el Ciclo Formativo de Grado Superior en Desarrollo de Aplicaciones Multiplataforma (DAM) en el **IES San Juan de la Cruz** (Curso 2025/2026).

Este sistema elimina los registros manuales en papel, previniendo pérdidas, controlando los tiempos de préstamo y garantizando la trazabilidad de cada movimiento mediante un control de acceso basado en roles (RBAC).

## Funcionalidades Principales

* **Autenticación y RBAC:** Acceso seguro diferenciando roles (Administrador/Jefatura vs. Conserje) para proteger módulos sensibles.
* **Dashboard Interactivo:** Panel de control en tiempo real con métricas de llaves totales, disponibles y préstamos activos.
* **Gestión de Inventario (CRUD):** Control total sobre el catálogo de llaves, espacios asignados y estados operativos (Disponible, En Uso, Perdida, Mantenimiento).
* **Gestión del Personal:** Directorio de docentes y personal autorizado con validación estricta de datos (DNI, Email) y borrado lógico (Soft Delete) para mantener el historial.
* **Control de Préstamos Activos:** Registro de salidas y entradas con cálculo de estado en tiempo real (A Tiempo / Vencido).
* **Generación de Informes:** Auditoría detallada con exportación a PDF (JasperReports), Excel y CSV.

## Arquitectura y Diseño

El proyecto sigue una arquitectura estricta para garantizar un código limpio, mantenible y escalable (**Clean Code**):

* **Frontend (Cliente Pobre):** Desarrollado en **JavaFX** implementando el patrón **MVVM (Model-View-ViewModel)**. 
    * *Views:* Definidas en archivos `.fxml` (Scene Builder). Cero lógica de negocio.
    * *ViewModels:* Gestionan el estado de la UI mediante `Properties` y `Bindings`.
    * *Servicios:* Clientes HTTP asíncronos (`CompletableFuture` / `Task`) para no bloquear el hilo de la interfaz gráfica (UI Thread).
* **Backend (API REST):** Desarrollado con **Spring Boot**, exponiendo endpoints seguros.
    * Gestiona la lógica de negocio, transacciones, control de concurrencia (Optimistic Locking) y acceso a datos mediante Spring Data JPA.

## Stack Tecnológico

| Entorno | Tecnologías | Herramientas Clave |
| :--- | :--- | :--- |
| **Frontend** | Java 17, JavaFX 19+ | Scene Builder, ControlsFX, Ikonli (Iconos) |
| **Backend** | Java 17, Spring Boot 3+ | Spring Web, Spring Security, Spring Data JPA, Hibernate |
| **Base de Datos** | MySQL 8.0 | MySQL Workbench |
| **Reportes** | JasperReports, PDF.js | Generación de PDFs y exportación a Excel/CSV |
| **Testing & QA**| JUnit 5, Mockito | Pruebas unitarias y de integración |

## Instalación y Ejecución

### Requisitos Previos
* [JDK 17](https://adoptium.net/) o superior.
* [MySQL 8.0](https://dev.mysql.com/downloads/mysql/) en ejecución.
* Maven instalado en el sistema.

### 1. Configuración de la Base de Datos
1.  Abre tu cliente MySQL.
2.  Crea la base de datos: `CREATE DATABASE keymaster_db;`
3.  El esquema se generará automáticamente gracias a Hibernate (configurado en `application.properties`).

### 2. Despliegue del Backend (Spring Boot)
1.  Navega a la carpeta del servidor: `cd backend`
2.  Configura tus credenciales de MySQL en `src/main/resources/application.properties`.
3.  Ejecuta el servidor: `mvn spring-boot:run`
*(El servidor se iniciará en `http://localhost:8080`)*

### 3. Ejecución del Frontend (JavaFX)
1.  Abre una nueva terminal y navega a la carpeta del cliente: `cd frontend`
2.  Compila y ejecuta la aplicación: `mvn clean javafx:run`

## Consideraciones de Seguridad Implementadas

* **Soft Delete:** Implementado en la gestión de docentes y llaves para evitar la violación de integridad referencial y mantener el historial de préstamos intacto.
* **Optimistic Locking:** Previene condiciones de carrera si dos conserjes intentan prestar la misma llave simultáneamente.
* **UI No Bloqueante:** Todas las llamadas de red (HTTP) y operaciones pesadas (generación de PDFs) se realizan en hilos secundarios para mantener la fluidez de JavaFX.

## Autor

**Eduardo Jiménez Linares**
* Estudiante de 2º DAM - IES San Juan de la Cruz
* https://github.com/EduardoJL02
---
*Proyecto Intermodular desarrollado para la convocatoria académica 2025/2026.*
