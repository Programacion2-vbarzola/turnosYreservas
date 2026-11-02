# Planificación de sprints — Proyecto Integrador 2026

Este documento define los sprints de trabajo para los 3 repositorios del proyecto. Está pensado para que una IA (o el propio alumno) genere issues de GitHub a partir de cada bloque "Issue".

## Cómo usar este documento

- Cada repo tiene su propia lista de sprints, **independiente de los otros dos**. Se puede avanzar más en un repo sin esperar a los demás.
- Cada issue indica su **Dependencia**:
  - `Ninguna`: se puede hacer ya mismo, sin tocar otro repo.
  - `Soft (mockeable)`: idealmente depende de otro repo, pero se puede avanzar contra un mock/stub propio sin bloquearse.
  - `Hard`: necesita que el otro repo ya tenga ese endpoint/evento funcionando de verdad (no alcanza con mock) para cerrarse del todo. Se puede empezar antes, pero no se puede dar por terminado sin eso.
- Al crear los issues en GitHub, se puede usar el título entre corchetes como prefijo (`[S0]`, `[S1]`, etc.) y labels por sprint (`sprint-0`, `sprint-1`, ...).
- Cada sprint cierra con tests del incremento agregado y actualización de la documentación correspondiente — no se posponen a un sprint final.

## Decisiones ya tomadas

- Los 2 backends usan **JHipster** (Java + Spring Boot) con **PostgreSQL** como motor servidor, esquema/usuario propio por servicio.
- **Arquitectura hexagonal** en Repo 2 y Repo 3 (ver sección siguiente). KMP usa MVVM, no aplica hexagonal.
- Comunicación Turnos → Catálogo usa **JWT técnico de servicio** (no propaga el JWT del usuario final).
- La app KMP usa **Compose Multiplatform** para la UI y **SQLDelight** para persistencia local.
- Ningún repo accede a las tablas internas de otro: toda integración es HTTP + JWT.

---

## Arquitectura hexagonal en los backends (Repo 2 y Repo 3)

Ambos backends deben seguir esta estructura de paquetes. El dominio no puede tener imports de Spring, JPA, Kafka ni HTTP.

```
src/main/java/com/vbarzola/{servicio}/
│
├── {feature}/                   ← una carpeta por feature (ej: professional, schedule, sync, user)
│   ├── domain/
│   │   ├── model/               ← POJOs puros (sin @Entity, sin @Component)
│   │   └── ports/               ← plural "ports"
│   │       ├── in/              ← una interface por caso de uso
│   │       └── out/             ← sin sufijo "Port" (ej: ProfessionalRepository)
│   ├── application/
│   │   ├── service/             ← facade @Service: delega a los use cases (no implementa port in)
│   │   ├── usecases/            ← una clase @Component por caso de uso (implementa port in)
│   │   └── exception/           ← XxxException extends RuntimeException
│   └── infrastructure/
│       ├── persistence/
│       │   ├── entity/          ← @Entity con anotaciones JPA (separadas del modelo de dominio)
│       │   ├── mapper/          ← @Component manual: toEntity() / toDomainModel()
│       │   ├── repository/      ← JpaXxxRepository extends JpaRepository<XxxEntity, Long>
│       │   └── adapter/         ← JpaXxxRepositoryAdapter implements port out
│       └── web/
│           ├── controller/      ← XxxController: inyecta XxxService + XxxDtoMapper
│           ├── dto/             ← XxxRequest (entrada) + XxxResponse (salida) — separados
│           └── mapper/          ← @Component manual: toDomain(Request) / toResponse(Domain)
│
└── config/                      ← configuración Spring, beans, seguridad
```

### Reglas que deben cumplirse (verificadas con ArchUnit):

1. Nada en `{feature}.domain` puede importar clases de `{feature}.infrastructure`
2. Nada en `{feature}.application` puede importar clases de `{feature}.infrastructure`
3. Nada en `infrastructure.web` puede importar clases de `infrastructure.persistence`
4. Los use case impls (`application.usecases`) solo usan `domain.ports.out` — nunca implementaciones concretas
5. Los port out trabajan con modelos de dominio (`domain.model`) — nunca con `@Entity`

