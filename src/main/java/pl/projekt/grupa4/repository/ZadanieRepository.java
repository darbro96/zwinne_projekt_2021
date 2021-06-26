package pl.projekt.grupa4.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import pl.projekt.grupa4.model.Zadanie;

public interface ZadanieRepository extends R2dbcRepository<Zadanie, Integer> {
}
