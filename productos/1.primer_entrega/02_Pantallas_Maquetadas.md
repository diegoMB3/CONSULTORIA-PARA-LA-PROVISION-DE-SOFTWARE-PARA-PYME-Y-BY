## 🎨 COMPONENTE 2: PANTALLAS MAQUETADAS DE FORMULARIOS (DISEÑO DE INTERFACES)

### 🖥️ 1. Pantalla Principal (Dashboard del Asesor)
![Texto alternativo](C:\Users\HP VICTUS\OneDrive\Documentos\software\CONSULTORIA-PARA-LA-PROVISION-DE-SOFTWARE-PARA-PYME-Y-BY\productos\imagenes\dashboard.png)

### 📄 2. Formulario General: Registro y Check-list Documental


### 📊 3. Formulario de Volteo de Balances e Indicadores
* **Interfaz de Doble Entrada:** Columnas contables agrupadas para Activo, Pasivo y Patrimonio. Los totales de cada grupo se calculan automáticamente en tiempo real mediante programación reactiva en la UI.
* **Caja de Alerta Contable:** Si el total Activo no es exactamente igual a la suma de Pasivo + Patrimonio, se despliega un banner de advertencia dinámico: *"⚠️ Descuadre Contable Detectado: El sistema no permitirá la confirmación hasta que la diferencia sea menor o igual a 10 Bs."*
* **Panel de Ratios (Output):** Cuadro resumen que muestra los índices calculados por el backend (Liquidez, Endeudamiento, Solvencia) acompañados por un tag semafórico inteligente de riesgo.

### ⚙️ 4. Formularios Especializados Sectoriales
* **Interfaz Agrícola/Pecuaria:** Pestañas de captura dinámica que se expanden según la cantidad de cultivos o tipos de hato ganadero seleccionados. Incluye campos interactivos para tasas de mortalidad/natalidad y ciclos proyectados.
* **Interfaz de Producción:** Formulario simplificado de estructura de costos (Materia prima, mano de obra directa, costos indirectos). Muestra un medidor dinámico en la parte inferior con el Margen de Utilidad Neta; si se sitúa por debajo del $10.0\%$, el campo se tiñe de color rojo y deshabilita la opción de guardar la evaluación.

---