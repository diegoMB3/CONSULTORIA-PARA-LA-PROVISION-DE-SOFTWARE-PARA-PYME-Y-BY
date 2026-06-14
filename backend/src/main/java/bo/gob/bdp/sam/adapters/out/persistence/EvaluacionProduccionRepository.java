package bo.gob.bdp.sam.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluacionProduccionRepository extends JpaRepository<EvaluacionProduccionEntity, String> {
    // Hereda todos los métodos CRUD (save, findById, delete) indexados por clienteId (String)
}