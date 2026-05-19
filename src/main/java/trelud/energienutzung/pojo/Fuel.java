package trelud.energienutzung.pojo;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "fuel")
@Data
public class Fuel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fuel_id;

    @JsonAlias({"fuel_name"})
    private String fuelName;

    @JsonAlias({"Space and water heating"})
    private Double spaceAndWaterHeating;

    @JsonAlias({"Process heat <200 °C"})
    private Double processHeatBelow200c;

    @JsonAlias({"Process heat >200 °C"})
    private Double processHeatAbove200c;

    @JsonAlias({"Stationary engines"})
    private Double stationaryEngines;

    @JsonAlias({"Traction"})
    private Double traction;

    @JsonAlias({"Lighting and computing"})
    private Double lightingAndComputing;

    @JsonAlias({"Electrochemical purposes"})
    private Double electrochemicalPurposes;
}
