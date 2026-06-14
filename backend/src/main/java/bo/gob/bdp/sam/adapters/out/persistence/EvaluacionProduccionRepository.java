package bo.gob.bdp.sam.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluacionProduccionRepository extends JpaRepository<EvaluacionProduccionEntity, String> {
    // Hereda todos los métodos CRUD (save, findById, delete) indexados por clienteId (String)
}