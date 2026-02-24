# 2. Requerimientos Funcionales

## Módulo de Autenticación

### RF-AUTH-01: Validación de credenciales (Login)
**Como** usuario registrado  
**Quiero** ingresar mis credenciales de acceso  
**Para** acceder a las funcionalidades protegidas del sistema.  

**Criterios de Aceptación:**
- [ ] El sistema debe validar que el correo y la contraseña coincidan con los registros en la base de datos.
- [ ] El sistema debe denegar el acceso y bloquear la cuenta temporalmente tras **3 intentos fallidos**.
- [ ] La contraseña debe estar enmascarada durante el ingreso de datos.

---

### RF-CORP-02: Registro de empresas clientes
**Como** administrador del sistema  
**Quiero** registrar nuevas empresas clientes  
**Para** incorporarlas a la plataforma y permitirles utilizar los servicios.  

**Criterios de Aceptación:**
- [ ] El sistema debe solicitar campos obligatorios (Nombre legal, NIT/Tax ID, dirección y contacto).
- [ ] El sistema debe validar que el formato de los datos ingresados sea correcto.
- [ ] Tras un registro exitoso con datos válidos, la empresa debe visualizarse en el sistema.

---

### RF-USER-03: Gestión de usuarios y roles
**Como** administrador  
**Quiero** crear usuarios y asignarles roles específicos  
**Para** garantizar que cada persona tenga acceso solo a las funciones que le corresponden.  

**Criterios de Aceptación:**
- [ ] El sistema debe permitir la creación, edición y desactivación de usuarios.
- [ ] Se deben poder asignar roles predefinidos con permisos específicos.
- [ ] El sistema debe validar la asignación correcta de permisos según el rol seleccionado.

---

### RF-REP-04: Generación de reportes de diagnóstico
**Como** analista de datos  
**Quiero** generar reportes basados en los diagnósticos realizados  
**Para** documentar y compartir los resultados con los interesados.  

**Criterios de Aceptación:**
- [ ] El sistema debe procesar los datos de diagnóstico existentes.
- [ ] El reporte debe estar disponible para **descarga en formato PDF**.
- [ ] El documento generado debe mantener la estructura y diseño oficial de la empresa.

---

### RF-CORP-05: Actualización de información empresarial
**Como** gestor de cuenta  
**Quiero** modificar los datos de una empresa existente  
**Para** mantener la información de contacto y legal actualizada.  

**Criterios de Aceptación:**
- [ ] El sistema debe permitir la edición de la información empresarial registrada.
- [ ] Los cambios deben guardarse correctamente en la base de datos.
- [ ] La información actualizada debe reflejarse de manera inmediata en todos los módulos del sistema.
