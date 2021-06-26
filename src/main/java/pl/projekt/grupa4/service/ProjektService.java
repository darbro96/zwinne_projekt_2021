package pl.projekt.grupa4.service;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import pl.projekt.grupa4.model.Projekt;
import pl.projekt.grupa4.model.ProjektStudent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.BiFunction;

import static org.springframework.data.r2dbc.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Service
public class ProjektService {
    private final TransactionalOperator transactionalOperator;
    private final R2dbcEntityTemplate entityTemplate;
    public static final BiFunction<Row, RowMetadata, Projekt> MAPPER_PROJEKT = (row, rowMetaData) -> Projekt.builder()
            .projektId(row.get("projekt_id", Integer.class))
            .dataOddania(row.get("data_oddania", LocalDate.class))
            .dataczasUtworzenia(row.get("dataczas_utworzenia", LocalDateTime.class))
            .nazwa(row.get("nazwa", String.class))
            .opis(row.get("opis", String.class)).build();
    public static final BiFunction<Row, RowMetadata, ProjektStudent> MAPPER_PROJEKT_STUDENT = (row, rowMetaData) -> ProjektStudent.builder()
            .projektId(row.get("id_projekt", Integer.class))
            .studentId(row.get("id_student", Integer.class)).build();

    public ProjektService(R2dbcEntityTemplate template, TransactionalOperator transactionalOperator) {
        this.transactionalOperator = transactionalOperator;
        this.entityTemplate = template;
    }

    public Flux<Projekt> getProjekty() {
        return this.entityTemplate.getDatabaseClient()
                .sql("SELECT * FROM projekt")
                .filter((statement, executeFunction) -> statement.fetchSize(100).execute())
                .map(MAPPER_PROJEKT)
                .all();
    }

    public Mono<Projekt> getProjekt(int id) {
        return this.entityTemplate.selectOne(query(where("projekt_id").is(id)), Projekt.class);
    }

    public Mono<Projekt> createProjekt(Projekt projekt) {
        return this.entityTemplate.insert(Projekt.class)
                .using(projekt);
    }

    public Mono<ProjektStudent> addStudentToProjekt(ProjektStudent projektStudent) {
        return this.entityTemplate.insert(ProjektStudent.class).using(projektStudent);
    }

    public Flux<ProjektStudent> studentIdsFromProjekt(int projektId) {
        return this.entityTemplate.getDatabaseClient()
                .sql("SELECT * FROM projekt_student WHERE id_projekt=" + projektId)
                .filter((statement, executeFunction) -> statement.fetchSize(100).execute())
                .map(MAPPER_PROJEKT_STUDENT)
                .all();
    }

    public Flux<ProjektStudent> studentsProjects(int studentId) {
        return this.entityTemplate.getDatabaseClient()
                .sql("SELECT * FROM projekt_student WHERE id_student=" + studentId)
                .filter((statement, executeFunction) -> statement.fetchSize(100).execute())
                .map(MAPPER_PROJEKT_STUDENT)
                .all();
    }

    public Mono<Integer> deleteProjekt(int studentId) {
        return this.entityTemplate.delete(query(where("projekt_id").is(studentId)), Projekt.class);
    }
}
