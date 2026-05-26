package trelud.energienutzung.pojo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sector_id;

    @ToDto
    @JsonAlias({"sector_name"})
    private String sectorName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    @ToString.Exclude
    @JsonBackReference("regionSectors")
    private Region region;

    @ToDto
    @OneToMany(mappedBy = "sector",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonManagedReference("sectorFuels")
    private List<Fuel> fuels = new ArrayList<>();
}
