package pl.projekt.grupa4.service;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.var;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import pl.projekt.grupa4.model.Student;
import pl.projekt.grupa4.repository.StudentRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.BiFunction;

import static org.springframework.data.r2dbc.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;
import static org.springframework.data.relational.core.query.Update.update;

@Service
public class StudentService {
    private final TransactionalOperator transactionalOperator;
    private final R2dbcEntityTemplate entityTemplate;
    private StudentRepository studentRepository;
    public static final BiFunction<Row, RowMetadata, Student> MAPPER_STUDENT = (row, rowMetaData) -> Student.builder()
            .studentId(row.get("student_id", Integer.class))
            .nazwisko(row.get("nazwisko", String.class))
            .imie(row.get("imie", String.class))
            .email(row.get("email", String.class))
            .nrIndeksu(row.get("nr_indeksu", String.class))
            .stacjonarny(row.get("stacjonarny", Boolean.class)).build();

    public StudentService(R2dbcEntityTemplate template, TransactionalOperator transactionalOperator, StudentRepository studentRepository) {
        this.transactionalOperator = transactionalOperator;
        this.entityTemplate = template;
        this.studentRepository=studentRepository;
    }

    public Flux<Student> getStudenci() {
        return this.entityTemplate.getDatabaseClient()
                .sql("SELECT * FROM student")
                .filter((statement, executeFunction) -> statement.fetchSize(100).execute())
                .map(MAPPER_STUDENT)
                .all();
    }

    public Mono<Student> getStudent(Integer studentId) {
        return this.entityTemplate
                .selectOne(query(where("student_id").is(studentId)), Student.class);
    }

    public Mono<Student> createStudent(Student student) {
        return this.entityTemplate.insert(Student.class)
                .using(student);
//.map(s -> s.getId());
    }

    public Mono<Integer> updateStudent(Student student) {
        return this.entityTemplate.update(
                query(where("student_id").is(student.getStudentId())),
                update("nazwisko", student.getNazwisko())
                        .set("imie", student.getImie())
                        .set("nr_indeksu", student.getNrIndeksu())
                        .set("email", student.getEmail())
                        .set("stacjonarny", student.isStacjonarny()),
                Student.class);
    }

    public Flux<Student> findByNazwisko(String nazwisko) {
        return this.entityTemplate
                .select(Student.class)
                .matching(query(where("nazwisko").like("%" + nazwisko + "%")).limit(100).offset(0))
                .all();
    }

    public Mono<Integer> deleteStudent(Integer studentId) {
        return this.entityTemplate
                .delete(query(where("student_id").is(studentId)), Student.class);
    }
    //@Transactional

    public Flux<Integer> saveAll(List<Student> students) {
        return this.entityTemplate.getDatabaseClient()
                .inConnectionMany(connection -> {
                    var statement = connection
                            .createStatement("INSERT INTO student (nazwisko, imie, nr_indeksu, email, stacjonarny) VALUES ($1, $2, $3, $4, $5)")
                            .returnGeneratedValues("student_id");
                    students.forEach(s -> {
                        statement
                                .bind(0, s.getNazwisko())
                                .bind(1, s.getImie())
                                .bind(2, s.getNrIndeksu())
                                .bind(3, s.getEmail())
                                .bind(4, s.isStacjonarny())
                                .add();
                    });
                    return Flux.from(statement.execute())
                            .flatMap(result -> result.map((row, rowMetadata) -> row.get("student_id", Integer.class)))
                            .as(transactionalOperator::transactional);
                });
    }
}
