package bo.gob.bdp.sam.core.domain.event;

import lombok.Value;
import java.util.List;

@Value
public class PlanPagoGeneradoEvent {
    private final String clienteId;
    private final double monto;
    private final double tasaAnual;
    private final int plazoMeses;
    private final double cuotaMensual;
    private final List<CuotaDetalle> cronograma;
    private final String timestamp;

    @Value
    public static class CuotaDetalle {
        int cuota;
        double capital;
        double interes;
        double total;
        double saldo;
    }
}