### Verificación con ArchUnit

Ambos repos deben tener una clase de test `HexagonalArchitectureTest` que use la librería **ArchUnit** para verificar estas reglas automáticamente. Esta clase de test es obligatoria antes de continuar con Sprint 1 en cada repo.

Ejemplo de test (adaptar los nombres de features al repo correspondiente):

```java
@AnalyzeClasses(packages = "com.vbarzola.catalogo", importOptions = DoNotIncludeTests.class)
public class HexagonalArchitectureTest {

  @ArchTest
  static final ArchRule domain_no_depende_de_infrastructure = noClasses()
    .that()
    .resideInAnyPackage("..professional.domain..", "..schedule.domain..", "..sync.domain..", "..user.domain..")
    .should()
    .dependOnClassesThat()
    .resideInAnyPackage(
      "..professional.infrastructure..",
      "..schedule.infrastructure..",
      "..sync.infrastructure..",
      "..user.infrastructure.."
    );

  @ArchTest
  static final ArchRule application_no_depende_de_infrastructure = noClasses()
    .that()
    .resideInAnyPackage("..professional.application..", "..schedule.application..", "..sync.application..", "..user.application..")
    .should()
    .dependOnClassesThat()
    .resideInAnyPackage(
      "..professional.infrastructure..",
      "..schedule.infrastructure..",
      "..sync.infrastructure..",
      "..user.infrastructure.."
    );

  @ArchTest
  static final ArchRule web_no_depende_de_persistence = noClasses()
    .that()
    .resideInAnyPackage("..infrastructure.web..")
    .should()
    .dependOnClassesThat()
    .resideInAnyPackage("..infrastructure.persistence..", "..infrastructure.external..");
}

```

---

## Repo 1 — App KMP (Android)

Responsabilidad: UI + lógica cliente. Consume exclusivamente los contratos propios expuestos por Repo 2 y Repo 3. Nunca ve el JWT técnico de cátedra.

**Patrón arquitectónico: MVVM** (Model-View-ViewModel). No aplica hexagonal.

```
shared/src/commonMain/kotlin/com/vbarzola/shared/
├── {feature}/
│   ├── ui/          ← pantallas Compose (@Composable)
│   └── viewmodel/   ← ViewModels con estado observable
├── data/
│   ├── api/         ← clientes Ktor (HTTP)
│   ├── db/          ← SQLDelight
│   └── repository/  ← repositorios que combinan red + DB local
└── model/           ← modelos de dominio compartidos
```

### Sprint 0 — Scaffolding y contrato

- **[S0-KMP-1] Estructura del proyecto KMP** ✅ COMPLETADO

  - Módulos `shared` y `androidApp`, Gradle version catalog, compila y corre en emulador.
  - Dependencia: Ninguna.

- **[S0-KMP-2] Documentar contrato REST propio esperado de Repo 2 y Repo 3** ✅ COMPLETADO

  - Escrito en `docs/contracts/API_CONTRACT.md` — 14 endpoints documentados.
  - Dependencia: Ninguna.

- **[S0-KMP-3] Mock server del contrato propio** ⏭️ POSTERGADO (opcional)
  - Solo se hace si los backends se atrasan y se necesita avanzar la UI sin ellos.
  - Dependencia: Ninguna.

### Sprint 1 — Autenticación de usuario final

- **[S1-KMP-1] Pantallas de registro y login**

  - Formulario con validaciones de UI equivalentes a las del backend (login 3-50 caracteres, password 4-100, email opcional).
  - Dependencia: Soft (mockeable) — contra Repo 3 o su mock.

- **[S1-KMP-2] Almacenamiento seguro del JWT propio**

  - Persistencia cifrada del JWT de usuario final (nunca en texto plano, nunca loguearlo).
  - Dependencia: Ninguna.

- **[S1-KMP-3] Cliente HTTP Ktor con interceptor de auth**
  - Interceptor que agrega `Authorization: Bearer` a cada request protegida y maneja 401 (logout forzado, limpieza de sesión local).
  - Dependencia: Ninguna.

