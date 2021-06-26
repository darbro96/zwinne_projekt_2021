package pl.projekt.grupa4.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("zadanie")
public class Zadanie {
    @Id
   // @GeneratedValue
    @Column("zadanie_id")
    private Integer zadanieId;
    @Column("nazwa")
    private String nazwa;
    @Column("kolejnosc")
    private int kolejnosc;
    @Column("opis")
    private String opis;
    @Column("dataczas_oddania")
    private LocalDateTime dataczasOddania;
    @Column("projekt_id")
    private int projektId;
}
