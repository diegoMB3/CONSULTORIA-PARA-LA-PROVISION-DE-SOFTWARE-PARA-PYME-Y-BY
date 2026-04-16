import React, { useState, useEffect } from 'react';
import {
    Container,
    Typography,
    Box,
    Paper,
    TextField,
    Button,
    Divider,
    Alert,
    Chip,
    IconButton,
    LinearProgress
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import SaveIcon from '@mui/icons-material/Save';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import ErrorIcon from '@mui/icons-material/Error';
import { useNavigate, useParams } from 'react-router-dom';
import axios from 'axios';

interface CuentasBalance {
    [key: string]: number;
}

const VolteoBalances: React.FC = () => {
    const navigate = useNavigate();
    const { clienteId } = useParams<{ clienteId?: string }>();
    const [cuentas, setCuentas] = useState<CuentasBalance>({});
    const [totales, setTotales] = useState({
        activoCorriente: 0,
        activoNoCorriente: 0,
        activoTotal: 0,
        pasivoCorriente: 0,
        pasivoNoCorriente: 0,
        pasivoTotal: 0,
        patrimonio: 0,
        pasivoPatrimonioTotal: 0,
        diferencia: 0
    });
    const [balanceCuadrado, setBalanceCuadrado] = useState(false);
    const [loading, setLoading] = useState(true);
    const [guardando, setGuardando] = useState(false);
    const [mensaje, setMensaje] = useState('');

    const cuentasActivoCorriente = [
        { id: 'CAJA_BANCOS', label: 'Caja y Bancos' },
        { id: 'CUENTAS_COBRAR', label: 'Cuentas por Cobrar' },
        { id: 'INVENTARIOS', label: 'Inventarios' }
    ];

    const cuentasActivoNoCorriente = [
        { id: 'TERRENOS', label: 'Terrenos' },
        { id: 'EDIFICIOS', label: 'Edificios' },
        { id: 'MAQUINARIA', label: 'Maquinaria' },
        { id: 'VEHICULOS', label: 'Vehículos' }
    ];

    const cuentasPasivoCorriente = [
        { id: 'PROVEEDORES', label: 'Proveedores' },
        { id: 'IMPUESTOS_POR_PAGAR', label: 'Impuestos por Pagar' },
        { id: 'SUELDOS_POR_PAGAR', label: 'Sueldos por Pagar' }
    ];

    const cuentasPasivoNoCorriente = [
        { id: 'PRESTAMOS_BANCARIOS_LP', label: 'Préstamos Bancarios Largo Plazo' }
    ];

    const cuentasPatrimonio = [
        { id: 'CAPITAL_SOCIAL', label: 'Capital Social' },
        { id: 'RESERVAS', label: 'Reservas' },
        { id: 'RESULTADOS_ACUMULADOS', label: 'Resultados Acumulados' },
        { id: 'RESULTADO_EJERCICIO', label: 'Resultado del Ejercicio' }
    ];

    useEffect(() => {
        cargarBalance();
    }, [clienteId]);

    useEffect(() => {
        calcularTotales();
    }, [cuentas]);

    const cargarBalance = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/api/v1/balances/${clienteId || '1234567'}`);
            setCuentas(response.data.cuentas);
            setTotales({
                activoCorriente: response.data.activoCorriente,
                activoNoCorriente: response.data.activoNoCorriente,
                activoTotal: response.data.activoTotal,
                pasivoCorriente: response.data.pasivoCorriente,
                pasivoNoCorriente: response.data.pasivoNoCorriente,
                pasivoTotal: response.data.pasivoTotal,
                patrimonio: response.data.patrimonio,
                pasivoPatrimonioTotal: response.data.pasivoPatrimonioTotal,
                diferencia: response.data.diferencia
            });
            setBalanceCuadrado(response.data.balanceCuadrado);
            setMensaje(response.data.mensaje);
        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const calcularTotales = () => {
        const actCte = (cuentas.CAJA_BANCOS || 0) + (cuentas.CUENTAS_COBRAR || 0) + (cuentas.INVENTARIOS || 0);
        const actNoCte = (cuentas.TERRENOS || 0) + (cuentas.EDIFICIOS || 0) + (cuentas.MAQUINARIA || 0) + (cuentas.VEHICULOS || 0);
        const pasCte = (cuentas.PROVEEDORES || 0) + (cuentas.IMPUESTOS_POR_PAGAR || 0) + (cuentas.SUELDOS_POR_PAGAR || 0);
        const pasNoCte = (cuentas.PRESTAMOS_BANCARIOS_LP || 0);
        const pat = (cuentas.CAPITAL_SOCIAL || 0) + (cuentas.RESERVAS || 0) +
                    (cuentas.RESULTADOS_ACUMULADOS || 0) + (cuentas.RESULTADO_EJERCICIO || 0);

        const activoTotal = actCte + actNoCte;
        const pasivoTotal = pasCte + pasNoCte;
        const pasivoPatTotal = pasivoTotal + pat;
        const diferencia = Math.abs(activoTotal - pasivoPatTotal);
        const cuadrado = diferencia <= 10.0;

        setTotales({
            activoCorriente: actCte,
            activoNoCorriente: actNoCte,
            activoTotal,
            pasivoCorriente: pasCte,
            pasivoNoCorriente: pasNoCte,
            pasivoTotal,
            patrimonio: pat,
            pasivoPatrimonioTotal: pasivoPatTotal,
            diferencia
        });
        setBalanceCuadrado(cuadrado);
        setMensaje(cuadrado ? '✅ Balance Cuadrado' : `❌ Descuadre: ${diferencia.toFixed(2)} Bs (Máximo ±10 Bs)`);
    };

    const handleChange = (cuentaId: string, valor: string) => {
        const numValor = parseFloat(valor) || 0;
        setCuentas(prev => ({ ...prev, [cuentaId]: numValor }));
    };

    const handleGuardar = async () => {
        setGuardando(true);
        try {
            const response = await axios.post(`http://localhost:8080/api/v1/balances/${clienteId || '1234567'}`, cuentas);
            alert(`✅ ${response.data.mensaje}`);
        } catch (err) {
            console.error(err);
            alert('Error al guardar balance');
        } finally {
            setGuardando(false);
        }
    };

    const handleContinuar = async () => {
        if (!balanceCuadrado) {
            alert('❌ No puede continuar. El balance está descuadrado por más de ±10 Bs.');
            return;
        }

        try {
            const response = await axios.post(`http://localhost:8080/api/v1/balances/${clienteId || '1234567'}/validar`, cuentas);
            alert(response.data.mensaje);
            navigate(`/`);
        } catch (err: any) {
            alert(err.response?.data?.detalle || 'Error de validación');
        }
    };

    const renderFila = (cuenta: { id: string; label: string }) => (
        <Box key={cuenta.id} sx={{ display: 'flex', gap: 2, mb: 1, alignItems: 'center' }}>
            <Box sx={{ flex: 1 }}>
                <Typography>{cuenta.label}</Typography>
            </Box>
            <Box sx={{ width: '50%' }}>
                <TextField
                    type="number"
                    value={cuentas[cuenta.id] || 0}
                    onChange={(e) => handleChange(cuenta.id, e.target.value)}
                    size="small"
                    fullWidth
                />
            </Box>
        </Box>
    );

    if (loading) {
        return (
            <Container sx={{ mt: 4 }}>
                <LinearProgress />
                <Typography sx={{ mt: 2 }}>Cargando balance...</Typography>
            </Container>
        );
    }

    return (
        <Box sx={{ bgcolor: '#f5f5f5', minHeight: '100vh', pb: 4 }}>
            <Container maxWidth="lg">
                <Paper elevation={3} sx={{ p: 4, mt: 4 }}>
                    <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                        <IconButton onClick={() => navigate('/')} sx={{ mr: 2 }}>
                            <ArrowBackIcon />
                        </IconButton>
                        <Typography variant="h5" sx={{ color: '#003366', fontWeight: 'bold' }}>
                            Volteo de Balances (RF-03.1)
                        </Typography>
                    </Box>

                    <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                        <Typography variant="body2" color="text.secondary" sx={{ mr: 2 }}>
                            Cliente ID: <strong>{clienteId || '1234567'}</strong>
                        </Typography>
                        {balanceCuadrado ? (
                            <Chip icon={<CheckCircleIcon />} label="Balance Cuadrado" color="success" />
                        ) : (
                            <Chip icon={<ErrorIcon />} label={mensaje} color="error" />
                        )}
                    </Box>

                    <Alert severity="info" sx={{ mb: 3 }}>
                        <strong>RNF-02:</strong> La diferencia entre Activo Total y (Pasivo + Patrimonio) no debe superar ±10 Bs.
                        {totales.diferencia > 10 && (
                            <Typography color="error" sx={{ mt: 1 }}>
                                ⚠️ Descuadre actual: {totales.diferencia.toFixed(2)} Bs
                            </Typography>
                        )}
                    </Alert>

                    <Box sx={{ display: 'flex', flexDirection: { xs: 'column', md: 'row' }, gap: 4 }}>
                        <Box sx={{ flex: 1 }}>
                            <Typography variant="h6" sx={{ color: '#003366', mb: 2 }}>ACTIVO</Typography>
                            <Paper sx={{ p: 2, mb: 2, bgcolor: '#e3f2fd' }}>
                                <Typography variant="subtitle1" sx={{ fontWeight: 'bold' }}>Activo Corriente</Typography>
                                {cuentasActivoCorriente.map(renderFila)}
                                <Divider sx={{ my: 1 }} />
                                <Typography sx={{ fontWeight: 'bold' }}>
                                    Subtotal Activo Corriente: Bs {totales.activoCorriente.toFixed(2)}
                                </Typography>
                            </Paper>

                            <Paper sx={{ p: 2, bgcolor: '#e3f2fd' }}>
                                <Typography variant="subtitle1" sx={{ fontWeight: 'bold' }}>Activo No Corriente</Typography>
                                {cuentasActivoNoCorriente.map(renderFila)}
                                <Divider sx={{ my: 1 }} />
                                <Typography sx={{ fontWeight: 'bold' }}>
                                    Subtotal Activo No Corriente: Bs {totales.activoNoCorriente.toFixed(2)}
                                </Typography>
                            </Paper>

                            <Paper sx={{ p: 2, mt: 2, bgcolor: '#003366', color: 'white' }}>
                                <Typography variant="h6">
                                    TOTAL ACTIVO: Bs {totales.activoTotal.toFixed(2)}
                                </Typography>
                            </Paper>
                        </Box>

                        <Box sx={{ flex: 1 }}>
                            <Typography variant="h6" sx={{ color: '#003366', mb: 2 }}>PASIVO</Typography>
                            <Paper sx={{ p: 2, mb: 2, bgcolor: '#ffebee' }}>
                                <Typography variant="subtitle1" sx={{ fontWeight: 'bold' }}>Pasivo Corriente</Typography>
                                {cuentasPasivoCorriente.map(renderFila)}
                                <Divider sx={{ my: 1 }} />
                                <Typography sx={{ fontWeight: 'bold' }}>
                                    Subtotal Pasivo Corriente: Bs {totales.pasivoCorriente.toFixed(2)}
                                </Typography>
                            </Paper>

                            <Paper sx={{ p: 2, mb: 2, bgcolor: '#ffebee' }}>
                                <Typography variant="subtitle1" sx={{ fontWeight: 'bold' }}>Pasivo No Corriente</Typography>
                                {cuentasPasivoNoCorriente.map(renderFila)}
                                <Divider sx={{ my: 1 }} />
                                <Typography sx={{ fontWeight: 'bold' }}>
                                    Subtotal Pasivo No Corriente: Bs {totales.pasivoNoCorriente.toFixed(2)}
                                </Typography>
                            </Paper>

                            <Typography variant="h6" sx={{ color: '#003366', mt: 3, mb: 2 }}>PATRIMONIO</Typography>
                            <Paper sx={{ p: 2, bgcolor: '#e8f5e9' }}>
                                {cuentasPatrimonio.map(renderFila)}
                                <Divider sx={{ my: 1 }} />
                                <Typography sx={{ fontWeight: 'bold' }}>
                                    Total Patrimonio: Bs {totales.patrimonio.toFixed(2)}
                                </Typography>
                            </Paper>

                            <Paper sx={{ p: 2, mt: 2, bgcolor: '#003366', color: 'white' }}>
                                <Typography variant="h6">
                                    TOTAL PASIVO + PATRIMONIO: Bs {totales.pasivoPatrimonioTotal.toFixed(2)}
                                </Typography>
                            </Paper>
                        </Box>
                    </Box>

                    <Divider sx={{ my: 3 }} />

                    <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                        <Button variant="outlined" onClick={() => navigate('/')}>Cancelar</Button>
                        <Box>
                            <Button
                                variant="contained"
                                onClick={handleGuardar}
                                disabled={guardando}
                                startIcon={<SaveIcon />}
                                sx={{ mr: 2, bgcolor: '#607d8b' }}
                            >
                                {guardando ? 'Guardando...' : 'Guardar Progreso'}
                            </Button>
                            <Button
                                variant="contained"
                                onClick={handleContinuar}
                                disabled={!balanceCuadrado}
                                sx={{
                                    bgcolor: balanceCuadrado ? '#003366' : '#bdbdbd',
                                    '&:hover': { bgcolor: balanceCuadrado ? '#002244' : '#bdbdbd' }
                                }}
                            >
                                Continuar a Indicadores Financieros
                            </Button>
                        </Box>
                    </Box>
                </Paper>
            </Container>
        </Box>
    );
};

export default VolteoBalances;
