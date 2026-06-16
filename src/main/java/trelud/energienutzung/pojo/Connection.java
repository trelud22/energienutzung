package trelud.energienutzung.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import trelud.energienutzung.annotation.DtoEntity;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@DtoEntity
public class Connection {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long connectionId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "year_id", nullable = false)
    @JsonBackReference("yearConnection")
    @ToString.Exclude
    private Year year;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fuel_id")
    @JsonBackReference("fuelConnection")
    @ToString.Exclude
    private Fuel fuel;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "region_id", nullable = false)
    @JsonBackReference("regionConnection")
    @ToString.Exclude
    private Region region;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sector_id", nullable = false)
    @JsonBackReference("sectorConnection")
    @ToString.Exclude
    private Sector sector;
}
