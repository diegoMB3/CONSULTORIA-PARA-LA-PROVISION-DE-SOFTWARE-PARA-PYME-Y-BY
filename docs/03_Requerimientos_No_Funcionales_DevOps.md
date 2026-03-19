### RNF-01: Seguridad de Datos (Cifrado)
**Como** Oficial de Seguridad  
**Quiero** que la información en las tablets esté cifrada  
**Para** proteger los datos del cliente en caso de pérdida o robo del dispositivo.  

**Criterios de Aceptación:**
- [ ] El sistema debe cifrar los datos sensibles utilizando el algoritmo AES-256.
- [ ] El sistema debe almacenar claves criptográficas en Android Keystore.
- [ ] El sistema debe impedir el acceso a datos sin autenticación válida.
- [ ] El sistema debe cumplir con pruebas de seguridad sin vulnerabilidades críticas (OWASP Top 10).
- [ ] El tiempo de cifrado/descifrado no debe exceder 500 ms por operación.

---

### RNF-02: Validación de Integridad de Datos
**Como** Analista de Riesgos  
**Quiero** que el sistema bloquee el envío de balances descuadrados  
**Para** garantizar la calidad de la información financiera.  

**Criterios de Aceptación:**
- [ ] El sistema debe validar que la diferencia entre activos y pasivos no supere ±10 Bs.
- [ ] El sistema debe mostrar una alerta clara en caso de descuadre.
- [ ] El sistema debe impedir el envío de datos si el descuadre supera el umbral permitido.
- [ ] El tiempo de validación no debe exceder 1 segundo.
- [ ] El sistema debe registrar los intentos fallidos de envío por descuadre.

---

### RNF-03: Rendimiento del Sistema
**Como** Analista  
**Quiero** obtener resultados de búsqueda instantáneos  
**Para** no hacer esperar al cliente durante la atención.  

**Criterios de Aceptación:**
- [ ] El tiempo de respuesta de búsquedas debe ser menor a 2 segundos en el 95% de los casos.
- [ ] El sistema debe soportar al menos 100 usuarios concurrentes sin degradación significativa.
- [ ] El tiempo de carga inicial de la aplicación no debe superar los 3 segundos.
- [ ] El sistema debe mantener uso de CPU menor al 70% en condiciones normales.
- [ ] El sistema debe contar con monitoreo de rendimiento en tiempo real.

---

### RNF-04: Disponibilidad y Tolerancia a Fallos
**Como** Gerente de Operaciones  
**Quiero** que el sistema sea tolerante a fallos  
**Para** asegurar la continuidad del servicio.  

**Criterios de Aceptación:**
- [ ] El sistema debe garantizar una disponibilidad mínima del 99.5% mensual.
- [ ] El sistema debe contar con redundancia en servidores y base de datos.
- [ ] El sistema debe recuperar operaciones en menos de 5 minutos ante fallos.
- [ ] El sistema debe realizar backups automáticos diarios.
- [ ] El sistema debe contar con monitoreo y alertas de caídas en tiempo real.

---

### RNF-05: Usabilidad y Consistencia Visual
**Como** Usuario  
**Quiero** navegar en una interfaz familiar y estandarizada  
**Para** reducir la curva de aprendizaje.  

**Criterios de Aceptación:**
- [ ] El sistema debe seguir la línea gráfica oficial del BDP (colores, tipografía, componentes).
- [ ] El sistema debe mantener consistencia visual en todas las pantallas.
- [ ] El sistema debe permitir completar tareas clave en máximo 3 clics.
- [ ] El sistema debe lograr al menos 80% de satisfacción en pruebas de usuario.
- [ ] El sistema debe incluir validaciones visuales claras (errores, confirmaciones).

---

### RNF-07: Compatibilidad y Multi-dispositivo
**Como** Asesor de Campo  
**Quiero** usar la aplicación en mi tablet Android  
**Para** trabajar directamente en campo.  

**Criterios de Aceptación:**
- [ ] El sistema debe ser compatible con Android versión 8.0 o superior.
- [ ] El sistema debe adaptarse a resoluciones de tablet (mínimo 7 pulgadas).
- [ ] El sistema debe funcionar en modo offline con sincronización posterior.
- [ ] El sistema debe mantener funcionalidad completa en modo táctil.
- [ ] El sistema debe sincronizar datos en menos de 10 segundos al recuperar conexión.