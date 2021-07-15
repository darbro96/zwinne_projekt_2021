package pl.projekt.grupa4.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("student")
public class Student {

    @Id
    @Column("student_id")
    private Integer studentId;
    @Column("imie")
    private String imie;
    @Column("nazwisko")
    private String nazwisko;
    @Column("nr_indeksu")
    private String nrIndeksu;
    @Column("email")
    private String email;
    @Column("stacjonarny")
    private boolean stacjonarny;
}