### Sprint 2 — Catálogo local y búsqueda

- **[S2-KMP-1] Esquema SQLDelight de catálogo**

  - Tablas para categorías, profesionales, horarios semanales + versión local aplicada.
  - Dependencia: Ninguna.

- **[S2-KMP-2] Sincronización completa (snapshot) contra Repo 2**

  - Consumir el endpoint propio de snapshot del backend de catálogo, persistir de forma consistente (todo o nada).
  - Dependencia: Soft (mockeable).

- **[S2-KMP-3] Pantalla de búsqueda y filtros**
  - Filtros obligatorios: categoría, nombre, estado habilitado, disponibilidad. Resueltos 100% contra SQLDelight local.
  - Dependencia: Ninguna.

### Sprint 3 — Disponibilidad y creación de hold

- **[S3-KMP-1] Pantalla de horarios disponibles**

  - Por profesional y fecha, consumiendo el endpoint propio de disponibilidad de Repo 3.
  - Dependencia: Soft (mockeable).

- **[S3-KMP-2] Flujo de creación de hold**
  - Selección de slot → `POST` hold propio → mostrar countdown con `expiresAt`.
  - Mapeo de errores (slot ya reservado/held, profesional deshabilitado, slot inválido) a mensajes de usuario.
  - Dependencia: Soft (mockeable).

### Sprint 4 — Confirmación asíncrona (teléfono)

- **[S4-KMP-1] Confirmar hold y esperar pedido de teléfono**

  - Llamada a confirmación inicial → estado `WAITING_FOR_PHONE`.
  - Polling a endpoint propio de estado de proceso.
  - Dependencia: Hard.

- **[S4-KMP-2] Pantalla de ingreso de teléfono**

  - Envío del teléfono al backend propio, reintentable mientras no se venza el proceso.
  - Dependencia: Soft (mockeable) para el envío; Hard para ver el resultado real.

- **[S4-KMP-3] Pantalla de resultado final del proceso**
  - Estados: confirmado, rechazado (reintentable), expirado, inválido. Tratar los estados finales como terminales en la UI.
  - Dependencia: Hard.

### Sprint 5 — Mis reservas

- **[S5-KMP-1] Listado de reservas propias**

  - Consumir el endpoint propio de Repo 3, filtrado automáticamente por el usuario logueado.
  - Dependencia: Soft (mockeable).

- **[S5-KMP-2] Cancelación de reserva confirmada**
  - Botón de cancelar con estado idempotente (deshabilitar tras click, reflejar estado actual).
  - Dependencia: Soft (mockeable).

### Sprint 6 — Robustez

- **[S6-KMP-1] Manejo centralizado de errores**

  - Mapeo de `application/problem+json` / códigos propios a mensajes de usuario.
  - Dependencia: Ninguna.

- **[S6-KMP-2] Retry/backoff acotado y estados offline**
  - Indicadores de offline/reconectando, catálogo "stale" visible cuando no se puede sincronizar.
  - Dependencia: Ninguna.

### Sprint 7 — Tests, documentación y auditoría final

- **[S7-KMP-1] Unit tests de `shared`**

  - Repositorios, mappers, máquina de estados de reserva.
  - Dependencia: Ninguna.

- **[S7-KMP-2] README y diagrama de arquitectura**

  - Pasos reproducibles, incluyendo cómo apuntar a mock vs. backends reales.
  - Dependencia: Ninguna.

- **[S7-KMP-3] Auditoría contra checklist de evidencias mínimas**
  - Verificar cobertura de los ítems de la sección 11 del enunciado.
  - Dependencia: Hard.

---

## Repo 2 — Backend de Catálogo y Sincronización

Responsabilidad: única fuente local vigente de categorías, profesionales y horarios. Sincroniza con la cátedra vía REST/Redis/Kafka. Expone contrato propio de solo lectura a Repo 3 y Repo 1.

**Patrón arquitectónico: Hexagonal (Ports & Adapters)**

### Sprint 0 — Scaffolding ✅ PARCIALMENTE COMPLETADO

