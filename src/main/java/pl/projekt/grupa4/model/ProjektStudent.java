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
@Table("projekt_student")
public class ProjektStudent {
    @Id
    @Column("id_projekt") // tylko jeżeli nazwa kolumny w bazie danych ma być inna od nazwy zmiennej
    private Integer projektId;
    @Column("id_student") // tylko jeżeli nazwa kolumny w bazie danych ma być inna od nazwy zmiennej
    private Integer studentId;
}
