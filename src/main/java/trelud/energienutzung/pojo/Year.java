package trelud.energienutzung.pojo;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import trelud.energienutzung.annotation.DtoEntity;
import trelud.energienutzung.annotation.ToDto;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "year")
@Data
@DtoEntity
public class Year {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long year_id;

    @ToDto
    @EqualsAndHashCode.Include
    private int year;

    @OneToMany(mappedBy = "year",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonManagedReference("yearConnection")
    private List<Connection> connections = new ArrayList<>();
}
