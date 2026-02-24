# 3. Requerimientos No Funcionales (Atributos de Calidad)

## 3.1 Seguridad (DevSecOps)
* **RNF-SEC-01 (Protección de Credenciales):** Las contraseñas no deben guardarse en texto plano bajo ninguna circunstancia.
* **RNF-SEC-02 (Algoritmo de Hashing):** La base de datos debe cifrar las contraseñas utilizando algoritmos de hashing robustos como `bcrypt` o `Argon2` para asegurar la integridad de los datos de acceso.

## 3.2 Rendimiento (Escalabilidad)
* **RNF-PERF-01 (Eficiencia de Consultas):** El sistema debe garantizar un tiempo de respuesta en búsquedas y filtrados de menos de **1 segundo**, incluso con una carga de hasta 10,000 registros simultáneos.

## 3.3 Compatibilidad (UX/UI)
* **RNF-COMP-01 (Diseño Adaptativo):** La interfaz de usuario debe ser **Web Responsive**, asegurando una visualización y funcionalidad óptima tanto en dispositivos móviles como en navegadores de escritorio (Desktop).