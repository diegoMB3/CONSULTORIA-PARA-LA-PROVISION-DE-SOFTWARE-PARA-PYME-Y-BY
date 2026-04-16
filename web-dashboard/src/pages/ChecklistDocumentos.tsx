import React, { useState, useEffect, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Box,
  Typography,
  Card,
  CardContent,
  FormControlLabel,
  Checkbox,
  Button,
  Alert,
  LinearProgress,
  Chip,
  Paper
} from '@mui/material';
import axios from 'axios';

interface Documento {
  nombre: string;
  descripcion: string;
  obligatorio: boolean;
}

interface ChecklistData {
  clienteId: string;
  documentos: Record<string, boolean>;
  completo: boolean;
  puedeAvanzar: boolean;
}

const ChecklistDocumentos: React.FC = () => {
  const { clienteId } = useParams<{ clienteId: string }>();
  const navigate = useNavigate();
  const [checklist, setChecklist] = useState<Record<string, boolean>>({});
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);

  const documentos: Documento[] = [
    { nombre: 'CI_FRENTE', descripcion: 'Cédula de Identidad - Frente', obligatorio: true },
    { nombre: 'CI_REVERSO', descripcion: 'Cédula de Identidad - Reverso', obligatorio: true },
    { nombre: 'FACTURA_AGUA', descripcion: 'Factura de Agua (último mes)', obligatorio: true },
    { nombre: 'FACTURA_LUZ', descripcion: 'Factura de Luz (último mes)', obligatorio: true },
    { nombre: 'ESTADO_CUENTA', descripcion: 'Estado de Cuenta Bancario', obligatorio: true },
    { nombre: 'AVAL_BANCARIO', descripcion: 'Aval Bancario', obligatorio: false },
    { nombre: 'DECLARACION_JURADA', descripcion: 'Declaración Jurada de Ingresos', obligatorio: false }
  ];

  const cargarChecklist = useCallback(async () => {
    if (!clienteId) return;
    try {
      setLoading(true);
      const response = await axios.get<ChecklistData>(`http://localhost:8080/api/v1/documentos/checklist/${clienteId}`);
      setChecklist(response.data.documentos);
      setError(null);
    } catch (err) {
      setError('Error al cargar el checklist de documentos');
      console.error('Error:', err);
    } finally {
      setLoading(false);
    }
  }, [clienteId]);

  useEffect(() => {
    cargarChecklist();
  }, [cargarChecklist]);

  const handleCheckboxChange = (documento: string) => (event: React.ChangeEvent<HTMLInputElement>) => {
    setChecklist(prev => ({
      ...prev,
      [documento]: event.target.checked
    }));
  };

  const guardarChecklist = async () => {
    if (!clienteId) return;

    try {
      setSaving(true);
      setError(null);
      setSuccess(null);

      const response = await axios.post(`http://localhost:8080/api/v1/documentos/checklist/${clienteId}`, checklist);
      
      setSuccess(response.data.mensaje);
      
      // Recargar para obtener el estado actualizado
      await cargarChecklist();
      
    } catch (err: unknown) {
      const errorMessage = axios.isAxiosError(err)
        ? err.response?.data?.detalle || 'Error al guardar el checklist'
        : 'Error al guardar el checklist';
      setError(errorMessage);
      console.error('Error:', err);
    } finally {
      setSaving(false);
    }
  };

  const calcularProgreso = () => {
    const total = documentos.length;
    const completados = Object.values(checklist).filter(Boolean).length;
    return (completados / total) * 100;
  };

  const documentosObligatoriosCompletos = () => {
    return documentos
      .filter(doc => doc.obligatorio)
      .every(doc => checklist[doc.nombre]);
  };

  const puedeAvanzar = documentosObligatoriosCompletos();

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '400px' }}>
        <Typography>Cargando checklist...</Typography>
      </Box>
    );
  }

  return (
    <Box sx={{ maxWidth: 800, mx: 'auto', p: 3 }}>
      <Typography variant="h4" gutterBottom align="center">
        Checklist de Documentos
      </Typography>
      
      <Typography variant="subtitle1" gutterBottom align="center" color="text.secondary">
        Cliente ID: {clienteId}
      </Typography>

      <Paper sx={{ p: 3, mb: 3 }}>
        <Box sx={{ mb: 2 }}>
          <Typography variant="h6" gutterBottom>
            Progreso de Documentación
          </Typography>
          <LinearProgress 
            variant="determinate" 
            value={calcularProgreso()} 
            sx={{ height: 10, borderRadius: 5, mb: 1 }}
          />
          <Typography variant="body2" color="text.secondary">
            {Object.values(checklist).filter(Boolean).length} de {documentos.length} documentos completados
          </Typography>
        </Box>

        {!puedeAvanzar && (
          <Alert severity="warning" sx={{ mb: 2 }}>
            <strong>Documentos Obligatorios Pendientes:</strong> Complete todos los documentos marcados como obligatorios para continuar con el proceso.
          </Alert>
        )}

        {puedeAvanzar && (
          <Alert severity="success" sx={{ mb: 2 }}>
            ¡Todos los documentos obligatorios completados! Puede continuar con el proceso de evaluación.
          </Alert>
        )}
      </Paper>

      <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 2 }}>
        {documentos.map((doc) => (
          <Box key={doc.nombre} sx={{ flex: '1 1 calc(50% - 16px)', minWidth: '280px' }}>
            <Card variant="outlined">
              <CardContent>
                <FormControlLabel
                  control={
                    <Checkbox
                      checked={checklist[doc.nombre] || false}
                      onChange={handleCheckboxChange(doc.nombre)}
                      color="primary"
                    />
                  }
                  label={
                    <Box>
                      <Typography variant="body1" sx={{ fontWeight: 'medium' }}>
                        {doc.descripcion}
                      </Typography>
                      <Chip 
                        label={doc.obligatorio ? "Obligatorio" : "Opcional"} 
                        size="small" 
                        color={doc.obligatorio ? "error" : "default"}
                        variant="outlined"
                      />
                    </Box>
                  }
                />
              </CardContent>
            </Card>
          </Box>
        ))}
      </Box>

      {error && (
        <Alert severity="error" sx={{ mt: 3 }}>
          {error}
        </Alert>
      )}

      {success && (
        <Alert severity="success" sx={{ mt: 3 }}>
          {success}
        </Alert>
      )}

      <Box sx={{ mt: 4, display: 'flex', gap: 2, justifyContent: 'center' }}>
        <Button 
          variant="outlined" 
          onClick={() => navigate('/')}
          disabled={saving}
        >
          Volver al Menú
        </Button>
        
        <Button 
          variant="contained" 
          onClick={guardarChecklist}
          disabled={saving}
        >
          {saving ? 'Guardando...' : 'Guardar Checklist'}
        </Button>
        
        <Button 
          variant="contained" 
          color="success"
          disabled={!puedeAvanzar || saving}
          onClick={() => navigate('/evaluacion/' + clienteId)}
        >
          Continuar con Evaluación
        </Button>
      </Box>
    </Box>
  );
};

export default ChecklistDocumentos;