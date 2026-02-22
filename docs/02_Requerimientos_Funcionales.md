## Módulo de Autenticación

### RF-AUTH-01: Inicio de sesión de usuario
**Como** cliente registrado  
**Quiero** iniciar sesión utilizando mi correo y contraseña  
**Para** poder acceder a mi panel de control personal.  

**Criterios de Aceptación:**
- [ ] El sistema debe bloquear la cuenta temporalmente tras 5 intentos fallidos.
- [ ] La contraseña no debe mostrarse en texto plano al escribirse (enmascarada).
- [ ] Si las credenciales son válidas, redirigir a `/dashboard`.

### RF-AUTH-02: Recuperación de contraseña
(...)