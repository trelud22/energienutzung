package trelud.energienutzung.dto;

import trelud.energienutzung.pojo.Year;

import java.util.List;

public class YearDto {
    Integer year;
    List<SectorDto> sectors;

    public static YearDto toDto(Year year){
        YearDto yearDto = new YearDto();
        yearDto.year = year.getYear();
        yearDto.sectors = year.getSectors().stream()
                .map(SectorDto::toDto).toList();
        return yearDto;
    }
}
