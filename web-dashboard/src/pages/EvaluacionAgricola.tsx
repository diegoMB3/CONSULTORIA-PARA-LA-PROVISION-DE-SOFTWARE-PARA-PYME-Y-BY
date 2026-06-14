import React, { useState } from 'react';
import {
  Container,
  Typography,
  Box,
  Paper,
  Grid,
  TextField,
  Button,
  Chip,
  CircularProgress,
  Alert,
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import AgricultureIcon from '@mui/icons-material/Agriculture';
import SaveIcon from '@mui/icons-material/Save';
import { useNavigate, useParams } from 'react-router-dom';
import axios from 'axios';

interface EvaluacionData {
  clienteId: string;
  tipoCultivo: string;
  superficieHectareas: number;
  costosInsumos: number;
  costosManoObra: number;
  ingresosVenta: number;
  ciclosProductivos: number;
  costoTotal: number;
  ingresoTotal: number;
  rentabilidad: number;
  mensaje: string;
}

const EvaluacionAgricola: React.FC = () => {
  const navigate = useNavigate();
  const { clienteId } = useParams();
  const [formData, setFormData] = useState({
    tipoCultivo: '',
    superficieHectareas: 0,
    costosInsumos: 0,
    costosManoObra: 0,
    ingresosVenta: 0,
    ciclosProductivos: 1,
  });
  const [resultado, setResultado] = useState<EvaluacionData | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target;
    setFormData({
      ...formData,
      [name]: name === 'tipoCultivo' ? value : Number(value),
    });
  };

  const handleRegistrar = async () => {
    setLoading(true);
    setError('');
    setResultado(null);

    try {
      const response = await axios.post('http://localhost:8080/api/v1/evaluacion-agricola/registrar', {
        clienteId: clienteId || 'SIN_CLIENTE',
        ...formData,
      });
      setResultado(response.data);
    } catch (err: any) {
      setError(err.response?.data?.detalle || 'Error al registrar evaluación agrícola');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const cultivosSugeridos = [
    'Soya',
    'Maíz',
    'Trigo',
    'Quinua',
    'Girasol',
    'Arroz',
    'Caña de azúcar',
    'Papa',
    'Cebada',
    'Otro',
  ];

  return (
    <Container maxWidth="md" sx={{ mt: 4, mb: 4 }}>
      <Button
        startIcon={<ArrowBackIcon />}
        onClick={() => navigate('/')} 
        sx={{ mb: 2 }}
      >
        Volver al menú
      </Button>

      <Paper sx={{ p: 4 }} elevation={3}>
        <Box display="flex" alignItems="center" gap={2} mb={3}>
          <AgricultureIcon color="primary" fontSize="large" />
          <Typography variant="h4">Evaluación Agrícola</Typography>
        </Box>

        <Typography sx={{ mb: 2 }}>
          Registre los datos del cultivo y el número de ciclos para calcular rentabilidad.
        </Typography>

        <Grid container spacing={2}>
          <Grid item xs={12} md={6}>
            <TextField
              fullWidth
              label="Tipo de cultivo"
              name="tipoCultivo"
              value={formData.tipoCultivo}
              onChange={handleChange}
            />
          </Grid>
          <Grid item xs={12} md={6}>
            <TextField
              fullWidth
              label="Superficie (hectáreas)"
              name="superficieHectareas"
              type="number"
              value={formData.superficieHectareas}
              onChange={handleChange}
            />
          </Grid>
          <Grid item xs={12} md={6}>
            <TextField
              fullWidth
              label="Costos de insumos (Bs)"
              name="costosInsumos"
              type="number"
              value={formData.costosInsumos}
              onChange={handleChange}
            />
          </Grid>
          <Grid item xs={12} md={6}>
            <TextField
              fullWidth
              label="Costos de mano de obra (Bs)"
              name="costosManoObra"
              type="number"
              value={formData.costosManoObra}
              onChange={handleChange}
            />
          </Grid>
          <Grid item xs={12} md={6}>
            <TextField
              fullWidth
              label="Ingresos por venta por ciclo (Bs)"
              name="ingresosVenta"
              type="number"
              value={formData.ingresosVenta}
              onChange={handleChange}
            />
          </Grid>
          <Grid item xs={12} md={6}>
            <TextField
              fullWidth
              label="Ciclos productivos"
              name="ciclosProductivos"
              type="number"
              value={formData.ciclosProductivos}
              onChange={handleChange}
            />
          </Grid>
        </Grid>

        <Box sx={{ mt: 3, display: 'flex', gap: 2, flexWrap: 'wrap' }}>
          {cultivosSugeridos.map((cultivo) => (
            <Chip
              key={cultivo}
              label={cultivo}
              clickable
              onClick={() => setFormData({ ...formData, tipoCultivo: cultivo })}
              color={formData.tipoCultivo === cultivo ? 'primary' : 'default'}
            />
          ))}
        </Box>

        <Box sx={{ mt: 4, display: 'flex', gap: 2, flexWrap: 'wrap' }}>
          <Button
            variant="contained"
            startIcon={<SaveIcon />}
            onClick={handleRegistrar}
            disabled={loading}
          >
            {loading ? 'Calculando...' : 'Calcular Rentabilidad'}
          </Button>
        </Box>

        {error && (
          <Alert severity="error" sx={{ mt: 3 }}>
            {error}
          </Alert>
        )}

        {loading && !resultado && (
          <Box sx={{ mt: 3, display: 'flex', justifyContent: 'center' }}>
            <CircularProgress />
          </Box>
        )}

        {resultado && (
          <Paper sx={{ mt: 4, p: 3, bgcolor: '#f7fbff' }}>
            <Typography variant="h6" gutterBottom>
              Resultado de la evaluación
            </Typography>
            <Grid container spacing={2}>
              <Grid item xs={12} sm={6}>
                <Typography><strong>Tipo de cultivo:</strong> {resultado.tipoCultivo}</Typography>
                <Typography><strong>Superficie:</strong> {resultado.superficieHectareas} ha</Typography>
                <Typography><strong>Ciclos:</strong> {resultado.ciclosProductivos}</Typography>
              </Grid>
              <Grid item xs={12} sm={6}>
                <Typography><strong>Costo Total:</strong> Bs {resultado.costoTotal.toFixed(2)}</Typography>
                <Typography><strong>Ingreso Total:</strong> Bs {resultado.ingresoTotal.toFixed(2)}</Typography>
                <Typography><strong>Rentabilidad:</strong> Bs {resultado.rentabilidad.toFixed(2)}</Typography>
              </Grid>
            </Grid>

            <Box sx={{ mt: 3, p: 2, borderRadius: 2, backgroundColor: resultado.rentabilidad >= 0 ? '#e8f5e9' : '#ffebee' }}>
              <Typography variant="subtitle1" color={resultado.rentabilidad >= 0 ? 'success.main' : 'error.main'}>
                {resultado.mensaje}
              </Typography>
            </Box>
          </Paper>
        )}
      </Paper>
    </Container>
  );
};

export default EvaluacionAgricola;
