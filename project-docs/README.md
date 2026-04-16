# Documentación del Proyecto BDP-SAM

## 1. Extensiones recomendadas en VS Code

Estas extensiones ayudan a trabajar con el backend Java y el frontend React/TypeScript.

### Backend Java / Spring Boot
- **Language Support for Java(TM) by Red Hat**
  - Proporciona soporte de lenguaje Java y conexión al servidor de lenguaje.
- **Spring Boot Extension Pack**
  - Incluye herramientas para Spring Boot, edición y ejecución de aplicaciones.
- **Debugger for Java**
  - Permite depurar aplicaciones Java en VS Code.
- **Maven for Java**
  - Ayuda a ejecutar comandos Maven, compilar proyectos y administrar dependencias.
- **Lombok Annotations Support for VS Code**
  - Permite que el editor reconozca las anotaciones de Lombok como `@Data` y `@Value`.

### Frontend React / TypeScript
- **ESLint**
  - Valida el código JavaScript/TypeScript y ayuda a mantener la calidad.
- **TypeScript and JavaScript Language Features**
  - Viene integrado con VS Code y proporciona autocompletado y diagnóstico.
- **Prettier - Code formatter** (opcional)
  - Formatea código React/TypeScript automáticamente.

### Otros útiles
- **Material Icon Theme**
  - Mejora los íconos en el explorador de archivos.
- **GitLens**
  - Ayuda con el historial de Git y revisiones de código.

## 2. Cómo activar las extensiones

1. Abre VS Code.
2. Ve al panel de extensiones (icono de cuadrito o `Ctrl+Shift+X`).
3. Busca cada extensión por nombre.
4. Haz clic en `Instalar`.
5. Después de instalar, recarga VS Code si se solicita.
6. Para Java, abre el proyecto completo y espera que el servidor de lenguaje inicie. En la barra de estado debe mostrarse la actividad de Java.
7. Para ESLint, asegúrate de tener un archivo de configuración (`eslint.config.js`) en la raíz del proyecto. VS Code debería activar ESLint automáticamente.

> Nota: si alguna extensión no funciona inmediatamente, abre cualquier archivo `.java` o `.tsx` para forzar el inicio del servidor de lenguaje.

## 3. Estructura del proyecto

El proyecto está organizado en varias carpetas principales:

```
BDP-SAM-Project/
├── backend/
│   ├── pom.xml
│   ├── src/main/java/bo/gob/bdp/sam/
│   │   ├── adapters/in/web/
│   │   ├── core/application/command/
│   │   ├── core/domain/aggregate/
│   │   └── core/domain/event/
│   ├── src/main/resources/application.properties
│   └── .mvn/ (wrapper de Maven)
├── web-dashboard/
│   ├── package.json
│   ├── tsconfig.json
│   ├── vite.config.ts
│   ├── src/
│   │   ├── pages/
│   │   └── App.tsx
│   └── public/
├── infra/
│   └── docker-compose.yml
├── mobile/
│   └── (estructura del proyecto móvil pendiente)
└── project-docs/
    └── README.md
```

### Backend
- `backend/pom.xml` define dependencias y plugins para Spring Boot, Axon, JPA y más.
- `backend/src/main/java` contiene los paquetes principales:
  - `adapters.in.web`: controladores REST de entrada.
  - `core.application.command`: comandos CQRS.
  - `core.domain.aggregate`: agregados de dominio.
  - `core.domain.event`: eventos inmutables.
- `backend/src/main/resources/application.properties` contiene la configuración de la aplicación.

### Frontend
- `web-dashboard/package.json` define dependencias como React, MUI, Axios, React Router y Vite.
- `web-dashboard/src/App.tsx` define las rutas de la aplicación web.
- `web-dashboard/src/pages/` contiene las páginas principales:
  - `MenuPrincipal.tsx`
  - `RegistroCliente.tsx`
  - `ChecklistDocumentos.tsx`
  - `VolteoBalances.tsx`
  - `EvaluacionCliente.tsx`

### Infraestructura
- `infra/docker-compose.yml` es la definición de servicios que pueden usarse para PostgreSQL, Axon Server y otros servicios.

## 4. Qué se hizo en el proyecto

### Backend
- Inicializamos un servicio Spring Boot con Maven.
- Agregamos CQRS/Event Sourcing usando Axon Framework.
- Implementamos:
  - Registro de clientes con validación y prevención de duplicados.
  - Checklist de documentos con bloqueo si faltan documentos obligatorios.
  - Volteo de balances con validación de descuadre de ±10 Bs.
- Creamos los componentes:
  - `BalanceAggregate` con reglas contables.
  - `ActualizarBalanceCommand` para actualizar balances.
  - `BalanceActualizadoEvent` como evento inmutable.
  - `BalanceController` para exponer los endpoints REST.

### Frontend
- Creamos una aplicación React con Vite.
- Usamos Material UI para la interfaz.
- Implementamos páginas para:
  - menú principal
  - registro de cliente
  - checklist de documentos
  - volteo de balances
  - evaluación cliente
- Configuramos rutas con React Router.
- Integramos llamadas al backend usando Axios.

### Limpieza y validación
- Eliminamos artefactos generados: `target/`, `dist/`, `node_modules/`, archivos ZIP temporales.
- Compilamos y validamos:
  - Backend con Maven.
  - Frontend con `npm run build`.

## 5. Cómo iniciar el proyecto

### Backend
1. Navega a `backend/`.
2. Configura `JAVA_HOME` a Java 17.
3. Ejecuta: `c:\Users\ADMIN\BDP-SAM-Project\apache-maven-3.8.8\bin\mvn.cmd spring-boot:run`

### Frontend
1. Navega a `web-dashboard/`.
2. Instala dependencias si no están instaladas: `npm install`
3. Ejecuta: `npm run dev`
4. Abre `http://localhost:5173`

## 6. Consejos de uso

- Usa la ruta `/volteo-balances/1234567` para probar el formulario de balance.
- Usa `/registro-cliente` para crear un cliente y luego probar el checklist.
- El backend usa H2 en memoria para desarrollo, lo que evita depender de PostgreSQL mientras pruebas.

---

Esta documentación está en la carpeta `project-docs` para que puedas leerla con facilidad en cualquier momento y entender cómo está organizado el proyecto y qué extensiones son útiles para trabajar en él.
