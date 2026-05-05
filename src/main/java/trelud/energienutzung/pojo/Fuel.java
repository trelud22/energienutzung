package trelud.energienutzung.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Fuel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long fuel_id;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "sector_id")
    @JsonBackReference
    private Sector sector;

    private String fuel;

    @JsonProperty("Space and water heating")
    private Double SpaceAndWaterHeating;

    @JsonProperty("Process heat <200 °C")
    private Double processHeatUnder200;

    @JsonProperty("Process heat >200 °C")
    private Double processHeatOver200;

    @JsonProperty("Stationary engines")
    private Double stationaryEngines;

    @JsonProperty("Traction")
    private Double traction;

    @JsonProperty("Lighting and computing")
    private Double lightingAndComputing;

    @JsonProperty("Electrochemical purposes")
    private Double electrochemicalPurposes;
}
