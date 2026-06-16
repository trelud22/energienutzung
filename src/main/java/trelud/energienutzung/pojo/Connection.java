package trelud.energienutzung.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import trelud.energienutzung.annotation.ToDto;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Connection {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long connectionId;

    @ManyToOne
    @JoinColumn(name = "year_id", nullable = false)
    @JsonBackReference("yearConnection")
    @ToString.Exclude
    @ToDto
    private Year year;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fuel_id")
    @JsonBackReference("fuelConnection")
    @ToString.Exclude
    @ToDto
    private Fuel fuel;

    @ManyToOne
    @JoinColumn(name = "region_id", nullable = false)
    @JsonBackReference("regionConnection")
    @ToString.Exclude
    @ToDto
    private Region region;

    @ManyToOne
    @JoinColumn(name = "sector_id", nullable = false)
    @JsonBackReference("sectorConnection")
    @ToString.Exclude
    @ToDto
    private Sector sector;
}
