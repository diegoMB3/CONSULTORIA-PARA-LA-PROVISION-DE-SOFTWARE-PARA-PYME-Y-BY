package bo.gob.bdp.sam.core.application.query;

import bo.gob.bdp.sam.core.domain.event.EvaluacionProduccionRegistradaEvent;
import bo.gob.bdp.sam.adapters.out.persistence.EvaluacionProduccionEntity;
import bo.gob.bdp.sam.adapters.out.persistence.EvaluacionProduccionRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EvaluacionProduccionProjector {

    private final EvaluacionProduccionRepository repository;

    @EventHandler
    public void on(EvaluacionProduccionRegistradaEvent event) {
        EvaluacionProduccionEntity entity = new EvaluacionProduccionEntity();
        
        entity.setEvaluacionId(event.getEvaluacionId());
        entity.setClienteId(event.getClienteId());
        entity.setIngresos(event.getIngresos());
        entity.setCostosVariables(event.getCostosVariables());
        entity.setCostosFijos(event.getCostosFijos());
        entity.setUtilidadNeta(event.getUtilidadNeta());

        repository.save(entity);
        System.out.println("====== PROYECTOR BDP ======");
        System.out.println("¡Evaluación Industrial guardada en PostgreSQL con ID: " + event.getEvaluacionId() + "!");
    }
}
