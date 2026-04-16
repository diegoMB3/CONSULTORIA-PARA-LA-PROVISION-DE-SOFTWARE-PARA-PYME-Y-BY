import React from 'react';
import { Box, Typography, Button, Paper } from '@mui/material';
import { useNavigate, useParams } from 'react-router-dom';

const EvaluacionCliente: React.FC = () => {
  const navigate = useNavigate();
  const { clienteId } = useParams<{ clienteId: string }>();

  return (
    <Box sx={{ minHeight: '100vh', bgcolor: '#f5f5f5', py: 6 }}>
      <Paper sx={{ maxWidth: 720, mx: 'auto', p: 4 }} elevation={3}>
        <Typography variant="h4" sx={{ mb: 2, color: '#003366' }}>
          Evaluación de Cliente
        </Typography>
        <Typography variant="body1" sx={{ mb: 3 }}>
          Cliente ID: {clienteId}
        </Typography>
        <Typography variant="body2" sx={{ mb: 3 }}>
          Esta pantalla es la etapa siguiente del proceso después de completar el checklist de documentos. Aquí se validará la información de ingreso, capacidad de pago y cumplimiento de requisitos normativos.
        </Typography>
        <Button variant="contained" color="primary" onClick={() => navigate('/')}>
          Volver al Menú Principal
        </Button>
      </Paper>
    </Box>
  );
};

export default EvaluacionCliente;
