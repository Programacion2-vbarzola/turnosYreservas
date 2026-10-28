# Refactor Hexagonal — turnosYreservas

## Objetivo

Reorganizar el proyecto para que cada feature tenga su propia capa de **dominio**, **aplicación** e **infraestructura**, siguiendo la misma estructura del repositorio de ejemplo `hexagonal/`. La regla de oro es:

> El dominio no puede depender de infraestructura. Las entidades JPA viven en infraestructura. El dominio contiene POJOs puros.

---

## Problema central

Los modelos de dominio actuales (`Hold`, `Reservation`, `ReservationProcess`, `User`, `UserProfile`) son **entidades JPA** (`@Entity`, `@Table`, `@Column`, etc.). Esto viola la arquitectura hexagonal porque el dominio depende de JPA, que es un detalle de infraestructura.

La solución, siguiendo el ejemplo:

| Ejemplo (`hexagonal/`)                                                | Este repo (a crear)                                                   |
| --------------------------------------------------------------------- | --------------------------------------------------------------------- |
| `domain/model/Product.java` → POJO puro                               | `hold/domain/model/Hold.java` → POJO puro                             |
| `infrastructure/persistence/entity/ProductEntity.java` → `@Entity`    | `hold/infrastructure/persistence/entity/HoldEntity.java` → `@Entity`  |
| `infrastructure/persistence/mapper/ProductMapper.java`                | `hold/infrastructure/persistence/mapper/HoldEntityMapper.java`        |
| `infrastructure/persistence/adapter/JpaProductRepositoryAdapter.java` | `hold/infrastructure/persistence/adapter/HoldPersistenceAdapter.java` |

---

## Estructura objetivo completa

