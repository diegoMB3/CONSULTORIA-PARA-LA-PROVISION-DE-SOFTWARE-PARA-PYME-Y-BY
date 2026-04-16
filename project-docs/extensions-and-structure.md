# Documentación de extensiones y estructura del proyecto

Esta carpeta es únicamente de documentación y no modifica el código del proyecto.

## 1. Extensiones / herramientas usadas en el proyecto

### Backend (`backend/`)
- `Spring Boot 3.5.13`
  - Se usa como base del backend.
- `Java 17`
  - El proyecto actual está configurado con `<java.version>17</java.version>` en `backend/pom.xml`.
- `Maven Wrapper` (`mvnw`, `mvnw.cmd`)
  - Garantiza que el proyecto use una versión controlada de Maven.
- Dependencias principales:
  - `spring-boot-starter-web`
  - `spring-boot-starter-data-jpa`
  - `spring-boot-starter-validation`
  - `spring-boot-starter-actuator`
  - `spring-security-crypto`
  - `axon-spring-boot-starter` (Axon Framework para CQRS/Event Sourcing)
  - `lombok` (procesador de anotaciones)
  - `postgresql` y `h2` para bases de datos
  - `bouncycastle` para cifrado AES-256

### Frontend (`web-dashboard/`)
- `React 19`
- `Vite` como bundler y servidor de desarrollo
- `TypeScript 6`
- `MUI 9` (`@mui/material`, `@mui/icons-material`)
- `React Router DOM 7`
- `axios` para llamadas HTTP
- `jspdf` para generación de PDF
- `ESLint` con plugins de React

### Configuración de VS Code
- No se encontró un archivo `extensions.json` en `.vscode/`.
- Existe `.vscode/settings.json` con la configuración:
  - `java.compile.nullAnalysis.mode`: `automatic`

## 2. Estructura principal del proyecto

### Nivel raíz
- `.github/`
- `.mvn/`
- `apache-maven-3.8.8/`
- `backend/`
- `infra/`
- `mobile/`
- `web-dashboard/`
- `.gitignore`
- `HELP.md`

### `backend/`
- `pom.xml`
- `mvnw`, `mvnw.cmd`
- `src/main/java/bo/gob/bdp/sam/` (código fuente Java)
- `src/main/resources/` (configuración, plantillas, recursos estáticos)
- `src/test/java/` (pruebas Java)
- `target/` (artefactos compilados y generados)

### `web-dashboard/`
- `package.json`
- `package-lock.json`
- `vite.config.ts`
- `tsconfig.json`, `tsconfig.app.json`, `tsconfig.node.json`
- `src/` (código fuente React/TypeScript)
- `public/` (activos públicos)
- `dist/` (build generado)

## 3. Notas importantes

- La documentación creada bajo `project-docs/` no afecta al proyecto ni a sus builds.
- La carpeta `project-docs/` es una referencia estática para el equipo.
- Si deseas, puedo agregar información adicional sobre las dependencias de `backend/pom.xml` o los scripts de `web-dashboard/package.json`.
