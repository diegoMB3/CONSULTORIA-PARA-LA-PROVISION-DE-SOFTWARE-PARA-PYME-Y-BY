# Filosofía Arquitectónica

## Event Sourcing y CQRS (Command Query Responsibility Segregation)

Esta es quizás la arquitectura más poderosa para la banca. En lugar de guardar solo el "estado actual" de una cuenta (ej. Saldo: $100), guardas la **historia completa de eventos** que llevaron a ese saldo (ej. +$50, -$20, +$70).

- **Por qué en Banca:** Proporciona una pista de auditoría perfecta por diseño. Nunca pierdes información y puedes "reconstruir" el estado de cualquier cuenta en cualquier momento del pasado.

- **CQRS:** Separa la lógica de **escritura** (transacciones) de la de **lectura** (consultas de saldo), optimizando el rendimiento de la aplicación.

- **Principios:** Almacenar el estado de una aplicación como una secuencia cronológica e inmutable de elementos en lugar de guardar el estado normal.

## Patrón Arquitectónico

Para implementar un sistema bancario moderno, especialmente si vas a utilizar **Event Sourcing** (que es una técnica de persistencia), el patrón arquitectónico "paraguas" que mejor encaja es la **Arquitectura de Microservicios Basada en Eventos (Event-Driven Microservices)**.

Sin embargo, para estructurar cada uno de esos servicios, el patrón específico que te recomiendo es la **Arquitectura Hexagonal (Puertos y Adaptadores)** combinada con **CQRS**.

Aquí te detallo por qué esta combinación es el "estándar de oro" para la banca:

## Estructura Interna: Arquitectura Hexagonal

Imagina que el "Corazón" de tu código son las reglas bancarias (cómo se calcula un interés, quién puede transferir). Este corazón no debe saber nada de si usas una base de datos Oracle, Postgres o si el cliente usa una App móvil o una Web.

- **El Núcleo (Dominio):** Aquí vive la lógica pura.
- **Puertos:** Interfaces que dicen "necesito guardar un dato" o "necesito enviar una notificación".
- **Adaptadores:** Implementaciones reales (el adaptador de base de datos, el adaptador de Swift para pagos internacionales, etc.).

**¿Por qué elegirlo para un banco?** Los bancos cambian de proveedores y de regulaciones constantemente. Con este patrón, puedes cambiar el proveedor de mensajes SMS o la base de datos **sin tocar la lógica de negocio**, reduciendo el riesgo de introducir errores financieros.

# Repository Pattern

## 1. Definición

El Repository Pattern es un patrón de diseño que pertenece a la categoría de patrones de acceso a datos. Su propósito es **abstraer y encapsular la lógica de persistencia**, proporcionando una interfaz que permite acceder a los datos sin exponer los detalles de almacenamiento.

Actúa como un intermediario entre la capa de dominio (lógica de negocio) y la capa de datos, permitiendo que ambas evolucionen de manera independiente.

## 2. Objetivo

El objetivo principal del patrón es:

- Desacoplar la lógica de negocio del mecanismo de acceso a datos
- Centralizar las operaciones de persistencia
- Facilitar el mantenimiento y la evolución del sistema
- Mejorar la estabilidad del código

## 3. Estructura

El patrón se compone de los siguientes elementos:

### 3.1 Entidades (Domain Models)

Representan los objetos del dominio del sistema. Contienen los datos y, en algunos casos, reglas básicas de negocio.

Ejemplo:
- Usuario
- Cuenta
- Transacción

### 3.2 Interfaz del repositorio

Define las operaciones disponibles para interactuar con las entidades, sin especificar cómo se implementan.

Características:
- Expone métodos de tipo CRUD (Create, Read, Update, Delete)
- Define contratos claros para el acceso a datos
- No contiene lógica técnica (SQL, conexiones, etc.)

### 3.3 Implementación del repositorio

Contiene la lógica concreta de acceso a datos.

Características:
- Interactúa con la base de datos (por ejemplo, PostgreSQL)
- Puede usar ORM (como JPA/Hibernate) o consultas SQL directas
- Es reemplazable sin afectar la lógica de negocio

## 4. Funcionamiento

El flujo típico es el siguiente:

1. La capa de negocio solicita datos al repositorio
2. El repositorio ejecuta la operación correspondiente
3. La implementación accede a la base de datos
4. Se devuelve el resultado a la capa de negocio

La lógica de negocio nunca interactúa directamente con la base de datos.

# Stack Tecnológico Propuesto

El sistema se construye sobre un stack tecnológico moderno y robusto, compuesto por tecnologías ampliamente utilizadas en entornos empresariales, lo que garantiza escalabilidad, mantenibilidad y seguridad.

## Frontend: Angular (Aplicación Web)

El frontend se desarrolla utilizando Angular, un framework de desarrollo web basado en TypeScript que permite la construcción de aplicaciones de una sola página (SPA). Angular proporciona una arquitectura estructurada basada en componentes, facilitando la reutilización de código, la mantenibilidad y la organización del proyecto.

## Backend: Java

El backend se implementa en Java, un lenguaje ampliamente adoptado en sistemas empresariales críticos debido a su estabilidad, seguridad y alto rendimiento. Se complementa con Spring Boot para la creación de servicios web REST.

## Base de Datos: PostgreSQL

Para la persistencia de datos se utiliza PostgreSQL, un sistema de gestión de bases de datos relacional de código abierto reconocido por su cumplimiento con estándares SQL, ACID e integridad referencial.

## Integración del Stack

La comunicación entre el frontend y el backend se realiza mediante APIs REST, donde Angular consume los servicios expuestos por el backend en Java. A su vez, el backend gestiona la lógica de negocio y la interacción con la base de datos PostgreSQL.