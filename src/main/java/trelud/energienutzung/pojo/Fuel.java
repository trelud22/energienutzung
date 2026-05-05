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
    Long fuel_id;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "sector_id")
    @JsonBackReference
    Sector sector;

    String fuel;

    @JsonProperty("Space and water heating")
    Double SpaceAndWaterHeating;

    @JsonProperty("Process heat <200 °C")
    Double processHeatUnder200;

    @JsonProperty("Process heat >200 °C")
    Double processHeatOver200;

    @JsonProperty("Stationary engines")
    Double stationaryEngines;

    @JsonProperty("Traction")
    Double traction;

    @JsonProperty("Lighting and computing")
    Double lightingAndComputing;

    @JsonProperty("Electrochemical purposes")
    Double electrochemicalPurposes;
}
