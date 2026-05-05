package trelud.energienutzung.dto;

import trelud.energienutzung.pojo.Fuel;

public class FuelDto {
    SectorDto sector;
    String fuel;

    Double SpaceAndWaterHeating;
    Double processHeatUnder200;
    Double processHeatOver200;
    Double stationaryEngines;
    Double traction;
    Double lightingAndComputing;
    Double electrochemicalPurposes;

    public static FuelDto toDto(Fuel fuel){
        FuelDto fuelDto = new FuelDto();
        fuelDto.fuel = fuel.getFuel();

        fuelDto.SpaceAndWaterHeating = fuel.getSpaceAndWaterHeating();
        fuelDto.processHeatUnder200 = fuel.getProcessHeatUnder200();
        fuelDto.processHeatOver200 = fuel.getProcessHeatOver200();
        fuelDto.stationaryEngines = fuel.getStationaryEngines();
        fuelDto.traction = fuel.getTraction();
        fuelDto.lightingAndComputing = fuel.getLightingAndComputing();
        fuelDto.electrochemicalPurposes = fuel.getElectrochemicalPurposes();

        fuelDto.sector = SectorDto.toDto(fuel.getSector());
        return fuelDto;
    }
}
