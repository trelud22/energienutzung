package trelud.energienutzung.dto;

public class FuelDto {
    Long fuel_id;
    SectorDto sector;
    String fuel;

    Double SpaceAndWaterHeating;
    Double processHeatUnder200;
    Double processHeatOver200;
    Double stationaryEngines;
    Double traction;
    Double lightingAndComputing;
    Double electrochemicalPurposes;
}
