package bo.gob.bdp.sam.core.domain.event;

import lombok.Value;
import java.util.List;

// Este evento es INMUTABLE. Queda registrado para siempre (Exigencia ASFI)
@Value
public class ModelosCargadosEvent {
    private final String usuarioId;
    private final List<String> nombresModelos; // Ej: ["Agrícola", "Pecuario"]
    private final String timestamp;
}
