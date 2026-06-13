package bo.gob.bdp.sam.adapters.in.web;

import bo.gob.bdp.sam.core.application.command.CalcularIndicadoresCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/indicadores")
@CrossOrigin(origins = "*")
public class IndicadorController {

    private final CommandGateway commandGateway;
    private final Map<String, Map<String, Double>> balancesPorCliente = new LinkedHashMap<>();

    public IndicadorController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<Map<String, Object>> calcularIndicadores(@PathVariable String clienteId) {
        Map<String, Double> balance = balancesPorCliente.getOrDefault(clienteId, getBalanceSimulado());
        
        double activoCorriente = balance.getOrDefault("activoCorriente", 0.0);
        double activoNoCorriente = balance.getOrDefault("activoNoCorriente", 0.0);
        double activoTotal = activoCorriente + activoNoCorriente;
        double pasivoCorriente = balance.getOrDefault("pasivoCorriente", 0.0);
        double pasivoNoCorriente = balance.getOrDefault("pasivoNoCorriente", 0.0);
        double pasivoTotal = pasivoCorriente + pasivoNoCorriente;
        double patrimonio = balance.getOrDefault("patrimonio", 0.0);
        double resultadoEjercicio = balance.getOrDefault("resultadoEjercicio", 0.0);
        
        double liquidezCorriente = pasivoCorriente != 0 ? activoCorriente / pasivoCorriente : 0.0;
        double endeudamiento = activoTotal != 0 ? pasivoTotal / activoTotal : 0.0;
        double solvencia = pasivoTotal != 0 ? activoTotal / pasivoTotal : 0.0;
        double roa = activoTotal != 0 ? (resultadoEjercicio / activoTotal) * 100 : 0.0;
        double roe = patrimonio != 0 ? (resultadoEjercicio / patrimonio) * 100 : 0.0;
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("clienteId", clienteId);
        response.put("liquidezCorriente", Math.round(liquidezCorriente * 100.0) / 100.0);
        response.put("endeudamiento", Math.round(endeudamiento * 100.0) / 100.0);
        response.put("solvencia", Math.round(solvencia * 100.0) / 100.0);
        response.put("roa", Math.round(roa * 100.0) / 100.0);
        response.put("roe", Math.round(roe * 100.0) / 100.0);
        response.put("interpretacion", generarInterpretacion(liquidezCorriente, endeudamiento, solvencia, roa, roe));
        
        commandGateway.send(new CalcularIndicadoresCommand(clienteId));
        
        return ResponseEntity.ok(response);
    }
    
    private Map<String, String> generarInterpretacion(double liquidez, double endeudamiento, 
                                                       double solvencia, double roa, double roe) {
        Map<String, String> interpretacion = new LinkedHashMap<>();
        interpretacion.put("liquidezCorriente", liquidez >= 1.5 ? "✅ Saludable (≥1.5)" : 
                          liquidez >= 1.0 ? "⚠️ Aceptable (1.0-1.5)" : "❌ Riesgo (<1.0)");
        interpretacion.put("endeudamiento", endeudamiento <= 0.5 ? "✅ Bajo (≤50%)" : 
                           endeudamiento <= 0.8 ? "⚠️ Moderado (50-80%)" : "❌ Alto (>80%)");
        interpretacion.put("solvencia", solvencia >= 2.0 ? "✅ Alta (≥2.0)" : 
                           solvencia >= 1.5 ? "⚠️ Media (1.5-2.0)" : "❌ Baja (<1.5)");
        interpretacion.put("roa", roa >= 10 ? "✅ Rentable (≥10%)" : 
                          roa >= 5 ? "⚠️ Regular (5-10%)" : "❌ Baja (<5%)");
        interpretacion.put("roe", roe >= 15 ? "✅ Excelente (≥15%)" : 
                          roe >= 10 ? "⚠️ Bueno (10-15%)" : "❌ Regular (<10%)");
        return interpretacion;
    }
    
    private Map<String, Double> getBalanceSimulado() {
        Map<String, Double> balance = new LinkedHashMap<>();
        balance.put("activoCorriente", 0.0);
        balance.put("activoNoCorriente", 0.0);
        balance.put("pasivoCorriente", 0.0);
        balance.put("pasivoNoCorriente", 0.0);
        balance.put("patrimonio", 0.0);
        balance.put("resultadoEjercicio", 0.0);
        return balance;
    }
}
