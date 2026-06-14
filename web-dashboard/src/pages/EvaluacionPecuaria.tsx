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
  FormControl,
  InputLabel,
  Select,
  MenuItem,
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import PetsIcon from '@mui/icons-material/Pets';
import SaveIcon from '@mui/icons-material/Save';
import { useNavigate, useParams } from 'react-router-dom';
import axios from 'axios';

interface EvaluacionPecuariaData {
  clienteId: string;
  tipoGanado: string;
  cantidadInicial: number;
  tasaNatalidad: number;
  tasaMortalidad: number;
  tasaNeta: number;
  animalesProyectados: number;
  ingresoProyectado: number;
  costoProyectado: number;
  rentabilidadProyectada: number;
  escenario: string;
  mensaje: string;
}

const EvaluacionPecuaria: React.FC = () => {
  const navigate = useNavigate();
  const { clienteId } = useParams();
  const [formData, setFormData] = useState({
    tipoGanado: '',
    cantidadInicial: 0,
    tasaNatalidad: 0,
    tasaMortalidad: 0,
    precioVentaUnitario: 0,
    costoMantenimientoUnitario: 0,
    escenario: 'CRIA',
  });
  const [resultado, setResultado] = useState<EvaluacionPecuariaData | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const tiposGanado = ['Bovino', 'Ovino', 'Porcino', 'Camélido', 'Caprino', 'Aves de corral'];

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target;
    setFormData({
      ...formData,
      [name]: name === 'tipoGanado' ? value : Number(value),
    });
  };

  const handleEscenarioChange = (event: React.ChangeEvent<{ value: unknown }>) => {
    setFormData({ ...formData, escenario: event.target.value as string });
  };

  const handleRegistrar = async () => {
    setLoading(true);
    setError('');
    setResultado(null);

    try {
      const response = await axios.post('http://localhost:8080/api/v1/evaluacion-pecuaria/registrar', {
        clienteId: clienteId || 'SIN_CLIENTE',
        ...formData,
      });
      setResultado(response.data);
    } catch (err: any) {
      setError(err?.response?.data?.detalle || 'Error al registrar evaluación pecuaria');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

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
        <Box display="flex" alignItems="center" mb={3} gap={2}>
          <PetsIcon color="primary" fontSize="large" />
          <Typography variant="h4">Evaluación Pecuaria</Typography>
        </Box>

        <Typography sx={{ mb: 3 }}>
          Proyecte el hato ganadero y calcule la rentabilidad esperada por escenario.
        </Typography>

        <Grid container spacing={2}>
          <Grid item xs={12} md={6}>
            <TextField
              fullWidth
              label="Tipo de ganado"
              name="tipoGanado"
              value={formData.tipoGanado}
              onChange={handleChange}
            />
          </Grid>
          <Grid item xs={12} md={6}>
            <TextField
              fullWidth
              label="Cantidad inicial"
              name="cantidadInicial"
              type="number"
              value={formData.cantidadInicial}
              onChange={handleChange}
            />
          </Grid>
          <Grid item xs={12} md={6}>
            <TextField
              fullWidth
              label="Tasa de natalidad (%)"
              name="tasaNatalidad"
              type="number"
              value={formData.tasaNatalidad}
              onChange={handleChange}
            />
          </Grid>
          <Grid item xs={12} md={6}>
            <TextField
              fullWidth
              label="Tasa de mortalidad (%)"
              name="tasaMortalidad"
              type="number"
              value={formData.tasaMortalidad}
              onChange={handleChange}
            />
          </Grid>
          <Grid item xs={12} md={6}>
            <TextField
              fullWidth
              label="Precio de venta unitario (Bs)"
              name="precioVentaUnitario"
              type="number"
              value={formData.precioVentaUnitario}
              onChange={handleChange}
            />
          </Grid>
          <Grid item xs={12} md={6}>
            <TextField
              fullWidth
              label="Costo de mantenimiento unitario (Bs)"
              name="costoMantenimientoUnitario"
              type="number"
              value={formData.costoMantenimientoUnitario}
              onChange={handleChange}
            />
          </Grid>
          <Grid item xs={12} md={6}>
            <FormControl fullWidth>
              <InputLabel id="escenario-label">Escenario</InputLabel>
              <Select
                labelId="escenario-label"
                label="Escenario"
                value={formData.escenario}
                onChange={handleEscenarioChange}
              >
                <MenuItem value="CRIA">Cría</MenuItem>
                <MenuItem value="RECRIA">Recría</MenuItem>
                <MenuItem value="ENGORDE">Engorde</MenuItem>
              </Select>
            </FormControl>
          </Grid>
        </Grid>

        <Box sx={{ mt: 3, display: 'flex', gap: 2, flexWrap: 'wrap' }}>
          {tiposGanado.map((tipo) => (
            <Chip
              key={tipo}
              label={tipo}
              clickable
              onClick={() => setFormData({ ...formData, tipoGanado: tipo })}
              color={formData.tipoGanado === tipo ? 'primary' : 'default'}
            />
          ))}
        </Box>

        <Box sx={{ mt: 4 }}>
          <Button
            variant="contained"
            startIcon={<SaveIcon />}
            onClick={handleRegistrar}
            disabled={loading}
          >
            {loading ? 'Calculando...' : 'Calcular Proyección'}
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
          <Paper sx={{ mt: 4, p: 3, bgcolor: '#f9fbe7' }}>
            <Typography variant="h6" gutterBottom>
              Resultados de la proyección
            </Typography>
            <Grid container spacing={2}>
              <Grid item xs={12} sm={6}>
                <Typography><strong>Ganado:</strong> {resultado.tipoGanado}</Typography>
                <Typography><strong>Escenario:</strong> {resultado.escenario}</Typography>
                <Typography><strong>Cantidad inicial:</strong> {resultado.cantidadInicial}</Typography>
                <Typography><strong>Animales proyectados:</strong> {resultado.animalesProyectados}</Typography>
              </Grid>
              <Grid item xs={12} sm={6}>
                <Typography><strong>Ingreso proyectado:</strong> Bs {resultado.ingresoProyectado.toFixed(2)}</Typography>
                <Typography><strong>Costo proyectado:</strong> Bs {resultado.costoProyectado.toFixed(2)}</Typography>
                <Typography><strong>Rentabilidad:</strong> Bs {resultado.rentabilidadProyectada.toFixed(2)}</Typography>
                <Typography><strong>Tasa neta:</strong> {(resultado.tasaNeta * 100).toFixed(2)}%</Typography>
              </Grid>
            </Grid>

            <Box sx={{ mt: 3, p: 2, borderRadius: 2, backgroundColor: resultado.rentabilidadProyectada >= 0 ? '#e8f5e9' : '#ffebee' }}>
              <Typography color={resultado.rentabilidadProyectada >= 0 ? 'success.main' : 'error.main'}>
                {resultado.mensaje}
              </Typography>
            </Box>
          </Paper>
        )}
      </Paper>
    </Container>
  );
};

export default EvaluacionPecuaria;
