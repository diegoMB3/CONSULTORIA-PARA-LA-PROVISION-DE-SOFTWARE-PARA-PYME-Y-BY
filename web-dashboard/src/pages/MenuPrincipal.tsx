import React, { useEffect, useState, type ReactNode } from 'react';
import { Container, Card, CardContent, Typography, AppBar, Toolbar, Box, Fab } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import AgricultureIcon from '@mui/icons-material/Agriculture';
import PetsIcon from '@mui/icons-material/Pets';
import FactoryIcon from '@mui/icons-material/Factory';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

// Mapeo de Iconos según el texto del backend
const iconMap: Record<string, ReactNode> = {
    '🌾 Evaluación Agrícola (RF-04.1)': <AgricultureIcon sx={{ fontSize: 60, color: '#2E7D32' }} />,
    '🐄 Evaluación Pecuaria (RF-05.1)': <PetsIcon sx={{ fontSize: 60, color: '#5D4037' }} />,
    '🏭 Evaluación Producción (RF-06.1)': <FactoryIcon sx={{ fontSize: 60, color: '#1565C0' }} />,
};

const MenuPrincipal: React.FC = () => {
    const navigate = useNavigate();
    const [modelos, setModelos] = useState<string[]>([]);
    const [loading, setLoading] = useState(true);

    // Al cargar la página, llamamos al Backend
    useEffect(() => {
        // NOTA: Asegúrate de que el Backend esté corriendo en el puerto 8080
        axios.get('http://localhost:8080/api/v1/modelos/menu')
            .then(response => {
                setModelos(response.data);
                setLoading(false);
            })
            .catch(error => {
                console.error("Error conectando al Backend (¿Docker está encendido?)", error);
                setLoading(false);
            });
    }, []);

    const handleSeleccionarModelo = (nombre: string) => {
        if (nombre.includes('Agrícola')) {
            navigate('/evaluacion-agricola/1234567');
            return;
        }
        if (nombre.includes('Pecuaria')) {
            navigate('/evaluacion-pecuaria/1234567');
            return;
        }
        if (nombre.includes('Producción') || nombre.includes('RF-06.1')) {
            navigate('/evaluacion-produccion/1234567');
            return;
        }
        if (nombre.includes('Volteo')) {
            navigate('/volteo-balances/1234567');
            return;
        }
        alert(`Flujo de trabajo: Redirigiendo a ${nombre} (En Desarrollo)`);
    };

    return (
        <Box sx={{ flexGrow: 1 }}>
            <AppBar position="static" sx={{ backgroundColor: '#003366' }}>
                <Toolbar>
                    <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                        BDP - Sistema de Análisis de Microcrédito (S.A.M.)
                    </Typography>
                    <Typography variant="body2">Usuario: Analista Senior</Typography>
                </Toolbar>
            </AppBar>
            <Container maxWidth="lg" sx={{ mt: 4 }}>
                <Typography variant="h4" gutterBottom sx={{ mb: 4, fontWeight: 'bold', color: '#003366' }}>
                    Repositorio de Modelos de Evaluación
                </Typography>
                <Typography variant="subtitle1" sx={{ mb: 3, color: 'gray' }}>
                    Seleccione el modelo según el rubro del cliente (RF-AUTH-01)
                </Typography>
                
                {loading ? (
                    <Typography>Cargando módulos financieros...</Typography>
                ) : (
                    <Box sx={{ display: 'flex', flexWrap: 'wrap', justifyContent: 'space-between', gap: 4 }}>
                        {modelos.map((modelo) => (
                            <Box key={modelo} sx={{ flex: '1 1 calc(33% - 32px)', minWidth: 280 }}>
                                <Card 
                                    onClick={() => handleSeleccionarModelo(modelo)}
                                    sx={{ 
                                        cursor: 'pointer', 
                                        transition: '0.3s', 
                                        '&:hover': { transform: 'scale(1.03)', boxShadow: 6 },
                                        minHeight: 220,
                                        display: 'flex',
                                        flexDirection: 'column',
                                        alignItems: 'center',
                                        justifyContent: 'center',
                                        borderTop: '5px solid #FFC107'
                                    }}
                                >
                                    <CardContent sx={{ textAlign: 'center' }}>
                                        {iconMap[modelo] || <FactoryIcon sx={{ fontSize: 60 }} />}
                                        <Typography variant="h6" component="div" sx={{ mt: 2, fontWeight: 'medium' }}>
                                            {modelo.replace(/\(.*?\)/g, '')} 
                                        </Typography>
                                        <Typography variant="caption" color="text.secondary">
                                            {modelo.includes('Agrícola') && 'Costos por hectárea y ciclos'}
                                            {modelo.includes('Pecuaria') && 'Proyección de hato ganadero'}
                                            {modelo.includes('Producción') && 'Costos operativos industriales'}
                                        </Typography>
                                    </CardContent>
                                </Card>
                            </Box>
                        ))}
                    </Box>
                )}
            </Container>
            <Fab 
                color="primary" 
                aria-label="add" 
                sx={{ position: 'fixed', bottom: 30, right: 30, bgcolor: '#FFC107', color: '#003366' }}
                onClick={() => navigate('/registro-cliente')}
            >
                <AddIcon />
            </Fab>
        </Box>
    );
};

export default MenuPrincipal;
