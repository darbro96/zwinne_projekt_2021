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
    @Column("id_projekt")
    private Integer projektId;
    @Column("id_student")
    private Integer studentId;
}