> ⚠️ El Sprint 0 fue generado con JHipster usando su estructura de capas por defecto. Antes de continuar con Sprint 1, se deben completar los issues S0-CAT-5 y S0-CAT-6 para migrar a arquitectura hexagonal y verificarla.

- **[S0-CAT-1] Proyecto JHipster + entidades base** ✅ COMPLETADO

  - Entidades `ProfessionalCategory`, `Professional`, `WeeklySchedule` + tabla de versión de sincronización local.

- **[S0-CAT-2] Externalización de configuración de cátedra** ✅ COMPLETADO

  - Variables de entorno para URL REST, host/puerto Redis, bootstrap servers Kafka.

- **[S0-CAT-3] Docker Compose con PostgreSQL propio** ✅ COMPLETADO

- **[S0-CAT-4] Registro de cuenta técnica contra cátedra** ✅ COMPLETADO

  - Una vez, vía Postman.

- **[S0-CAT-5] Refactorizar estructura JHipster a arquitectura hexagonal** 🔴 PENDIENTE — hacer antes de Sprint 3

  - Plan detallado en `REFACTOR_HEXAGONAL.md` (en la raíz del repo). Seguir ese documento fase a fase.
  - La estructura JHipster default (`domain/`, `service/`, `repository/`, `web/rest/`) se reemplaza por una organización por **features**, cada una con capas internas `domain/`, `application/` e `infrastructure/`:
    - Features: `professional/`, `schedule/`, `sync/`, `user/`
    - Cada feature sigue la estructura definida en la sección de arquitectura hexagonal arriba
    - Convenciones clave: `ports` (plural), port out sin sufijo "Port", facade en `application/service/`, impls en `application/usecases/`, entidades JPA en `infrastructure/persistence/entity/`, adapters en `infrastructure/persistence/adapter/`, controllers en `infrastructure/web/controller/`
    - Los mappers son `@Component` manuales (no MapStruct)
    - Los DTOs web se dividen en `XxxRequest` + `XxxResponse` separados
  - Orden de fases: FASE 1 (professional) → FASE 2 (schedule) → FASE 3 (sync) → FASE 4 (user) → FASE 5 (limpieza)
  - Dependencia: Ninguna.

- **[S0-CAT-6] Test de verificación de arquitectura hexagonal con ArchUnit** 🔴 PENDIENTE — hacer antes de Sprint 3
  - Agregar dependencia ArchUnit al `pom.xml`.
  - Crear clase `HexagonalArchitectureTest` con las reglas descritas en la sección de arquitectura hexagonal de este documento.
  - El test debe pasar en verde antes de continuar con Sprint 3. Si falla, corregir S0-CAT-5 hasta que pase.
  - Dependencia: Ninguna (depende de S0-CAT-5).

### Sprint 1 — Seguridad base y contrato propio hacia afuera ✅ COMPLETADO

> Todos los issues de este sprint y en adelante deben seguir la estructura hexagonal. Cada nuevo caso de uso → nuevo puerto de entrada. Cada nueva integración externa → nuevo puerto de salida + adaptador.

- **[S1-CAT-1] JWT técnico de servicio para llamadas de Repo 3**

  - Puerto de entrada: `AuthenticateServiceUseCase`
  - Dependencia: Ninguna.

- **[S1-CAT-2] Configuración CORS**

  - Permitir únicamente los orígenes necesarios.
  - Dependencia: Ninguna.

- **[S1-CAT-3] Documentar contrato propio de catálogo**
  - Endpoints que expone este servicio hacia Repo 1 y Repo 3.
  - Dependencia: Ninguna.

### Sprint 2 — Sincronización completa ✅ COMPLETADO

- **[S2-CAT-1] Puerto de salida: cliente REST contra cátedra (snapshot)**

  - Interfaz: `CatedraSnapshotClient` en `sync/domain/ports/out/`
  - Implementación: `CatedraSnapshotRestAdapter` en `sync/infrastructure/external/catedra/`
  - Caso de uso: `SyncFullCatalogUseCase` en `sync/domain/ports/in/`
  - Lógica: `SyncFullCatalogUseCaseImpl` en `sync/application/usecases/` + facade `CatalogSyncService` en `sync/application/service/`
  - Dependencia: Ninguna.

