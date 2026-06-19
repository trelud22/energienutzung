package trelud.energienutzung.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import trelud.energienutzung.database.*;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ConnectionService {
    private final ConnectionRepository connectionRepository;

    private final FuelRepository fuelRepository;
    private final SectorRepository sectorRepository;
    private final RegionRepository regionRepository;
    private final YearRepository yearRepository;

    public List<Map<String, Object>> getConnectionsByYearByRegionBySectorByFuelType(
            int yearNumber,
            String regionName,
            String sectorName,
            String fuelTypeName
    ){
        return null;
    }

}
