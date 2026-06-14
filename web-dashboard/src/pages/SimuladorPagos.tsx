import React, { useState } from 'react';
import {
  Container,
  Typography,
  Box,
  Paper,
  Grid,
  TextField,
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  CircularProgress,
  Alert,
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import CalculateIcon from '@mui/icons-material/Calculate';
import PictureAsPdfIcon from '@mui/icons-material/PictureAsPdf';
import { useNavigate, useParams } from 'react-router-dom';
import axios from 'axios';

interface CuotaData {
  cuota: number;
  capital: number;
  interes: number;
  total: number;
  saldo: number;
}

interface PlanPagoData {
  clienteId: string;
  monto: number;
  tasaAnual: number;
  plazoMeses: number;
  cuotaMensual: number;
  cronograma: CuotaData[];
}

const SimuladorPagos: React.FC = () => {
  const navigate = useNavigate();
  const { clienteId } = useParams();
  const [formData, setFormData] = useState({ monto: 10000, tasaAnual: 12, plazoMeses: 12 });
  const [plan, setPlan] = useState<PlanPagoData | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target;
    setFormData({ ...formData, [name]: Number(value) });
  };

  const handleGenerar = async () => {
    setLoading(true);
    setError('');
    setPlan(null);

    try {
      const response = await axios.post('http://localhost:8080/api/v1/simulador-pagos/generar', {
        clienteId: clienteId || 'SIN_CLIENTE',
        monto: formData.monto,
        tasaAnual: formData.tasaAnual,
        plazoMeses: formData.plazoMeses,
      });

      setPlan(response.data);
    } catch (err: any) {
      setError(err?.response?.data?.detalle || 'Error al generar el plan de pagos');
    } finally {
      setLoading(false);
    }
  };

  const totalPagar = plan ? plan.cronograma.reduce((sum, fila) => sum + fila.total, 0) : 0;
  const interesTotal = plan ? totalPagar - plan.monto : 0;

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Button
        startIcon={<ArrowBackIcon />}
        onClick={() => navigate(`/indicadores/${clienteId || ''}`)}
        sx={{ mb: 2 }}
      >
        Volver a Indicadores
      </Button>

      <Paper sx={{ p: 4 }} elevation={3}>
        <Typography variant="h4" gutterBottom>
          Simulador de Pagos
        </Typography>
        <Typography variant="body1" sx={{ mb: 3 }}>
          Genere un cronograma de amortización para el cliente seleccionado.
        </Typography>

        <Grid container spacing={2}>
          <Grid item xs={12} md={4}>
            <TextField
              fullWidth
              label="Monto (Bs)"
              name="monto"
              type="number"
              value={formData.monto}
              onChange={handleChange}
            />
          </Grid>
          <Grid item xs={12} md={4}>
            <TextField
              fullWidth
              label="Tasa Anual (%)"
              name="tasaAnual"
              type="number"
              value={formData.tasaAnual}
              onChange={handleChange}
            />
          </Grid>
          <Grid item xs={12} md={4}>
            <TextField
              fullWidth
              label="Plazo (meses)"
              name="plazoMeses"
              type="number"
              value={formData.plazoMeses}
              onChange={handleChange}
            />
          </Grid>
        </Grid>

        <Box sx={{ mt: 4, display: 'flex', gap: 2, alignItems: 'center' }}>
          <Button
            variant="contained"
            color="primary"
            startIcon={<CalculateIcon />}
            onClick={handleGenerar}
            disabled={loading}
          >
            {loading ? 'Calculando...' : 'Generar Plan de Pagos'}
          </Button>
          <Button
            variant="outlined"
            color="secondary"
            startIcon={<PictureAsPdfIcon />}
            onClick={() => alert('Funcionalidad de exportación PDF pendiente')}
          >
            Exportar PDF
          </Button>
        </Box>

        {error && (
          <Alert severity="error" sx={{ mt: 3 }}>
            {error}
          </Alert>
        )}

        {loading && !plan && (
          <Box sx={{ display: 'flex', justifyContent: 'center', mt: 3 }}>
            <CircularProgress />
          </Box>
        )}

        {plan && (
          <Box sx={{ mt: 4 }}>
            <Paper sx={{ p: 3, mb: 3, backgroundColor: '#f5f5f5' }}>
              <Grid container spacing={2}>
                <Grid item xs={12} md={3}>
                  <Typography variant="subtitle2">Cuota Mensual</Typography>
                  <Typography variant="h6">Bs {plan.cuotaMensual.toFixed(2)}</Typography>
                </Grid>
                <Grid item xs={12} md={3}>
                  <Typography variant="subtitle2">Total a Pagar</Typography>
                  <Typography variant="h6">Bs {totalPagar.toFixed(2)}</Typography>
                </Grid>
                <Grid item xs={12} md={3}>
                  <Typography variant="subtitle2">Interés Total</Typography>
                  <Typography variant="h6">Bs {interesTotal.toFixed(2)}</Typography>
                </Grid>
                <Grid item xs={12} md={3}>
                  <Typography variant="subtitle2">Tasa Mensual</Typography>
                  <Typography variant="h6">{(plan.tasaAnual / 12).toFixed(2)}%</Typography>
                </Grid>
              </Grid>
            </Paper>

            <TableContainer component={Paper} elevation={2}>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell>N°</TableCell>
                    <TableCell>Capital (Bs)</TableCell>
                    <TableCell>Interés (Bs)</TableCell>
                    <TableCell>Total (Bs)</TableCell>
                    <TableCell>Saldo (Bs)</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {plan.cronograma.map((fila) => (
                    <TableRow key={fila.cuota}>
                      <TableCell>{fila.cuota}</TableCell>
                      <TableCell>{fila.capital.toFixed(2)}</TableCell>
                      <TableCell>{fila.interes.toFixed(2)}</TableCell>
                      <TableCell>{fila.total.toFixed(2)}</TableCell>
                      <TableCell>{fila.saldo.toFixed(2)}</TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          </Box>
        )}
      </Paper>
    </Container>
  );
};

export default SimuladorPagos;