- **[S2-CAT-2] Endpoint propio de búsqueda/filtros**

  - Caso de uso: `SearchProfessionalsUseCase` en `professional/domain/ports/in/`
  - Adaptador de entrada: `ProfessionalController` en `professional/infrastructure/web/controller/`
  - Dependencia: Ninguna.

- **[S2-CAT-3] Endpoint propio de horarios vigentes por profesional**
  - Caso de uso: `GetAllWeeklySchedulesUseCase` + `GetWeeklySchedulesByProfessionalUseCase` en `schedule/domain/ports/in/`
  - Adaptador de entrada: `WeeklyScheduleController` en `schedule/infrastructure/web/controller/`
  - Dependencia: Ninguna.

### Sprint 3 — Sincronización incremental 🔴 NO INICIADO — completar S0-CAT-5 y S0-CAT-6 primero

- **[S3-CAT-1] Adaptador de entrada Kafka: consumer `CatalogUpdated`**

  - Implementación: `CatalogKafkaConsumer` en `sync/infrastructure/messaging/`
  - Llama al caso de uso `ProcessCatalogUpdateUseCase` (en `sync/domain/ports/in/`)
  - Deduplicación por `eventId` en `sync/application/usecases/`
  - Dependencia: Ninguna.

- **[S3-CAT-2] Puerto de salida Redis + aplicación de cambios incrementales**

  - Interfaz: `CatalogChangesClient` en `sync/domain/ports/out/`
  - Implementación: `RedisAdapter` en `sync/infrastructure/redis/`
  - Dependencia: Ninguna.

- **[S3-CAT-3] Detección de discontinuidad y recuperación**
  - Lógica en `domain/service/` — si versión local < `oldestAvailableVersion`, invocar `SyncFullCatalogUseCase`.
  - Dependencia: Ninguna.

### Sprint 4 — Robustez de sincronización

- **[S4-CAT-1] Idempotencia verificada ante duplicados Kafka**

  - Test explícito: mismo `eventId` recibido dos veces no duplica efectos.
  - Dependencia: Ninguna.

- **[S4-CAT-2] Observabilidad de sincronización**
  - Endpoint o log estructurado con estado y errores de la última sincronización.
  - Dependencia: Ninguna.

### Sprint 5 — Integración real con Repo 3 y Repo 1

- **[S5-CAT-1] Habilitar y probar JWT técnico de servicio con Repo 3 real**

  - Dependencia: Hard.

- **[S5-CAT-2] Habilitar y probar endpoints con Repo 1 real**
  - Dependencia: Hard.

### Sprint 6 — Tests y documentación

- **[S6-CAT-1] Tests automatizados mínimos exigidos**

  - Sincronización completa e incremental, idempotencia, autorización del endpoint propio.
  - Los tests de dominio (`domain/service/`) no deben necesitar Spring context ni base de datos — son tests unitarios puros.
  - Dependencia: Ninguna.

- **[S6-CAT-2] Documentación**
  - Modelo de datos, estrategia de sync, decisiones de seguridad, diagrama de arquitectura hexagonal del servicio.
  - Dependencia: Ninguna.

### Sprint 7 — Auditoría final

- **[S7-CAT-1] Checklist de evidencias mínimas (sección 11) para este repo**
  - Inicialización desde snapshot, actualización incremental, recuperación tras discontinuidad, búsquedas/filtros, separación efectiva de datos, rechazo de accesos no autenticados.
  - Dependencia: Ninguna para la mayoría; Hard para verificación end-to-end.

---

## Repo 3 — Backend de Turnos y Reservas

Responsabilidad: disponibilidad, holds, máquina de estados de reserva, integración asincrónica vía Kafka. Único propietario de holds/procesos/reservas y de los usuarios finales.

**Patrón arquitectónico: Hexagonal (Ports & Adapters)**

