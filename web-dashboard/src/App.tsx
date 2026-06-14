import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import MenuPrincipal from './pages/MenuPrincipal';
import RegistroCliente from './pages/RegistroCliente';
import ChecklistDocumentos from './pages/ChecklistDocumentos';
import EvaluacionCliente from './pages/EvaluacionCliente';
import VolteoBalances from './pages/VolteoBalances';
import IndicadoresFinancieros from './pages/IndicadoresFinancieros';
import EvaluacionAgricola from './pages/EvaluacionAgricola';
import EvaluacionPecuaria from './pages/EvaluacionPecuaria';
import SimuladorPagos from './pages/SimuladorPagos';
import { CssBaseline, ThemeProvider, createTheme } from '@mui/material';

// Tema personalizado con colores del BDP (RNF-05)
const theme = createTheme({
  palette: {
    primary: {
      main: '#003366', // Azul Corporativo
    },
    secondary: {
      main: '#FFC107', // Dorado
    },
  },
  typography: {
    fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
  },
});

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Router>
        <Routes>
          <Route path="/" element={<MenuPrincipal />} />
          <Route path="/registro-cliente" element={<RegistroCliente />} />
          <Route path="/checklist/:clienteId" element={<ChecklistDocumentos />} />
          <Route path="/evaluacion/:clienteId" element={<EvaluacionCliente />} />
          <Route path="/volteo-balances/:clienteId?" element={<VolteoBalances />} />
          <Route path="/indicadores/:clienteId" element={<IndicadoresFinancieros />} />
          <Route path="/evaluacion-agricola/:clienteId" element={<EvaluacionAgricola />} />
          <Route path="/evaluacion-pecuaria/:clienteId" element={<EvaluacionPecuaria />} />
          <Route path="/simulador-pagos/:clienteId" element={<SimuladorPagos />} />
          {/* Futuras rutas: /agricola, etc. */}
        </Routes>
      </Router>
    </ThemeProvider>
  );
}

export default App;
