package pl.projekt.grupa4.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import pl.projekt.grupa4.model.Projekt;

public interface ProjektRepository extends R2dbcRepository<Projekt, Integer> {

}
