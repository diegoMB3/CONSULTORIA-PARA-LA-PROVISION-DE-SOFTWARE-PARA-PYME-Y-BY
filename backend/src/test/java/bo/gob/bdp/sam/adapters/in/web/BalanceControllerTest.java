package bo.gob.bdp.sam.adapters.in.web;

import bo.gob.bdp.sam.core.application.command.ActualizarBalanceCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import org.springframework.test.web.servlet.MvcResult;

public class BalanceControllerTest {

    @Mock
    private CommandGateway commandGateway;

    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        BalanceController controller = new BalanceController(commandGateway);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void obtenerBalance_devuelve_balance_inicial_y_mensaje_cuadrado() throws Exception {
        mockMvc.perform(get("/api/v1/balances/{id}", "cliente-1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteId").value("cliente-1"))
                .andExpect(jsonPath("$.balanceCuadrado").value(true))
                .andExpect(jsonPath("$.mensaje").value("✅ Balance Cuadrado"));
    }

    @Test
    void actualizarBalance_retornara_objeto_con_info_y_balanceActualizado() throws Exception {
        when(commandGateway.send(any(ActualizarBalanceCommand.class))).thenReturn(CompletableFuture.completedFuture("ok"));

        java.util.Map<String, Double> cuentas = new java.util.LinkedHashMap<>();
        cuentas.put("CAJA_BANCOS", 100.0);
        cuentas.put("CUENTAS_COBRAR", 0.0);
        cuentas.put("INVENTARIOS", 0.0);
        cuentas.put("TERRENOS", 0.0);
        cuentas.put("EDIFICIOS", 0.0);
        cuentas.put("MAQUINARIA", 0.0);
        cuentas.put("VEHICULOS", 0.0);
        cuentas.put("PROVEEDORES", 50.0);
        cuentas.put("IMPUESTOS_POR_PAGAR", 0.0);
        cuentas.put("SUELDOS_POR_PAGAR", 40.0);
        cuentas.put("PRESTAMOS_BANCARIOS_LP", 0.0);
        cuentas.put("CAPITAL_SOCIAL", 10.0);
        cuentas.put("RESERVAS", 0.0);
        cuentas.put("RESULTADOS_ACUMULADOS", 0.0);
        cuentas.put("RESULTADO_EJERCICIO", 0.0);

        String body = mapper.writeValueAsString(cuentas);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/balances/{id}", "cliente-2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(request().asyncStarted())
            .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.mensaje").value("Balance actualizado"))
            .andExpect(jsonPath("$.activoTotal").isNumber())
            .andExpect(jsonPath("$.balanceCuadrado").isBoolean());
    }

    @Test
    void validarBalanceFinal_retornara_mensaje_aprobado_al_confirmar() throws Exception {
        when(commandGateway.send(any(ActualizarBalanceCommand.class))).thenReturn(CompletableFuture.completedFuture("ok"));

        java.util.Map<String, Double> cuentas = new java.util.LinkedHashMap<>();
        cuentas.put("CAJA_BANCOS", 0.0);

        String body = mapper.writeValueAsString(cuentas);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/balances/{id}/validar", "cliente-3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(request().asyncStarted())
            .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.mensaje").exists())
            .andExpect(jsonPath("$.validacion").value("APROBADA"));
    }
}