```
src/main/java/ar/edu/um/prog2/turnosyreservas/
│
├── hold/
│   ├── domain/
│   │   ├── model/
│   │   │   ├── Hold.java                          ← POJO puro (sin @Entity)
│   │   │   └── enumeration/
│   │   │       └── HoldStatus.java
│   │   └── port/
│   │       ├── in/
│   │       │   ├── CreateHoldUseCase.java
│   │       │   └── ConfirmHoldUseCase.java
│   │       └── out/
│   │           ├── HoldRepositoryPort.java
│   │           └── AppointmentKafkaProducer.java
│   ├── application/
│   │   ├── service/
│   │   │   ├── CreateHoldService.java
│   │   │   └── ConfirmHoldService.java
│   │   └── exception/
│   │       ├── ProfessionalNotFoundException.java
│   │       └── SlotAlreadyHeldException.java
│   └── infrastructure/
│       └── persistence/
│           ├── entity/
│           │   └── HoldEntity.java                ← lleva el @Entity
│           ├── mapper/
│           │   └── HoldEntityMapper.java          ← convierte HoldEntity ↔ Hold
│           ├── repository/
│           │   └── HoldJpaRepository.java         ← Spring Data JPA, usa HoldEntity
│           └── adapter/
│               └── HoldPersistenceAdapter.java    ← implementa HoldRepositoryPort
│
├── reservation/
│   ├── domain/
│   │   ├── model/
│   │   │   ├── Reservation.java                   ← POJO puro
│   │   │   ├── ReservationProcess.java            ← POJO puro
│   │   │   └── enumeration/
│   │   │       ├── ProcessStatus.java
│   │   │       └── ReservationStatus.java
│   │   └── port/
│   │       ├── in/
│   │       │   ├── CancelReservationUseCase.java
│   │       │   ├── GetUserReservationsUseCase.java
│   │       │   ├── GetProcessStatusUseCase.java
│   │       │   └── SubmitPhoneUseCase.java
│   │       └── out/
│   │           ├── ReservationRepositoryPort.java
│   │           └── ReservationProcessRepositoryPort.java
│   ├── application/
│   │   └── service/
│   │       ├── CancelReservationService.java
│   │       ├── GetUserReservationsService.java
│   │       ├── GetProcessStatusService.java
│   │       └── SubmitPhoneService.java
│   └── infrastructure/
│       └── persistence/
│           ├── entity/
│           │   ├── ReservationEntity.java
│           │   └── ReservationProcessEntity.java
│           ├── mapper/
│           │   ├── ReservationEntityMapper.java
│           │   └── ReservationProcessEntityMapper.java
│           ├── repository/
│           │   ├── ReservationJpaRepository.java
│           │   └── ReservationProcessJpaRepository.java
│           └── adapter/
│               ├── ReservationPersistenceAdapter.java
│               └── ReservationProcessPersistenceAdapter.java
│
├── availability/
│   ├── domain/
│   │   ├── model/
│   │   │   ├── AvailabilitySlot.java              ← ya es POJO ✓ (solo mover)
│   │   │   ├── ScheduleSlot.java                  ← ya es POJO ✓
│   │   │   ├── Occupancy.java                     ← ya es POJO ✓
│   │   │   └── ProfessionalInfo.java              ← ya es POJO ✓
│   │   └── port/
│   │       ├── in/
│   │       │   └── GetAvailabilityUseCase.java
│   │       └── out/
│   │           ├── CatalogServiceClient.java
│   │           └── CatedraAppointmentClient.java
│   ├── application/
│   │   └── service/
│   │       └── GetAvailabilityService.java
│   └── infrastructure/
│       ├── external/
│       │   ├── catalog/
│       │   │   ├── CatalogServiceRestAdapter.java
│       │   │   └── dto/
│       │   │       └── ProfessionalResponseDTO.java
│       │   └── catedra/
│       │       ├── CatedraRestAdapter.java
│       │       └── dto/
│       │           ├── AppointmentOccupanciesResponseDTO.java
│       │           └── OccupancyItemDTO.java
│       └── web/
│           └── AvailabilityController.java
│
├── user/
│   ├── domain/
│   │   ├── model/
│   │   │   ├── User.java                          ← POJO puro (sin @Entity)
│   │   │   ├── UserProfile.java                   ← POJO puro
│   │   │   └── Authority.java                     ← POJO puro
│   │   └── port/
│   │       └── out/
│   │           ├── UserRepositoryPort.java
│   │           └── UserProfileRepositoryPort.java
│   ├── application/
│   │   ├── service/
│   │   │   └── UserService.java
│   │   └── dto/
│   │       ├── AdminUserDTO.java
│   │       ├── UserDTO.java
│   │       └── PasswordChangeDTO.java
│   └── infrastructure/
│       ├── persistence/
│       │   ├── entity/
│       │   │   ├── UserEntity.java                ← lleva el @Entity("jhi_user")
│       │   │   ├── AuthorityEntity.java           ← lleva el @Entity("jhi_authority")
│       │   │   └── UserProfileEntity.java         ← lleva el @Entity
│       │   ├── mapper/
│       │   │   ├── UserEntityMapper.java
│       │   │   └── UserProfileEntityMapper.java
│       │   ├── repository/
│       │   │   ├── UserJpaRepository.java
│       │   │   ├── AuthorityJpaRepository.java
│       │   │   └── UserProfileJpaRepository.java
│       │   └── adapter/
│       │       ├── UserPersistenceAdapter.java
│       │       └── UserProfilePersistenceAdapter.java
│       └── web/
│           ├── AuthenticateController.java
│           ├── AccountResource.java
│           ├── UserResource.java
│           ├── PublicUserResource.java
│           ├── AuthorityResource.java
│           ├── dto/
│           │   └── (vm/ → mover aquí: LoginVM, ManagedUserVM, etc.)
│           └── errors/
│               └── (errores JHipster: BadRequestAlertException, etc.)
│
├── config/                                        ← se queda sin cambios
├── security/                                      ← se queda; referencias a UserEntity
├── aop/                                           ← se queda sin cambios
├── management/                                    ← se queda sin cambios
├── service/                                       ← eliminar al finalizar (era JHipster legacy)
├── web/rest/                                      ← eliminar al finalizar (movido a user/)
├── repository/                                    ← eliminar al finalizar (movido a cada feature)
└── TurnosYreservasApp.java
```

