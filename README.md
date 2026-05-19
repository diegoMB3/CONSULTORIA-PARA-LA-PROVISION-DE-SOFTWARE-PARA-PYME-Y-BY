# Digitalizacion del proceso Crediticio PYME y Banca Empresa (BE)

1. Descripción del Proyecto
Este proyecto consiste en el desarrollo de una plataforma virtual transaccional para el Banco de Desarrollo Productivo (BDP S.A.M.)
. Su objetivo primordial es centralizar la información del proceso crediticio, eliminando los cuellos de botella generados por el uso manual de hojas de cálculo Excel y el "pasamanos físico" de legajos
.
2. Justificación de Negocio (Business Case)
Actualmente, el flujo de aprobación es fragmentado y caótico
. El sistema busca:
Automatizar cálculos financieros que hoy se hacen a mano
.
Mejorar la trazabilidad de las evaluaciones
.
Optimizar la toma de decisiones del Comité de Crédito mediante expedientes digitales bloqueados
.
3. Capacidades Principales (Core Features)
Modo Offline-First: Permite el registro de datos, captura de fotos comprimidas y cuadres financieros en zonas rurales sin conexión, sincronizando automáticamente al detectar señal
.
Motores de Cálculo Paramétricos: Automatización de proyecciones para los sectores agrícola (rendimiento por zona/cultivo) y pecuario (evolución dinámica del hato)
.
Validación Financiera Estricta: El sistema bloquea automáticamente expedientes con descuadres contables superiores a +/- 10 Bs.
.
Auditoría Inalterable: Registro de logs de "solo lectura" que capturan los valores "antes y después" de cada modificación con precisión de milisegundos
.
4. Arquitectura y Tecnologías
Despliegue: On-Premise en el Data Center del Banco por soberanía de datos (normativa ASFI)
.
Infraestructura: Arquitectura Stateless basada en contenedores Docker
.
Base de Datos: Soporte para Oracle o PostgreSQL
.
Seguridad: Cifrado AES-256, autenticación vía JWT y protección de credenciales mediante Android Keystore
.
Hardware: Optimizado para 200 tablets Android gestionadas por Microsoft Intune MDM
.
5. Objetivos Estratégicos (KPIs)
Reducción del 30% en el "Time-to-Yes" (tiempo de respuesta al cliente)
.
Migración del 100% de la cartera nueva al sistema digital durante el primer año
.
Cero observaciones críticas en auditorías regulatorias
.
6. Equipo del Proyecto
Gestor del Proyecto: Diego Méndez
.
Líder de Desarrollo: Leonardo Radek Condori Yucra
.
Ingenieros de Software: Juan Daniel Ancieta, Christian Fabian Gonzales Mamani, Maializ Mamani Quispe
.
7. Hitos Principales (Cronograma)
El proyecto tiene una duración estimada de 260 días hábiles
:
Día 40: Análisis funcional y diseño de pantallas
.
Día 80: Implementación del módulo de captura digital
.
Día 140: Desarrollo de motores de cálculo
.
Día 240: Pruebas piloto en Santa Cruz y La Paz
.
Día 260: Transferencia tecnológica y cierre
.