> ⚠️ La estructura base se generó con JHipster en Sprint 0, pero requiere un refactor para seguir exactamente la estructura del repositorio de ejemplo del profesor. Completar **S0-TUR-7** antes de iniciar Sprint 2.

### Sprint 0 — Scaffolding con arquitectura hexagonal ✅ COMPLETADO

Paquete base: `ar.edu.um.prog2.turnosyreservas`
Puerto del servicio: 8082
`groupId` de cátedra: `valentin-barzola`

- **[S0-TUR-1] Proyecto JHipster + entidades + estructura hexagonal** ✅ COMPLETADO

  - Entidades JPA: `Hold`, `ReservationProcess`, `Reservation`, `UserProfile`
  - Enums: `HoldStatus`, `ProcessStatus`, `ReservationStatus`
  - CRUD de JHipster eliminado para entidades de negocio
  - Puertos de entrada: `CreateHoldUseCase`, `ConfirmHoldUseCase`, `GetAvailabilityUseCase`, `GetProcessStatusUseCase`, `SubmitPhoneUseCase`, `GetUserReservationsUseCase`, `CancelReservationUseCase`
  - Puertos de salida: `HoldRepositoryPort`, `ReservationRepositoryPort`, `ReservationProcessRepositoryPort`, `CatedraAppointmentClient`, `CatalogServiceClient`, `AppointmentKafkaProducer`
  - Adaptadores vacíos listos: `adapter/in/rest/`, `adapter/in/kafka/`, `adapter/out/persistence/`, `adapter/out/catedra/`, `adapter/out/catalog/`, `adapter/out/kafka/`
  - JHipster conservado sin tocar: `config/`, `security/`, `service/` (solo UserService/MailService), `web/rest/` (solo endpoints de usuario/auth)

- **[S0-TUR-2] Externalización de configuración de cátedra** ✅ COMPLETADO

  - Variables en `.env` (en `.gitignore`): `CATEDRA_API_URL`, `CATEDRA_JWT`, `CATEDRA_KAFKA_BOOTSTRAP_SERVERS`, topics y consumer group
  - Bindings Kafka: `appointmentActionsConsumer`, `phoneSubmittedProducer`
  - `auto-create-topics: false`

- **[S0-TUR-3] Docker Compose con PostgreSQL propio** ✅ COMPLETADO

  - `src/main/docker/services.yml` con PostgreSQL + Kafka local

- **[S0-TUR-4] Registro de cuenta técnica contra cátedra** ✅ COMPLETADO

  - Registrado contra `http://192.168.194.250:8090`, `groupId: valentin-barzola`

- **[S0-TUR-5] Diseño de `externalPatientId`** ✅ COMPLETADO

  - Estrategia: UUID estable generado al registrar el usuario
  - Campo `externalPatientId` en entidad `UserProfile` (OneToOne con `User` de JHipster)

- **[S0-TUR-6] Test de verificación de arquitectura hexagonal con ArchUnit** ✅ COMPLETADO

  - `HexagonalArchitectureTest` con 3 reglas pasando en verde
  - `TechnicalStructureTest` de JHipster actualizado con capas `AdapterIn` y `AdapterOut`
  - ⚠️ NOTA: las reglas verifican la estructura `domain/` → `adapter/`. Deben actualizarse en S0-TUR-7 a los nuevos packages por feature.

- **[S0-TUR-7] Refactorizar a estructura hexagonal del profesor** 🔴 PENDIENTE — hacer antes de continuar Sprint 3
  - Plan detallado en `REFACTOR_HEXAGONAL.md` (en la raíz del repo). Seguir ese documento fase a fase.
  - Reorganizar por features (`hold/`, `reservation/`, `user/`) con capas `domain/`, `application/`, `infrastructure/` dentro de cada feature, siguiendo exactamente el patrón del repositorio de ejemplo.
  - Convenciones clave: `ports` (plural), port out sin sufijo "Port" (`HoldRepository` no `HoldRepositoryPort`), facade en `application/service/`, impls en `application/usecases/`, entidades JPA en `infrastructure/persistence/entity/`, adapters en `infrastructure/persistence/adapter/`.
  - Actualizar `HexagonalArchitectureTest` para verificar los nuevos packages por feature.
  - Dependencia: Ninguna.

