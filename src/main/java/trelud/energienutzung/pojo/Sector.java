package trelud.energienutzung.pojo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import trelud.energienutzung.annotation.DtoEntity;
import trelud.energienutzung.annotation.ToDto;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sector")
@Data
@DtoEntity
public class Sector {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long sector_id;

    @ToDto
    @JsonAlias({"sector_name"})
    @EqualsAndHashCode.Include
    private String sectorName;

    @OneToMany(mappedBy = "sector",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonManagedReference("sectorConnection")
    private List<Connection> connections = new ArrayList<>();
}
