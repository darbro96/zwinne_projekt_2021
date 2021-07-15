package pl.projekt.grupa4.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("projekt")
public class Projekt {
    @Id
    @Column("projekt_id")
    private Integer projektId;
    @Column("nazwa")
    private String nazwa;
    @Column("opis")
    private String opis;
    @Column("dataczas_utworzenia")
    private LocalDateTime dataczasUtworzenia;
    @Column("data_oddania")
    private LocalDate dataOddania;
    @Transient
    private List<Zadanie> zadania;
    @Transient
    private Set<Student> studenci;
}