---

## Plan de modificaciones

El refactor se divide en **5 fases** ordenadas por complejidad, de menor a mayor. Cada fase termina con una verificación de compilación antes de continuar.

---

### FASE 1 — Feature `availability/`

**Impacto:** bajo. Los modelos ya son POJOs. Solo es mover archivos y actualizar paquetes.

#### Archivos a crear

| Nuevo archivo                                                                             | Origen                                        |
| ----------------------------------------------------------------------------------------- | --------------------------------------------- |
| `availability/domain/model/AvailabilitySlot.java`                                         | mover de `domain/model/AvailabilitySlot.java` |
| `availability/domain/model/ScheduleSlot.java`                                             | mover de `domain/model/ScheduleSlot.java`     |
| `availability/domain/model/Occupancy.java`                                                | mover de `domain/model/Occupancy.java`        |
| `availability/domain/model/ProfessionalInfo.java`                                         | mover de `domain/model/ProfessionalInfo.java` |
| `availability/domain/port/in/GetAvailabilityUseCase.java`                                 | mover de `domain/port/in/`                    |
| `availability/domain/port/out/CatalogServiceClient.java`                                  | mover de `domain/port/out/`                   |
| `availability/domain/port/out/CatedraAppointmentClient.java`                              | mover de `domain/port/out/`                   |
| `availability/application/service/GetAvailabilityService.java`                            | mover de `domain/service/`                    |
| `availability/infrastructure/external/catalog/CatalogServiceRestAdapter.java`             | mover de `adapter/out/catalog/`               |
| `availability/infrastructure/external/catalog/dto/ProfessionalResponseDTO.java`           | mover de `adapter/out/catalog/`               |
| `availability/infrastructure/external/catedra/CatedraRestAdapter.java`                    | mover de `adapter/out/catedra/`               |
| `availability/infrastructure/external/catedra/dto/AppointmentOccupanciesResponseDTO.java` | mover de `adapter/out/catedra/`               |
| `availability/infrastructure/external/catedra/dto/OccupancyItemDTO.java`                  | mover de `adapter/out/catedra/`               |
| `availability/infrastructure/web/AvailabilityController.java`                             | mover de `adapter/in/rest/`                   |

#### Archivos a eliminar tras mover

- `domain/model/` (todo)
- `domain/port/in/GetAvailabilityUseCase.java`
- `domain/port/out/CatalogServiceClient.java`
- `domain/port/out/CatedraAppointmentClient.java`
- `domain/service/GetAvailabilityService.java`
- `adapter/out/catalog/` (todo)
- `adapter/out/catedra/` (todo)
- `adapter/in/rest/AvailabilityController.java`

---

### FASE 2 — Feature `hold/`

**Impacto:** medio. Requiere crear `HoldEntity` separada y mapper.

#### Archivos a crear (nuevos)

**`hold/infrastructure/persistence/entity/HoldEntity.java`**
Copiar el contenido actual de `domain/Hold.java` (con todas sus anotaciones JPA: `@Entity`, `@Table`, `@Column`, `@GeneratedValue`, `@ManyToOne`, etc.) renombrándolo `HoldEntity`. Este archivo mantiene todas las anotaciones JPA.

**`hold/infrastructure/persistence/mapper/HoldEntityMapper.java`**
Interface o clase que convierte `HoldEntity ↔ Hold`. Ejemplo siguiendo el repo de referencia:

```java
@Component
public class HoldEntityMapper {
    public Hold toDomain(HoldEntity entity) { ... }
    public HoldEntity toEntity(Hold domain) { ... }
}
```

Campos a mapear: `id`, `externalHoldId`, `externalProcessId`, `professionalId`, `date`,
`startTime`, `endTime`, `status`, `expiresAt`, `createdAt`.
Para `user`: mapear solo el `id` del usuario (no la entidad entera). Ver sección de nota sobre relaciones.

