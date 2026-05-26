package trelud.energienutzung.pojo;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long year_id;

    @ToDto
    private int year;

    @ToDto
    @OneToMany(mappedBy = "year",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonManagedReference("yearRegions")
    private List<Region> regions = new ArrayList<>();
}
