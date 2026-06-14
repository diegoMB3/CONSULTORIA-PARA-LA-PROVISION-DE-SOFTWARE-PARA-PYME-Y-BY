## 📑 COMPONENTE 1: INFORME DE ANÁLISIS FUNCIONAL

### 1.1. Arquitectura del Sistema y Patrones de Diseño
Para dar cumplimiento a los requerimientos de **Extensibilidad del Sistema**, **Seguridad (JWT)** e **Integración Core vía API-REST**, la solución se ha diseñado bajo los siguientes estándares:

* **Arquitectura Hexagonal (Ports and Adapters):** Desacopla estrictamente la lógica de negocio de las tecnologías externas (Base de datos, Frameworks de presentación, APIs de terceros). Esto garantiza que el core de evaluación de créditos del BDP sea altamente mantenible y extensible de forma autónoma.
* **CQRS (Command Query Responsibility Segregation):** Se divide la aplicación en dos capas independientes:
  * *Capa de Comandos (Escritura):* Encargada del procesamiento de transacciones mutables (crear cliente, registrar balance, modificar monto de solicitud) resguardando las reglas de negocio en los Agregados (*Aggregates*).
  * *Capa de Consultas (Lectura):* Proyecciones optimizadas y síncronas hacia la base de datos relacional para alimentar la interfaz responsiva de usuario con tiempos de respuesta en tiempo real.
* **Event Sourcing (Abstracción mediante Axon Framework):** Cada cambio de estado en el proceso crediticio se almacena como una secuencia ordenada de eventos atómicos e inmutables. Esto provee, de manera nativa, el **Log/Pista de Auditoría** exigido por el TDR, permitiendo reconstruir el trazo exacto de qué asesor intervino, en qué fecha y qué modificación realizó.

### 1.2. Especificación de Reglas de Negocio por Módulos Solicitados

#### A. Módulo de Registro Inicial, Expediente Único y Check-list
* **Flujo Funcional:** El asesor inicia el registro del cliente en campo a través de la plataforma responsiva. El sistema genera un identificador de expediente unificado a través del patrón `CI-NIT + UUID`.
* **Regla de Validación:** El documento de identidad debe pasar por una validación sintáctica estricta (mínimo 7 caracteres) antes de iniciar el comando de persistencia.
* **Control Documental Normativo (ASFI):** El sistema inicializa un estado de validación obligatorio (Check-list) para los 5 documentos mandatorios: Cédula de Identidad Frente (`CI_FRENTE`), Reverso (`CI_REVERSO`), Factura de Agua (`FACTURA_AGUA`), Factura de Luz (`FACTURA_LUZ`) y Estado de Cuenta (`ESTADO_CUENTA`). El sistema expone un indicador lógico (`puedeAvanzar = false`) que bloquea el flujo hacia las fases de evaluación si no se cumple el check-list mínimo.

#### B. Módulo de Volteo de Balances e Indicadores Financieros
* **Flujo Funcional:** Permite digitalizar en campo los datos contables de la PyME para consolidar el Balance General.
* **Regla de Control (Restricción Antidescuadre):** El sistema calcula dinámicamente los subtotales de Activo Corriente/No Corriente, Pasivo Corriente/No Corriente y Patrimonio. Se implementa un validador que impide el cierre o confirmación del balance si la ecuación contable presenta un descuadre superior a $\pm10\text{ Bs}$.
* **Motor Analítico Financiero:** Almacenado el balance de forma conforme, el sistema gatilla automáticamente el cálculo de los siguientes ratios:
  * *Liquidez Corriente:* $\text{Activo Corriente} / \text{Pasivo Corriente}$
  * *Endeudamiento:* $(\text{Pasivo Total} / \text{Activo Total}) \times 100$
  * *Métricas Adicionales:* Solvencia, ROA y ROE.
  * *Semáforo de Riesgo:* El backend interpreta los resultados y asigna alertas visuales cualitativas (Ej: Liquidez $\ge 1.5 \rightarrow$ "✅ Saludable") para agilizar las decisiones del comité.

#### C. Motores de Evaluación Sectorial Productiva (Core BDP)
1. **Evaluación Agrícola:** El analista registra parámetros de superficie (hectáreas), tipo de cultivo, costos de insumos, mano de obra y ciclos productivos. El sistema procesa proyecciones financieras multiplicando costos e ingresos estimados en un horizonte paramétrico de 1 a 24 ciclos para emitir una conclusión automatizada de viabilidad ("Cultivo rentable" / "No rentable").
2. **Evaluación Pecuaria:** Permite modelar el desarrollo del hato ganadero (cría/recría). Recibe variables dinámicas como: cantidad inicial de animales, tasa de natalidad, tasa de mortalidad y costo de mantenimiento unitario, proyectando la población neta resultante y su rentabilidad económica.
3. **Evaluación de Producción (Industrial/Microempresa):** Destinado a la transformación de materia prima. Exige obligatoriamente el registro de ingresos superiores a cero.
   * *Restricción de Viabilidad:* El motor calcula de forma estricta el margen de utilidad neta ($\text{Ingresos} - \text{Costos Fijos y Variables}$). Si el margen resultante es inferior al $10.0\%$, el sistema bloquea la solicitud y la marca como rechazada por inviabilidad del sector.

#### D. Simulador de Planes de Pago
* **Flujo Funcional:** Motor financiero matemático que permite estructurar la propuesta de amortización en tiempo real frente al cliente.
* **Lógica de Cálculo:** Aplica el **Sistema de Amortización Francés** (cuotas constantes de capital e interés compuesto decreciente). Recibe de forma externa el monto solicitado, la tasa anual regulada y el plazo (hasta 360 meses). El sistema procesa la matriz celda por celda (`CuotaDetalle`) y realiza un ajuste por redondeo de centavos exclusivamente en la última cuota contra el saldo remanente para garantizar cuadratura cero en el Core Bancario.

---