**`hold/infrastructure/persistence/repository/HoldJpaRepository.java`**
Interface que extiende `JpaRepository<HoldEntity, Long>`. Mover las queries que hoy están en `repository/HoldRepository.java`, adaptando el tipo a `HoldEntity`. Agregar el método necesario para `existsActiveHoldForSlot`.

#### Archivos a modificar

**`hold/domain/model/Hold.java`** (mover de `domain/Hold.java`)

- Cambiar `package` a `...hold.domain.model`
- **Eliminar** todos los imports de `jakarta.persistence.*`
- **Eliminar** `@Entity`, `@Table`, `@GeneratedValue`, `@SequenceGenerator`, `@Column`, `@ManyToOne`, `@OneToOne`, `@JsonIgnoreProperties`, `@NotNull`, `@Size`
- Reemplazar el campo `private User user` por `private Long userId` (solo el ID, no la entidad)
- Reemplazar el campo `private ReservationProcess reservationProcess` por `private Long reservationProcessId`
- El resultado es un POJO con solo campos primitivos/value types y getters/setters

**`hold/infrastructure/persistence/adapter/HoldPersistenceAdapter.java`** (mover de `adapter/out/persistence/`)

- Cambiar `package`
- Inyectar `HoldJpaRepository` y `HoldEntityMapper`
- Método `save`: recibir `Hold`, convertir a `HoldEntity` via mapper, guardar, convertir resultado de vuelta a `Hold`
- Método `existsActiveHoldForSlot`: delegar a `HoldJpaRepository`

**`hold/application/service/CreateHoldService.java`** (mover de `domain/service/`)

- Actualizar `package`
- El campo `hold.setUser(user)` cambia a `hold.setUserId(user.getId())` porque el dominio ya no tiene la entidad `User`

#### Archivos a mover (solo cambio de package)

| Origen                                              | Destino                          |
| --------------------------------------------------- | -------------------------------- |
| `domain/port/in/CreateHoldUseCase.java`             | `hold/domain/port/in/`           |
| `domain/port/in/ConfirmHoldUseCase.java`            | `hold/domain/port/in/`           |
| `domain/port/out/HoldRepositoryPort.java`           | `hold/domain/port/out/`          |
| `domain/port/out/AppointmentKafkaProducer.java`     | `hold/domain/port/out/`          |
| `domain/service/ConfirmHoldService.java`            | `hold/application/service/`      |
| `domain/service/ProfessionalNotFoundException.java` | `hold/application/exception/`    |
| `domain/service/SlotAlreadyHeldException.java`      | `hold/application/exception/`    |
| `domain/enumeration/HoldStatus.java`                | `hold/domain/model/enumeration/` |

#### Archivos a eliminar tras mover

- `domain/Hold.java`
- `domain/port/in/CreateHoldUseCase.java`, `ConfirmHoldUseCase.java`
- `domain/port/out/HoldRepositoryPort.java`, `AppointmentKafkaProducer.java`
- `domain/service/CreateHoldService.java`, `ConfirmHoldService.java`
- `domain/service/ProfessionalNotFoundException.java`, `SlotAlreadyHeldException.java`
- `domain/enumeration/HoldStatus.java`
- `adapter/out/persistence/HoldPersistenceAdapter.java`
- `repository/HoldRepository.java`

---

### FASE 3 — Feature `reservation/`

**Impacto:** medio-alto. Similar a `hold/` pero con dos entidades relacionadas entre sí.

#### Nota sobre relaciones entre entidades

`ReservationProcess` referencia a `Hold` y `Reservation` referencia a `ReservationProcess`. En el dominio puro, estas referencias se hacen por **ID** (no por objeto). En la capa de infraestructura (entity), las relaciones JPA se mantienen con `@ManyToOne`, `@OneToOne`, etc.

#### Archivos a crear (nuevos)

