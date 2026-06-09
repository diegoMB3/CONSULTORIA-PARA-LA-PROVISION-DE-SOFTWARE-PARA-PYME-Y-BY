import React, { useState, useEffect } from 'react';
import {
    Container, Typography, Box, Paper, Grid, Card, CardContent,
    Chip, IconButton, Button, CircularProgress, Divider
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import { useNavigate, useParams } from 'react-router-dom';
import axios from 'axios';

interface IndicadoresData {
    clienteId: string;
    liquidezCorriente: number;
    endeudamiento: number;
    solvencia: number;
    roa: number;
    roe: number;
    interpretacion: {
        liquidezCorriente: string;
        endeudamiento: string;
        solvencia: string;
        roa: string;
        roe: string;
    };
}

const IndicadoresFinancieros: React.FC = () => {
    const navigate = useNavigate();
    const { clienteId } = useParams<{ clienteId: string }>();
    const [indicadores, setIndicadores] = useState<IndicadoresData | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        cargarIndicadores();
    }, [clienteId]);

    const cargarIndicadores = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/api/v1/indicadores/${clienteId || '1234567'}`);
            setIndicadores(response.data);
            setLoading(false);
        } catch (err) {
            console.error(err);
            setError('Error al cargar indicadores. Verifique que el backend esté corriendo.');
            setLoading(false);
        }
    };

    const getColorByValue = (indicador: string, valor: number): string => {
        switch (indicador) {
            case 'liquidezCorriente': return valor >= 1.5 ? '#2E7D32' : valor >= 1.0 ? '#FF8F00' : '#C62828';
            case 'endeudamiento': return valor <= 0.5 ? '#2E7D32' : valor <= 0.8 ? '#FF8F00' : '#C62828';
            case 'solvencia': return valor >= 2.0 ? '#2E7D32' : valor >= 1.5 ? '#FF8F00' : '#C62828';
            case 'roa': return valor >= 10 ? '#2E7D32' : valor >= 5 ? '#FF8F00' : '#C62828';
            case 'roe': return valor >= 15 ? '#2E7D32' : valor >= 10 ? '#FF8F00' : '#C62828';
            default: return '#666666';
        }
    };

    const getFormatValue = (indicador: string, valor: number): string => {
        if (indicador === 'liquidezCorriente' || indicador === 'solvencia') return valor.toFixed(2) + 'x';
        return valor.toFixed(2) + '%';
    };

    if (loading) return <Container sx={{ mt: 4, textAlign: 'center' }}><CircularProgress /><Typography sx={{ mt: 2 }}>Calculando indicadores...</Typography></Container>;
    if (error) return <Container sx={{ mt: 4 }}><Typography color="error">{error}</Typography><Button onClick={cargarIndicadores} variant="contained" sx={{ mt: 2 }}>Reintentar</Button></Container>;
    if (!indicadores) return null;

    const indicadoresList = [
        { key: 'liquidezCorriente', label: 'Liquidez Corriente', descripcion: 'Activo Corriente / Pasivo Corriente' },
        { key: 'endeudamiento', label: 'Endeudamiento', descripcion: 'Pasivo Total / Activo Total' },
        { key: 'solvencia', label: 'Solvencia', descripcion: 'Activo Total / Pasivo Total' },
        { key: 'roa', label: 'ROA', descripcion: 'Rentabilidad sobre Activos' },
        { key: 'roe', label: 'ROE', descripcion: 'Rentabilidad sobre Patrimonio' }
    ];

    return (
        <Box sx={{ bgcolor: '#f5f5f5', minHeight: '100vh', pb: 4 }}>
            <Container maxWidth="lg">
                <Paper elevation={3} sx={{ p: 4, mt: 4 }}>
                    <Box sx={{ display: 'flex', alignItems: 'center', mb: 3 }}>
                        <IconButton onClick={() => navigate(`/volteo-balances/${clienteId}`)} sx={{ mr: 2 }}><ArrowBackIcon /></IconButton>
                        <Typography variant="h5" sx={{ color: '#003366', fontWeight: 'bold' }}>Indicadores Financieros (RF-03.2)</Typography>
                    </Box>
                    <Typography variant="body2" color="text.secondary" sx={{ mb: 3 }}>
                        Cliente ID: <strong>{clienteId || '1234567'}</strong> | Calculados desde el Volteo de Balances
                    </Typography>
                    <Grid container spacing={4}>
                        {indicadoresList.map((item) => {
                            const valor = indicadores[item.key as keyof IndicadoresData] as number;
                            const interpretacion = indicadores.interpretacion[item.key as keyof typeof indicadores.interpretacion];
                            const color = getColorByValue(item.key, valor);
                            return (
                                <Grid item xs={12} sm={6} md={4} key={item.key}>
                                    <Card sx={{ borderTop: `5px solid ${color}`, transition: '0.3s', '&:hover': { transform: 'scale(1.02)', boxShadow: 6 } }}>
                                        <CardContent>
                                            <Typography variant="subtitle2" color="text.secondary" gutterBottom>{item.label}</Typography>
                                            <Typography variant="h4" sx={{ fontWeight: 'bold', color: color }}>{getFormatValue(item.key, valor)}</Typography>
                                            <Typography variant="caption" color="text.secondary" display="block">{item.descripcion}</Typography>
                                            <Divider sx={{ my: 1 }} />
                                            <Chip size="small" label={interpretacion} sx={{ bgcolor: color, color: 'white', fontSize: '0.75rem' }} />
                                        </CardContent>
                                    </Card>
                                </Grid>
                            );
                        })}
                    </Grid>
                    <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 4 }}>
                        <Button variant="outlined" onClick={() => navigate(`/volteo-balances/${clienteId}`)}>← Volver a Balances</Button>
                        <Button variant="contained" onClick={() => navigate(`/simulador-pagos/${clienteId}`)} sx={{ bgcolor: '#003366' }}>Continuar a Simulador de Pagos →</Button>
                    </Box>
                </Paper>
            </Container>
        </Box>
    );
};

export default IndicadoresFinancieros;
