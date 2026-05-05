package trelud.energienutzung.dto;

import trelud.energienutzung.pojo.Sector;

import java.util.List;

public class SectorDto {
    String sector;
    List<FuelDto> fuels;
    YearDto year;

    public static SectorDto toDto(Sector sector){
        SectorDto sectorDto = new SectorDto();
        sectorDto.year = YearDto.toDto(sector.getYear());
        sectorDto.sector = sector.getSector();
        sectorDto.fuels = sector.getFuels().stream()
                .map(FuelDto::toDto).toList();
        return sectorDto;
    }
}