**`reservation/infrastructure/persistence/entity/ReservationProcessEntity.java`**
Copia de `domain/ReservationProcess.java` con todas sus anotaciones JPA. La relación `Hold hold` pasa a ser `HoldEntity hold` con `@OneToOne`.

**`reservation/infrastructure/persistence/entity/ReservationEntity.java`**
Copia de `domain/Reservation.java` con todas sus anotaciones JPA. La relación `ReservationProcess process` pasa a ser `ReservationProcessEntity process` con `@OneToOne`.

**`reservation/infrastructure/persistence/mapper/ReservationProcessEntityMapper.java`**
Convierte `ReservationProcessEntity ↔ ReservationProcess`. El campo `Hold hold` del entity se mapea a `Long holdId` en el dominio.

**`reservation/infrastructure/persistence/mapper/ReservationEntityMapper.java`**
Convierte `ReservationEntity ↔ Reservation`. El campo `ReservationProcess process` del entity se mapea a `Long processId` en el dominio.

**`reservation/infrastructure/persistence/repository/ReservationProcessJpaRepository.java`**
Extiende `JpaRepository<ReservationProcessEntity, Long>`. Mover queries de `repository/ReservationProcessRepository.java`.

**`reservation/infrastructure/persistence/repository/ReservationJpaRepository.java`**
Extiende `JpaRepository<ReservationEntity, Long>`. Mover queries de `repository/ReservationRepository.java`.

**`reservation/infrastructure/persistence/adapter/ReservationProcessPersistenceAdapter.java`**
Implementa `ReservationProcessRepositoryPort`. Inyecta repositorio + mapper.

**`reservation/infrastructure/persistence/adapter/ReservationPersistenceAdapter.java`**
Implementa `ReservationRepositoryPort`. Inyecta repositorio + mapper.

#### Archivos a modificar

**`reservation/domain/model/Reservation.java`** (mover y limpiar de `domain/Reservation.java`)

- Eliminar todos los imports y anotaciones JPA
- `private ReservationProcess process` → `private Long processId`
- `private User user` → `private Long userId`

**`reservation/domain/model/ReservationProcess.java`** (mover y limpiar de `domain/ReservationProcess.java`)

- Eliminar todos los imports y anotaciones JPA
- `private Hold hold` → `private Long holdId`
- `private User user` → `private Long userId`
- `private Reservation reservation` → `private Long reservationId`

**Servicios de reserva** (solo cambio de package + imports actualizados):

- `CancelReservationService` → usa `Long holdId` en vez de objeto `Hold`
- `GetUserReservationsService` → usa `Long userId`
- `SubmitPhoneService` → sin cambios estructurales
- `GetProcessStatusService` → sin cambios estructurales

#### Archivos a mover (solo cambio de package)

| Origen                                                  | Destino                                 |
| ------------------------------------------------------- | --------------------------------------- |
| `domain/port/in/CancelReservationUseCase.java`          | `reservation/domain/port/in/`           |
| `domain/port/in/GetUserReservationsUseCase.java`        | `reservation/domain/port/in/`           |
| `domain/port/in/GetProcessStatusUseCase.java`           | `reservation/domain/port/in/`           |
| `domain/port/in/SubmitPhoneUseCase.java`                | `reservation/domain/port/in/`           |
| `domain/port/out/ReservationRepositoryPort.java`        | `reservation/domain/port/out/`          |
| `domain/port/out/ReservationProcessRepositoryPort.java` | `reservation/domain/port/out/`          |
| `domain/service/CancelReservationService.java`          | `reservation/application/service/`      |
| `domain/service/GetUserReservationsService.java`        | `reservation/application/service/`      |
| `domain/service/GetProcessStatusService.java`           | `reservation/application/service/`      |
| `domain/service/SubmitPhoneService.java`                | `reservation/application/service/`      |
| `domain/enumeration/ProcessStatus.java`                 | `reservation/domain/model/enumeration/` |
| `domain/enumeration/ReservationStatus.java`             | `reservation/domain/model/enumeration/` |

