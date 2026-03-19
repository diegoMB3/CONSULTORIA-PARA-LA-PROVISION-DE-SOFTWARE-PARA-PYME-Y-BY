### RF-AUTH-01: Pantalla Principal (Repositorio)
**Como** analista  
**Quiero** ver un menú con todos los modelos de evaluación  
**Para** seleccionar el adecuado según el rubro del cliente.  

**Criterios de Aceptación:**
- [ ] El sistema debe mostrar todos los modelos de evaluación disponibles en un menú principal.
- [ ] El sistema debe permitir la navegación hacia cada módulo desde el menú.
- [ ] El sistema debe mostrar el nombre y descripción breve de cada modelo.
- [ ] El sistema debe ser accesible según el rol del usuario.

---

### RF-02.1: Registro de Cliente
**Como** asesor  
**Quiero** capturar los datos generales del cliente  
**Para** crear un expediente digital único.  

**Criterios de Aceptación:**
- [ ] El sistema debe permitir ingresar datos personales y financieros del cliente.
- [ ] El sistema debe validar campos obligatorios antes de guardar.
- [ ] El sistema debe evitar registros duplicados.
- [ ] El sistema debe generar un identificador único por cliente.

---

### RF-02.2: Checklist de Documentos
**Como** operador  
**Quiero** marcar los documentos físicos recibidos  
**Para** validar que la carpeta cumple la normativa.  

**Criterios de Aceptación:**
- [ ] El sistema debe mostrar una lista de documentos requeridos.
- [ ] El usuario debe poder marcar documentos como recibidos.
- [ ] El sistema debe indicar si el checklist está completo o incompleto.
- [ ] El sistema debe impedir avanzar si faltan documentos obligatorios.

---

### RF-03.1: Volteo de Balances
**Como** analista financiero  
**Quiero** realizar el volteo de balances  
**Para** estandarizar la información contable.  

**Criterios de Aceptación:**
- [ ] El sistema debe permitir ingresar datos financieros.
- [ ] El sistema debe transformar automáticamente los datos al formato estándar.
- [ ] El sistema debe validar consistencia de los datos ingresados.
- [ ] El sistema debe permitir editar la información antes de guardar.

---

### RF-03.2: Indicadores Financieros
**Como** analista de riesgos  
**Quiero** visualizar índices de liquidez y solvencia automáticos  
**Para** medir el riesgo del crédito.  

**Criterios de Aceptación:**
- [ ] El sistema debe calcular automáticamente indicadores financieros.
- [ ] El sistema debe mostrar resultados en formato claro (tablas o gráficos).
- [ ] El sistema debe actualizar los indicadores ante cambios en los datos.
- [ ] El sistema debe permitir exportar los resultados.

---

### RF-03.3: Simulador de Pagos
**Como** asesor  
**Quiero** generar planes de pago personalizados  
**Para** ajustarlos a la capacidad real del cliente.  

**Criterios de Aceptación:**
- [ ] El sistema debe permitir ingresar monto, tasa y plazo.
- [ ] El sistema debe generar un cronograma de pagos.
- [ ] El sistema debe recalcular automáticamente ante cambios.
- [ ] El sistema debe permitir exportar el plan de pagos.

---

### RF-04.1: Evaluación Agrícola
**Como** evaluador agrícola  
**Quiero** registrar costos por hectárea y ciclos de cosecha  
**Para** calcular la rentabilidad del cultivo.  

**Criterios de Aceptación:**
- [ ] El sistema debe permitir registrar costos e ingresos agrícolas.
- [ ] El sistema debe calcular la rentabilidad automáticamente.
- [ ] El sistema debe soportar múltiples ciclos productivos.
- [ ] El sistema debe permitir guardar históricos.

---

### RF-05.1: Evaluación Pecuaria
**Como** evaluador pecuario  
**Quiero** proyectar el crecimiento del hato  
**Para** sustentar las ventas proyectadas.  

**Criterios de Aceptación:**
- [ ] El sistema debe permitir registrar cantidad de ganado.
- [ ] El sistema debe calcular crecimiento proyectado.
- [ ] El sistema debe permitir escenarios (cría/recría).
- [ ] El sistema debe mostrar resultados de proyección.

---

### RF-06.1: Evaluación Producción
**Como** analista industrial  
**Quiero** detallar los costos operativos de producción  
**Para** evaluar la eficiencia de la empresa.  

**Criterios de Aceptación:**
- [ ] El sistema debe permitir registrar costos de producción.
- [ ] El sistema debe clasificar costos (fijos/variables).
- [ ] El sistema debe calcular indicadores de eficiencia.
- [ ] El sistema debe permitir modificar registros.

---

### RF-07.1: Flujo de Aprobación
**Como** usuario  
**Quiero** derivar la solicitud al siguiente nivel jerárquico  
**Para** que el proceso de aprobación continúe.  

**Criterios de Aceptación:**
- [ ] El sistema debe permitir cambiar el estado de la solicitud.
- [ ] El sistema debe validar permisos según rol.
- [ ] El sistema debe registrar cada transición.
- [ ] El sistema debe notificar al siguiente responsable.

---

### RF-08.1: Generación de Reportes
**Como** analista  
**Quiero** generar el informe de evaluación en PDF  
**Para** presentarlo al Comité de Créditos.  

**Criterios de Aceptación:**
- [ ] El sistema debe generar reportes en formato PDF.
- [ ] El sistema debe incluir toda la información relevante.
- [ ] El sistema debe permitir vista previa.
- [ ] El sistema debe permitir descarga del archivo.

---

### RF-09.1: Integración API
**Como** administrador IT  
**Quiero** sincronizar los datos aprobados con el sistema CORE  
**Para** evitar errores de transcripción.  

**Criterios de Aceptación:**
- [ ] El sistema debe enviar datos mediante API REST.
- [ ] El sistema debe validar respuestas del CORE.
- [ ] El sistema debe registrar errores de sincronización.
- [ ] El sistema debe permitir reintentos.

---

### RF-10.1: Gestión de Archivos
**Como** usuario  
**Quiero** adjuntar fotos y documentos PDF  
**Para** respaldar digitalmente las evidencias.  

**Criterios de Aceptación:**
- [ ] El sistema debe permitir subir archivos.
- [ ] El sistema debe validar tipos de archivo.
- [ ] El sistema debe almacenar documentos de forma segura.
- [ ] El sistema debe permitir visualizar archivos cargados.

---

### RF-11.1: Auditoría
**Como** auditor  
**Quiero** ver el historial de cambios  
**Para** garantizar la integridad del proceso.  

**Criterios de Aceptación:**
- [ ] El sistema debe registrar todas las acciones del usuario.
- [ ] El sistema debe mostrar fecha, usuario y cambio realizado.
- [ ] El sistema debe impedir modificación de logs.
- [ ] El sistema debe permitir búsquedas en el historial.

---

### RF-12.1: Diseño Responsivo
**Como** asesor de campo  
**Quiero** usar la plataforma en mi tablet  
**Para** trabajar desde la unidad productiva.  

**Criterios de Aceptación:**
- [ ] El sistema debe adaptarse a diferentes tamaños de pantalla.
- [ ] El sistema debe funcionar en navegadores modernos.
- [ ] El sistema debe mantener usabilidad en dispositivos móviles.
- [ ] El sistema debe garantizar tiempos de respuesta adecuados.