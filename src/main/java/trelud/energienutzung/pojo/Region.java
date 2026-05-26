package trelud.energienutzung.pojo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import trelud.energienutzung.annotation.DtoEntity;
import trelud.energienutzung.annotation.ToDto;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "region")
@Data
@DtoEntity
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long region_id;

    @ToDto
    @JsonAlias({"region_name"})
    private String regionName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "year_id", nullable = false)
    @ToString.Exclude
    @JsonBackReference("yearRegions")
    private Year year;

    @ToDto
    @OneToMany(mappedBy = "region",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonManagedReference("regionSectors")
    private List<Sector> sectors = new ArrayList<>();

    @Transient
    @JsonIgnore
    private int startColumn;
}