#### Archivos a eliminar tras mover

- `domain/Reservation.java`
- `domain/ReservationProcess.java`
- `domain/port/in/` (los 4 de reservation)
- `domain/port/out/ReservationRepositoryPort.java`, `ReservationProcessRepositoryPort.java`
- `domain/service/CancelReservationService.java`, `GetUserReservationsService.java`, `GetProcessStatusService.java`, `SubmitPhoneService.java`
- `domain/enumeration/ProcessStatus.java`, `ReservationStatus.java`
- `repository/ReservationRepository.java`, `ReservationProcessRepository.java`

---

### FASE 4 — Feature `user/`

**Impacto:** alto. `User` está integrado con Spring Security. Requiere cuidado extra.

#### Decisión de diseño: User y Spring Security

`DomainUserDetailsService` (en `security/`) carga el `User` desde la base de datos y lo convierte en un `UserDetails` de Spring. Actualmente usa `UserRepository` que trabaja con `User` (entidad JPA).

Después del refactor:

- `DomainUserDetailsService` queda en `security/` (cross-cutting, no cambia de paquete)
- Pasa a usar `UserJpaRepository` (que trabaja con `UserEntity`) directamente, ya que está en infraestructura
- Los servicios de dominio (`UserService`) trabajan con el POJO `User` y usan `UserPersistenceAdapter`

#### Archivos a crear (nuevos)

**`user/infrastructure/persistence/entity/UserEntity.java`**
Copia exacta de `domain/User.java` con todas sus anotaciones JPA (`@Entity`, `@Table("jhi_user")`, etc.). Renombrar la clase a `UserEntity`. La relación `Set<Authority>` pasa a ser `Set<AuthorityEntity>`.

**`user/infrastructure/persistence/entity/AuthorityEntity.java`**
Copia de `domain/Authority.java` con sus anotaciones JPA. Clase renombrada a `AuthorityEntity`.

**`user/infrastructure/persistence/entity/UserProfileEntity.java`**
Copia de `domain/UserProfile.java` con sus anotaciones JPA. La relación `User user` pasa a ser `UserEntity user`.

**`user/infrastructure/persistence/mapper/UserEntityMapper.java`**
Convierte `UserEntity ↔ User` (dominio). El campo `Set<AuthorityEntity>` mapea a `Set<String>` (solo los nombres) en el dominio.

**`user/infrastructure/persistence/mapper/UserProfileEntityMapper.java`**
Convierte `UserProfileEntity ↔ UserProfile`.

**`user/infrastructure/persistence/repository/UserJpaRepository.java`**
Extiende `JpaRepository<UserEntity, Long>`. Mover métodos de `repository/UserRepository.java` (e.g., `findOneByLogin`, `findOneByEmail`).

**`user/infrastructure/persistence/repository/AuthorityJpaRepository.java`**
Mover de `repository/AuthorityRepository.java`. Cambia a `JpaRepository<AuthorityEntity, String>`.

**`user/infrastructure/persistence/repository/UserProfileJpaRepository.java`**
Mover de `repository/UserProfileRepository.java`. Cambia a `JpaRepository<UserProfileEntity, Long>`.

**`user/infrastructure/persistence/adapter/UserPersistenceAdapter.java`**
Implementa `UserRepositoryPort`. Inyecta `UserJpaRepository` + `UserEntityMapper`.

**`user/infrastructure/persistence/adapter/UserProfilePersistenceAdapter.java`**
Implementa `UserProfileRepositoryPort`. Inyecta `UserProfileJpaRepository` + `UserProfileEntityMapper`.

**`user/domain/port/out/UserRepositoryPort.java`**
Interface con los métodos que necesita `UserService`: `findOneByLogin`, `findOneByEmail`, `save`, `delete`, etc.

**`user/domain/port/out/UserProfileRepositoryPort.java`**
Interface con métodos de `UserProfile`: `findByUserId`, `save`, etc.

