import React, { useState } from 'react';
import { 
    Container, TextField, Button, Typography, Box, Paper, 
    Alert, Snackbar, CircularProgress, IconButton 
} from '@mui/material';
import type { AlertColor } from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import SaveIcon from '@mui/icons-material/Save';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

interface SnackbarState {
    open: boolean;
    message: string;
    severity: AlertColor;
}

const RegistroCliente: React.FC = () => {
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);
    const [formData, setFormData] = useState({
        nombreCompleto: '',
        numeroDocumento: '',
        email: '',
        telefono: ''
    });
    const [errores, setErrores] = useState<{ [key: string]: string }>({});
    const [snackbar, setSnackbar] = useState<SnackbarState>({ open: false, message: '', severity: 'success' });

    // Validación en tiempo real (RF-02.1: Validar campos obligatorios)
    const validarCampo = (nombre: string, valor: string) => {
        if (nombre === 'nombreCompleto' && valor.length < 5) {
            return 'Nombre completo debe tener al menos 5 caracteres';
        }
        if (nombre === 'numeroDocumento' && !/^[0-9]{7,10}$/.test(valor)) {
            return 'Documento debe tener entre 7 y 10 dígitos numéricos';
        }
        if (nombre === 'email' && valor && !/^\S+@\S+\.\S+$/.test(valor)) {
            return 'Formato de email inválido';
        }
        return '';
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
        const error = validarCampo(name, value);
        setErrores({ ...errores, [name]: error });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        const erroresFinales: { [key: string]: string } = {};
        Object.keys(formData).forEach(key => {
            const error = validarCampo(key, formData[key as keyof typeof formData]);
            if (error) erroresFinales[key] = error;
        });
        if (!formData.nombreCompleto) erroresFinales.nombreCompleto = 'Campo obligatorio';
        if (!formData.numeroDocumento) erroresFinales.numeroDocumento = 'Campo obligatorio';
        setErrores(erroresFinales);
        if (Object.keys(erroresFinales).length > 0) return;

        setLoading(true);
        try {
            const response = await axios.post('http://localhost:8080/api/v1/clientes/registrar', formData);
            setSnackbar({ open: true, message: `✅ ${response.data.mensaje}. Expediente: ${response.data.expedienteId}`, severity: 'success' });
            setTimeout(() => navigate(`/checklist/${response.data.clienteId || response.data.expedienteId}`), 2000);
        } catch (error: unknown) {
            console.error(error);
            let mensajeError = 'Error de conexión con el servidor';
            if (typeof error === 'object' && error !== null && 'response' in error) {
                const maybeError = error as { response?: { data?: { detalle?: string; error?: string } } };
                if (maybeError.response?.data?.detalle) mensajeError = maybeError.response.data.detalle;
                else if (maybeError.response?.data?.error) mensajeError = maybeError.response.data.error;
            }
            setSnackbar({ open: true, message: `❌ ${mensajeError}`, severity: 'error' });
        } finally {
            setLoading(false);
        }
    };

    return (
        <Box sx={{ bgcolor: '#f5f5f5', minHeight: '100vh', pb: 4 }}>
            <Container maxWidth="md">
                <Paper elevation={3} sx={{ p: 4, mt: 4 }}>
                    <Box sx={{ display: 'flex', alignItems: 'center', mb: 3 }}>
                        <IconButton onClick={() => navigate('/')} sx={{ mr: 2 }}>
                            <ArrowBackIcon />
                        </IconButton>
                        <Typography variant="h5" sx={{ color: '#003366', fontWeight: 'bold' }}>
                            Registro de Cliente (RF-02.1)
                        </Typography>
                    </Box>
                    <Typography variant="body2" color="text.secondary" sx={{ mb: 3 }}>
                        Creación de Expediente Digital Único. Todos los campos con * son obligatorios.
                    </Typography>

                    <form onSubmit={handleSubmit}>
                        <TextField
                            fullWidth
                            label="Nombre Completo / Razón Social *"
                            name="nombreCompleto"
                            value={formData.nombreCompleto}
                            onChange={handleChange}
                            error={!!errores.nombreCompleto}
                            helperText={errores.nombreCompleto}
                            margin="normal"
                            disabled={loading}
                        />
                        <TextField
                            fullWidth
                            label="Número de Documento (CI/NIT) *"
                            name="numeroDocumento"
                            value={formData.numeroDocumento}
                            onChange={handleChange}
                            error={!!errores.numeroDocumento}
                            helperText={errores.numeroDocumento || 'Solo números, sin guiones'}
                            margin="normal"
                            disabled={loading}
                        />
                        <TextField
                            fullWidth
                            label="Correo Electrónico"
                            name="email"
                            type="email"
                            value={formData.email}
                            onChange={handleChange}
                            error={!!errores.email}
                            helperText={errores.email}
                            margin="normal"
                            disabled={loading}
                        />
                        <TextField
                            fullWidth
                            label="Teléfono de Contacto"
                            name="telefono"
                            value={formData.telefono}
                            onChange={handleChange}
                            margin="normal"
                            disabled={loading}
                        />

                        <Box sx={{ display: 'flex', justifyContent: 'flex-end', mt: 4 }}>
                            <Button 
                                variant="outlined" 
                                onClick={() => navigate('/')} 
                                sx={{ mr: 2 }}
                                disabled={loading}
                            >
                                Cancelar
                            </Button>
                            <Button 
                                type="submit" 
                                variant="contained" 
                                startIcon={loading ? <CircularProgress size={20} color="inherit" /> : <SaveIcon />}
                                disabled={loading}
                                sx={{ bgcolor: '#003366' }}
                            >
                                {loading ? 'Guardando...' : 'Crear Expediente'}
                            </Button>
                        </Box>
                    </form>
                </Paper>
            </Container>

            <Snackbar 
                open={snackbar.open} 
                autoHideDuration={6000} 
                onClose={() => setSnackbar({ ...snackbar, open: false })}
                anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
            >
                <Alert severity={snackbar.severity} sx={{ width: '100%' }}>
                    {snackbar.message}
                </Alert>
            </Snackbar>
        </Box>
    );
};

export default RegistroCliente;
