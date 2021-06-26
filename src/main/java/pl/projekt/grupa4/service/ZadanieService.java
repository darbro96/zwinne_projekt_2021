package pl.projekt.grupa4.service;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import pl.projekt.grupa4.model.Zadanie;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.function.BiFunction;

import static org.springframework.data.r2dbc.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;
import static org.springframework.data.relational.core.query.Update.update;

@Service
public class ZadanieService {
    private final TransactionalOperator transactionalOperator;
    private final R2dbcEntityTemplate entityTemplate;
    public static final BiFunction<Row, RowMetadata, Zadanie> MAPPER_ZADANIA = (row, rowMetaData) -> Zadanie.builder()
            .zadanieId(row.get("zadanie_id", Integer.class))
            .nazwa(row.get("nazwa", String.class))
            .kolejnosc(row.get("kolejnosc", Integer.class))
            .dataczasOddania(row.get("dataczas_oddania", LocalDateTime.class))
            .opis(row.get("opis", String.class))
            .projektId(row.get("projekt_id",Integer.class)).build();

    public ZadanieService(R2dbcEntityTemplate template, TransactionalOperator transactionalOperator) {
        this.transactionalOperator = transactionalOperator;
        this.entityTemplate = template;
    }

    public Flux<Zadanie> getZadania() {
        return this.entityTemplate.getDatabaseClient()
                .sql("SELECT * FROM zadanie")
                .filter((statement, executeFunction) -> statement.fetchSize(100).execute())
                .map(MAPPER_ZADANIA)
                .all();
    }

    public Mono<Zadanie> getZadanie(Integer zadanieId) {
        return this.entityTemplate
                .selectOne(query(where("zadanie_id").is(zadanieId)), Zadanie.class);
    }

    public Flux<Zadanie> getZadaniaFromProjekt(Integer projektId) {
        return this.entityTemplate.getDatabaseClient()
                .sql("SELECT * FROM zadanie WHERE projekt_id=" + projektId)
                .filter((statement, executeFunction) -> statement.fetchSize(100).execute())
                .map(MAPPER_ZADANIA)
                .all();
    }

    public Mono<Zadanie> createZadanie(Zadanie zadanie) {
        return this.entityTemplate.insert(Zadanie.class)
                .using(zadanie);
    }

    public Mono<Integer> updateZadanie(Zadanie zadanie) {
        return this.entityTemplate.update(
                query(where("zadanie_id").is(zadanie.getZadanieId())),
                update("nazwa", zadanie.getNazwa())
                        .set("kolejnosc", zadanie.getKolejnosc())
                        .set("dataczas_oddania", zadanie.getDataczasOddania())
                        .set("opis", zadanie.getOpis())
                        .set("projekt_id", zadanie.getProjektId()),
                Zadanie.class);
    }

    public Mono<Integer> deleteZadanie(Integer zadanieId) {
        return this.entityTemplate
                .delete(query(where("zadanie_id").is(zadanieId)), Zadanie.class);
    }
}