#### Archivos a modificar

**`user/domain/model/User.java`** (mover y limpiar de `domain/User.java`)

- Eliminar todas las anotaciones JPA
- Eliminar `extends AbstractAuditingEntity<Long>` (los campos `createdBy`, `createdDate`, etc. pueden incluirse directamente)
- El campo `Set<Authority>` pasa a ser `Set<String>` (solo los nombres de los roles)
- Resultado: POJO con login, password, firstName, lastName, email, activated, langKey, imageUrl, activationKey, resetKey, resetDate, Set\<String\> authorities

**`user/domain/model/UserProfile.java`** (mover y limpiar)

- Eliminar anotaciones JPA
- `private User user` → `private Long userId`

**`user/domain/model/Authority.java`** (mover y limpiar)

- Eliminar `@Entity`, `@Table`
- Mantener solo `String name`

**`user/application/service/UserService.java`** (mover de `service/UserService.java`)

- Cambiar inyección de `UserRepository` → `UserRepositoryPort`
- Cambiar inyección de `AuthorityRepository` → `UserPersistenceAdapter` con método `findAuthority`
- El servicio trabaja con el POJO `User`, no con la entidad

**`security/DomainUserDetailsService.java`** ← actualizar imports

- Cambiar `UserRepository` → `UserJpaRepository` (porque está en infraestructura y puede acceder a la entidad directamente)
- Cambiar el tipo `User` que usa por `UserEntity`

#### Archivos a mover a `user/infrastructure/web/`

- `web/rest/AuthenticateController.java`
- `web/rest/AccountResource.java`
- `web/rest/UserResource.java`
- `web/rest/PublicUserResource.java`
- `web/rest/AuthorityResource.java`
- `web/rest/errors/` → `user/infrastructure/web/errors/`
- `web/rest/vm/` → `user/infrastructure/web/vm/`

#### Archivos a mover a `user/application/dto/`

- `service/dto/AdminUserDTO.java`
- `service/dto/UserDTO.java`
- `service/dto/PasswordChangeDTO.java`

#### Archivos a eliminar tras mover

- `domain/User.java`, `domain/Authority.java`, `domain/UserProfile.java`
- `domain/AbstractAuditingEntity.java` (moverlo a `user/infrastructure/persistence/entity/` como base para las entities)
- `repository/UserRepository.java`, `AuthorityRepository.java`, `UserProfileRepository.java`
- `service/UserService.java`, `service/dto/`, `service/mapper/UserMapper.java`
- `service/EmailAlreadyUsedException.java`, `service/InvalidPasswordException.java`, etc. → mover a `user/application/exception/`
- `web/rest/` (completo, una vez movido todo)

---

### FASE 5 — Limpieza de paquetes legacy

Una vez completadas todas las fases anteriores, eliminar los paquetes de origen que ya quedaron vacíos:

- `domain/` (debe estar vacío si todo fue movido)
- `domain/enumeration/` (debe estar vacío)
- `domain/model/` (debe estar vacío)
- `domain/port/` (debe estar vacío)
- `domain/service/` (debe estar vacío)
- `adapter/` (debe estar vacío)
- `repository/` (debe estar vacío)
- `service/` (debe estar vacío)
- `web/rest/` (debe estar vacío)

Y limpiar los `package-info.java` que quedan huérfanos.

---

## Verificación por fase

### Verificación rápida (después de cada fase)

```bash
# Desde el directorio turnosYreservas/
./mvnw compile -q
```

El objetivo es que compile sin errores antes de avanzar a la siguiente fase.

### Verificación de arquitectura hexagonal

```bash
./mvnw test -Dtest=HexagonalArchitectureTest -q
```

Este test (ya existe en el proyecto) verifica con ArchUnit que:

- El dominio no depende de adapters
- Los adapters `in` no dependen de adapters `out`
- Los adapters `out` no dependen de adapters `in`

