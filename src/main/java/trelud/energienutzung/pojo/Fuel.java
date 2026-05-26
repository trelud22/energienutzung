package trelud.energienutzung.pojo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import trelud.energienutzung.annotation.DtoEntity;
import trelud.energienutzung.annotation.ToDto;

@Entity
@Table(name = "fuel")
@Data
@DtoEntity
public class Fuel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fuel_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_id", nullable = false)
    @ToString.Exclude
    @JsonBackReference("sectorFuels")
    private Sector sector;

    @ToDto
    @JsonAlias({"fuel_name"})
    private String fuelName;

    @ToDto
    @JsonAlias({"Space and water heating"})
    private Double spaceAndWaterHeating;

    @ToDto
    @JsonAlias({"Process heat <200 °C"})
    private Double processHeatBelow200c;

    @ToDto
    @JsonAlias({"Process heat >200 °C"})
    private Double processHeatAbove200c;

    @ToDto
    @JsonAlias({"Stationary engines"})
    private Double stationaryEngines;

    @ToDto
    @JsonAlias({"Traction"})
    private Double traction;

    @ToDto
    @JsonAlias({"Lighting and computing"})
    private Double lightingAndComputing;

    @ToDto
    @JsonAlias({"Electrochemical purposes"})
    private Double electrochemicalPurposes;
}
