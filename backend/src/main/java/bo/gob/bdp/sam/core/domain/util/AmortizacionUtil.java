package bo.gob.bdp.sam.core.domain.util;

import bo.gob.bdp.sam.core.domain.event.PlanPagoGeneradoEvent.CuotaDetalle;
import java.util.ArrayList;
import java.util.List;

public class AmortizacionUtil {

    public static List<CuotaDetalle> generarCronogramaFrances(double monto, double tasaAnual, int plazoMeses) {
        double tasaMensual = (tasaAnual / 100.0) / 12.0;
        double cuota;

        if (tasaMensual == 0) {
            cuota = monto / plazoMeses;
        } else {
            cuota = monto * (tasaMensual * Math.pow(1 + tasaMensual, plazoMeses))
                    / (Math.pow(1 + tasaMensual, plazoMeses) - 1);
        }
        cuota = Math.round(cuota * 100.0) / 100.0;

        List<CuotaDetalle> plan = new ArrayList<>();
        double saldo = monto;

        for (int i = 1; i <= plazoMeses; i++) {
            double interes = Math.round((saldo * tasaMensual) * 100.0) / 100.0;
            double capital = Math.round((cuota - interes) * 100.0) / 100.0;

            if (i == plazoMeses) {
                capital = Math.round(saldo * 100.0) / 100.0;
                cuota = Math.round((capital + interes) * 100.0) / 100.0;
            }

            saldo = Math.round((saldo - capital) * 100.0) / 100.0;
            if (saldo < 0) {
                saldo = 0.0;
            }

            plan.add(new CuotaDetalle(i, capital, interes, cuota, saldo));
        }

        return plan;
    }
}