Actualizar el test para que verifique los nuevos paquetes. Reemplazar el paquete base y los patrones:

```java
// En HexagonalArchitectureTest.java — actualizar las reglas:
@Test
void domain_no_depende_de_adapters() {
  ArchRule rule = noClasses()
    .that()
    .resideInAnyPackage("..hold.domain..", "..reservation.domain..", "..availability.domain..", "..user.domain..")
    .should()
    .dependOnClassesThat()
    .resideInAnyPackage(
      "..hold.infrastructure..",
      "..reservation.infrastructure..",
      "..availability.infrastructure..",
      "..user.infrastructure.."
    );
  rule.check(classes);
}

@Test
void application_no_depende_de_infrastructure() {
  ArchRule rule = noClasses()
    .that()
    .resideInAnyPackage("..hold.application..", "..reservation.application..", "..availability.application..", "..user.application..")
    .should()
    .dependOnClassesThat()
    .resideInAnyPackage(
      "..hold.infrastructure..",
      "..reservation.infrastructure..",
      "..availability.infrastructure..",
      "..user.infrastructure.."
    );
  rule.check(classes);
}

```

### Verificación completa (al final del refactor)

```bash
# Build completo con tests
./mvnw verify

# Solo tests unitarios y de arquitectura
./mvnw test -Dtest="HexagonalArchitectureTest,TechnicalStructureTest"
```

### Verificación manual de endpoints (con la app corriendo)

```bash
# 1. Levantar la app
./mvnw spring-boot:run

# 2. Obtener token JWT
curl -X POST http://localhost:8080/api/authenticate \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin","rememberMe":false}'

# Guardar el token como variable
TOKEN="<token del paso anterior>"

# 3. Verificar disponibilidad (feature availability/)
curl -X GET "http://localhost:8080/api/availability?professionalId=1&date=2026-11-01" \
  -H "Authorization: Bearer $TOKEN"
# Esperado: 200 OK con lista de slots

# 4. Verificar que los endpoints de usuario siguen funcionando (feature user/)
curl -X GET http://localhost:8080/api/account \
  -H "Authorization: Bearer $TOKEN"
# Esperado: 200 OK con datos del usuario
```

---

## Reglas de arquitectura a respetar

| Capa                   | Puede depender de               | NO puede depender de                           |
| ---------------------- | ------------------------------- | ---------------------------------------------- |
| `domain/model/`        | nada externo                    | `infrastructure/`, `application/`, Spring, JPA |
| `domain/port/`         | `domain/model/`                 | `infrastructure/`, Spring, JPA                 |
| `application/service/` | `domain/model/`, `domain/port/` | `infrastructure/`                              |
| `infrastructure/`      | todo                            | nada del otro feature (idealmente)             |

---

## Notas importantes

### Relaciones entre entidades en el dominio

Cuando un modelo de dominio referencia a otro (ej. `Hold` referencia a `User`), **en el dominio se guarda solo el ID**, no el objeto completo. El objeto completo con la relación existe solo en la entidad JPA de infraestructura. Ejemplo:

```java
// ANTES (violación):
public class Hold {

  private User user; // ← referencia a entidad JPA
}

// DESPUÉS (correcto):
public class Hold {

  private Long userId; // ← solo el ID
}

```

### AbstractAuditingEntity

El `AbstractAuditingEntity` actual tiene anotaciones de Spring Data Auditing (`@CreatedBy`, `@CreatedDate`, etc.). Debe quedarse en la capa de infraestructura como base de las entidades JPA. Los modelos de dominio no deben extenderla.

### Lombok

El repo de ejemplo usa Lombok (`@Getter`, `@Setter`, `@Builder`, etc.) en los modelos de dominio para reducir el boilerplate. Se recomienda adoptarlo en los POJOs del dominio durante el refactor.

### Orden estricto

No saltar fases. Si se empieza la FASE 3 sin terminar la FASE 2, los servicios de reserva que usan `Hold` van a tener imports rotos. Compilar siempre entre fases.