### Sprint 1 — Usuarios finales y seguridad base

> Cada nuevo caso de uso → nuevo puerto de entrada. Cada nueva integración → nuevo puerto de salida + adaptador.

- **[S1-TUR-1] Modelo de usuario final compatible JHipster** ✅ COMPLETADO

  - `/api/register` → 201, `/api/authenticate` → `{id_token}`, `/api/account` → campos correctos.
  - `externalPatientId` UUID generado en `registerAccount()` línea 75, crea `UserProfile`.
  - Vive fuera de hexagonal (stack JHipster propio) — correcto, no refactorizar.
  - ⚠️ PENDIENTE VERIFICAR: JHipster requiere activación por email por defecto. El enunciado exige cuenta activa inmediatamente. Verificar `UserService.registerUser()` y deshabilitar activación si está habilitada.

- **[S1-TUR-2] CatalogServiceClient + CatalogServiceRestAdapter** ✅ COMPLETADO

  - `domain/port/out/CatalogServiceClient.java` — interfaz del dominio.
  - `adapter/out/catalog/CatalogServiceRestAdapter.java` — llama Repo 2 con Bearer serviceJwt.
  - `getProfessional()` implementado. `getSlotsForDate()` retorna lista vacía con TODO (completa en Sprint 2).
  - Dependencia: Soft (mockeable).

- **[S1-TUR-3] Configuración CORS** ✅ COMPLETADO (infraestructura)
  - `WebConfigurer.java` registra CorsFilter sobre `/api/**`.
  - Origen controlado por variable `CORS_ALLOWED_ORIGINS` en `application.yml`.
  - ⚠️ PENDIENTE: configurar el origen real de KMP cuando se defina la URL de producción.

### Sprint 2 — Disponibilidad ✅ COMPLETADO

- **[S2-TUR-1] Puerto de salida: cliente REST contra cátedra (ocupaciones)**

  - Interfaz: `CatedraOccupancyClient` en `domain/port/out/`
  - Implementación: `CatedraOccupancyRestAdapter` en `adapter/out/catedra/`
  - Dependencia: Ninguna.

- **[S2-TUR-2] Caso de uso y endpoint propio de disponibilidad**
  - Caso de uso: `GetAvailabilityUseCase` — combina horarios (Repo 2) + ocupaciones (cátedra)
  - Adaptador de entrada: controller REST en `adapter/in/rest/`
  - Dependencia: Soft (mockeable) contra Repo 2.

### Sprint 3 — Holds y confirmación inicial 🔄 EN PROGRESO — completar S0-TUR-7 antes de continuar

- **[S3-TUR-1] Caso de uso: crear hold** ✅ COMPLETADO

  - `CreateHoldUseCase` — valida contra Repo 2, llama a cátedra, persiste.
  - Adaptadores: `CatedraHoldRestAdapter` en `adapter/out/catedra/`
  - Dependencia: Soft (mockeable) para Repo 2.

- **[S3-TUR-2] Caso de uso: confirmar hold inicialmente**

  - `ConfirmHoldUseCase` — llama a cátedra, actualiza estado local.
  - Dependencia: Ninguna adicional.

- **[S3-TUR-3] Endpoint propio de estado de proceso**
  - Para polling desde KMP.
  - Caso de uso: `GetProcessStatusUseCase`
  - Dependencia: Ninguna.

### Sprint 4 — Intercambio asincrónico

- **[S4-TUR-1] Adaptador de entrada Kafka: consumer `AdditionalInformationRequested`**

  - Implementación en `adapter/in/kafka/`
  - Llama al caso de uso `ProcessPhoneRequestUseCase`
  - Dependencia: Ninguna.

- **[S4-TUR-2] Caso de uso y endpoint: recibir teléfono**

  - `SubmitPhoneUseCase` — valida formato, persiste.
  - Dependencia: Ninguna.

