import React, { useMemo, useState } from 'react';
import { useParams } from 'react-router-dom';
import {
  Alert,
  Box,
  Button,
  CircularProgress,
  Container,
  Grid,
  TextField,
  Typography,
} from '@mui/material';

export const EvaluacionProduccion = () => {
  const { clienteId } = useParams<{ clienteId?: string }>();
  const [ingresos, setIngresos] = useState<number>(0);
  const [costosVariables, setCostosVariables] = useState<number>(0);
  const [costosFijos, setCostosFijos] = useState<number>(0);
  const [loading, setLoading] = useState<boolean>(false);
  const [message, setMessage] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);

  const resolvedClienteId = clienteId || 'CLIENTE-ANONIMO';
  const utilidadNeta = useMemo(() => ingresos - costosVariables - costosFijos, [ingresos, costosVariables, costosFijos]);
  const margenEfectivo = useMemo(
    () => (ingresos > 0 ? Number(((utilidadNeta / ingresos) * 100).toFixed(2)) : 0),
    [ingresos, utilidadNeta]
  );

  const handleSave = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setLoading(true);
    setError(null);
    setMessage(null);

    const payload = {
      clienteId: resolvedClienteId,
      ingresos,
      costosVariables,
      costosFijos,
    };

    try {
      const response = await fetch(`http://localhost:8080/api/evaluacion-produccion/${resolvedClienteId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload),
      });

      if (!response.ok) {
        const errorBody = await response.text();
        throw new Error(errorBody || `Error en el servidor: ${response.status}`);
      }

      const evaluacionId = await response.text();
      setMessage(`Evaluación registrada correctamente. ID: ${evaluacionId}`);
    } catch (fetchError: unknown) {
      setError(
        fetchError instanceof Error
          ? fetchError.message
          : 'Hubo un error al comunicarse con el servidor.'
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container maxWidth="md" sx={{ py: 4 }}>
      <Typography variant="h4" component="h1" gutterBottom>
        Evaluación de Producción Industrial
      </Typography>
      <Typography variant="subtitle1" color="text.secondary" gutterBottom>
        Cliente: {resolvedClienteId}
      </Typography>

      {error && (
        <Alert severity="error" sx={{ mb: 3 }}>
          {error}
        </Alert>
      )}
      {message && (
        <Alert severity="success" sx={{ mb: 3 }}>
          {message}
        </Alert>
      )}

      <Box component="form" onSubmit={handleSave} noValidate>
        <Grid container spacing={3}>
          <Grid item xs={12}>
            <Typography variant="h6">Ingresos proyectados</Typography>
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
              fullWidth
              label="Ingresos (Bs)"
              type="number"
              value={ingresos}
              onChange={(event) => setIngresos(Number(event.target.value))}
              required
              disabled={loading}
            />
          </Grid>

          <Grid item xs={12}>
            <Typography variant="h6">Costos variables</Typography>
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
              fullWidth
              label="Costos variables (Bs)"
              type="number"
              value={costosVariables}
              onChange={(event) => setCostosVariables(Number(event.target.value))}
              required
              disabled={loading}
            />
          </Grid>

          <Grid item xs={12}>
            <Typography variant="h6">Costos fijos</Typography>
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
              fullWidth
              label="Costos fijos (Bs)"
              type="number"
              value={costosFijos}
              onChange={(event) => setCostosFijos(Number(event.target.value))}
              required
              disabled={loading}
            />
          </Grid>

          <Grid item xs={12}>
            <Box sx={{ p: 3, bgcolor: 'background.paper', borderRadius: 2, boxShadow: 1 }}>
              <Typography variant="subtitle1" gutterBottom>
                Resultados automáticos
              </Typography>
              <Typography>Utilidad neta: Bs {utilidadNeta.toFixed(2)}</Typography>
              <Typography>Margen estimado: {margenEfectivo.toFixed(2)}%</Typography>
            </Box>
          </Grid>

          <Grid item xs={12} sx={{ display: 'flex', justifyContent: 'flex-end' }}>
            <Button type="submit" variant="contained" color="primary" disabled={loading}>
              {loading ? <CircularProgress size={24} color="inherit" /> : 'Guardar evaluación'}
            </Button>
          </Grid>
        </Grid>
      </Box>
    </Container>
  );
};