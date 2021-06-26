package pl.projekt.grupa4.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import pl.projekt.grupa4.model.Student;

public interface StudentRepository extends R2dbcRepository<Student, Integer> {
}
