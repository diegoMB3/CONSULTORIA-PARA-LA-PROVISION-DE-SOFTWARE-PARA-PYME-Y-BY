package bo.gob.bdp.sam.core.domain.event;

import lombok.Value;

@Value
public class ClienteRegistradoEvent {
    private final String clienteId;
    private final String nombreCompleto;
    private final String numeroDocumento;
    private final String email;
    private final String telefono;
}