- **[S4-TUR-3] Adaptador de salida Kafka: producer `AdditionalInformationSubmitted`**
  - Interfaz: `AppointmentKafkaProducer` en `domain/port/out/`
  - Implementación: `KafkaProducerAdapter` en `adapter/out/kafka/`
  - Dependencia: Ninguna.

### Sprint 5 — Resultados finales y gestión de reservas

- **[S5-TUR-1] Adaptadores de entrada Kafka: consumers de resultado final**

  - `AppointmentConfirmed`, `AdditionalInformationRejected`, `AppointmentProcessExpired`, `AppointmentProcessInvalid`
  - Cada uno llama al caso de uso correspondiente en `domain/service/`
  - Estados finales son terminales: la lógica de dominio lo garantiza.
  - Dependencia: Ninguna.

- **[S5-TUR-2] Caso de uso y endpoint: listar reservas del usuario**

  - `GetUserReservationsUseCase` — filtrado por usuario autenticado.
  - Dependencia: Ninguna.

- **[S5-TUR-3] Caso de uso y endpoint: cancelar reserva**
  - `CancelReservationUseCase` — idempotente, verifica propiedad.
  - Dependencia: Ninguna.

### Sprint 6 — Robustez

- **[S6-TUR-1] Idempotencia verificada ante duplicados Kafka**

  - Test explícito con `eventId` repetido. La lógica de deduplicación vive en `domain/service/`.
  - Dependencia: Ninguna.

- **[S6-TUR-2] Vencimiento de holds/procesos**

  - Cierre local correcto sin nuevos intentos.
  - Dependencia: Ninguna.

- **[S6-TUR-3] Recuperación de procesos pendientes tras reinicio**
  - Dependencia: Ninguna.

### Sprint 7 — Tests, documentación y auditoría final

- **[S7-TUR-1] Tests automatizados mínimos exigidos**

  - Los tests de `domain/service/` son unitarios puros — sin Spring context, sin base de datos, sin Kafka.
  - Reservas, autorización, idempotencia.
  - Dependencia: Ninguna.

- **[S7-TUR-2] Documentación**

  - Contrato entre Repo 2 y Repo 3, máquina de estados, decisiones de seguridad, diagrama de arquitectura hexagonal del servicio.
  - Dependencia: Ninguna.

- **[S7-TUR-3] Checklist de evidencias mínimas (sección 11) para este repo**
  - Reserva confirmada end-to-end, reserva expirada, cancelación propia sin acceso cruzado, mensaje Kafka duplicado procesado correctamente.
  - Dependencia: Hard.

---

## Apéndice — Evidencias mínimas (sección 11 del enunciado) vs. sprints

| Evidencia exigida                                                    | Repo(s) responsables   | Sprint                       |
| -------------------------------------------------------------------- | ---------------------- | ---------------------------- |
| Inicialización desde snapshot                                        | Repo 2, Repo 1         | S2                           |
| Actualización incremental del catálogo                               | Repo 2                 | S3                           |
| Recuperación mediante snapshot tras discontinuidad                   | Repo 2                 | S3                           |
| Búsquedas resueltas sobre datos locales                              | Repo 2, Repo 1         | S2                           |
| Filtros obligatorios (categoría, nombre, habilitado, disponibilidad) | Repo 2, Repo 1         | S2-S3                        |
| Reserva confirmada vía flujo REST + Kafka                            | Repo 3, Repo 1         | S4-S5                        |
| Reserva que expire sin completar información                         | Repo 3, Repo 1         | S4-S6                        |
| Consulta y cancelación de reserva propia, sin acceso cruzado         | Repo 3, Repo 1         | S5                           |
| Procesamiento idempotente de mensaje duplicado                       | Repo 2, Repo 3         | S4 (cat) / S6 (tur)          |
| Separación efectiva de datos entre los dos servicios                 | Repo 2, Repo 3         | S0 (diseño) / S7 (auditoría) |
| Rechazo de accesos no autenticados o no autorizados                  | Repo 2, Repo 3, Repo 1 | S1                           |
| Pruebas automatizadas de comportamientos principales                 | Repo 2, Repo 3, Repo 1 | S6-S7                        |
