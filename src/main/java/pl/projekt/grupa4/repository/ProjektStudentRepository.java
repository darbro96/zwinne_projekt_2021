package pl.projekt.grupa4.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import pl.projekt.grupa4.model.ProjektStudent;

public interface ProjektStudentRepository extends R2dbcRepository<ProjektStudent, Integer> {
}
