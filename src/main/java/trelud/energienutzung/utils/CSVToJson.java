package trelud.energienutzung.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import trelud.energienutzung.database.*;
import trelud.energienutzung.pojo.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CSVToJson implements ApplicationRunner {

    private final ConnectionRepository connectionRepository;
    private final RegionRepository regionRepository;
    private final SectorRepository sectorRepository;
    private final YearRepository yearRepository;

    public static final boolean READ_CSV = true;
    public static final boolean READ_FROM_FILE = true;

    @Override
    public void run(@NonNull ApplicationArguments args) throws Exception {
        log.info("Starting with:  - READ_FROM_FILE:{}  - READ_CSV:{}", READ_FROM_FILE, READ_CSV);
        if(READ_CSV && READ_FROM_FILE){
            List<Connection> connections = new ArrayList<>();
            List<Region> regions = new ArrayList<>();
            List<Year> years = new ArrayList<>();
            List<Sector> sectors = new ArrayList<>();

            PathMatchingResourcePatternResolver resolver =
                    new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("2005-2024/*.csv");
            for (Resource resource : resources) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(resource.getInputStream()));
                Year currentYear = null;
                List<Region> currentRegions = new ArrayList<>();
                List<Connection> yearRegionConnection = new ArrayList<>();
                List<Connection> yearRegionSectorConnection = new ArrayList<>();
                String currentSectorName = null;
                String line;
                while ((line = reader.readLine()) != null){
                    try{
                        String[] tokens = line.split(",");
                        if(!tokens[0].isBlank()){
                            if(tokens[0].startsWith("\"") && !tokens[0].endsWith("\"")){
                                String[] realTokens = new String[tokens.length-1];
                                realTokens[0] = tokens[0] + ',' + tokens[1];
                                for (int i = 1; i < realTokens.length; i++) {
                                    realTokens[i]=tokens[i+1];
                                }
                                tokens = realTokens;
                            }
                        }

                        if(tokens[0].equals("\"Year\"")){
                            int yearNumber = Integer.parseInt(tokens[1].substring(1, tokens[1].length()-1));
                            currentYear = new Year();
                            currentYear.setYear(yearNumber);

                            currentYear = years.stream()
                                    .filter(y -> y.getYear() == yearNumber)
                                    .findFirst()
                                    .orElse(currentYear);

                            if(!years.contains(currentYear)){
                                years.add(currentYear);
                            }

                        }else if(tokens[0].isBlank()){
                            switch (tokens[1].substring(1, tokens[1].length()-1)){
                                case "Province (NUTS 2 unit)":
                                    for (Region region : getRegions(tokens)) {
                                        Connection newConnection = new Connection();
                                        String name = region.getRegionName();

                                        region = regions.stream().filter(r->
                                                                r.getRegionName().equals(name)
                                                        ).findFirst()
                                                        .orElse(region);
                                        if(!regions.contains(region)){
                                            regions.add(region);
                                        }
                                        newConnection.setYear(currentYear);
                                        newConnection.setRegion(region);

                                        currentRegions.add(region);
                                        yearRegionConnection.add(newConnection);
                                    }
                                    break;
                                case "Useful energy category":
                                    break;
                                default:
                                    connections.addAll(addCells(tokens, currentRegions, currentSectorName, yearRegionSectorConnection, currentYear.getYear()));
                                    break;
                            }
                        } else if (!tokens[0].equals("\"Sector\"")) {
                            currentSectorName = tokens[0].substring(1, tokens[0].length() - 1);

                            Sector sector = new Sector();
                            sector.setSectorName(currentSectorName);


                            String name = currentSectorName;
                            sector = sectors.stream().filter(s -> s.getSectorName().equals(name))
                                    .findFirst()
                                    .orElse(sector);

                            if(!sectors.contains(sector)){
                                sectors.add(sector);
                            }

                            for(Connection c : yearRegionConnection){
                                Connection newConnection = new Connection();
                                newConnection.setRegion(c.getRegion());
                                newConnection.setYear(c.getYear());
                                newConnection.setSector(sector);

                                yearRegionSectorConnection.add(newConnection);
                            }
                            connections.addAll(addCells(tokens, currentRegions, currentSectorName, yearRegionSectorConnection, currentYear.getYear()));
                        }
                    }catch (ArrayIndexOutOfBoundsException ex){
                        log.warn("{} IGNORED because {}\n{}", resource.getFilename(), ex.getMessage(), line);
                    }
                }
            }
            log.info("starting save");
            yearRepository.saveAll(years);
            sectorRepository.saveAll(sectors);
            regionRepository.saveAll(regions);
            connectionRepository.saveAll(connections);
            log.info("finished saving CSV");
        }
    }

    private List<Region> getRegions(String[] tokens){
        List<Region> regions = new ArrayList<>();
        for (int i = 2; i < tokens.length; i++) {
            if (!tokens[i].isBlank()){
                Region region = new Region();
                String regionName = tokens[i].split("<")[0];
                regionName = regionName.substring(1, regionName.length()-1);
                region.setRegionName(regionName);
                region.setStartColumn(i);
                regions.add(region);
            }
        }
        return regions;
    }

    private List<Connection> addCells(String[] tokens, List<Region> originalRegions, String currentSectorName, List<Connection> connections, int currentYear){
        List<Region> regions = new ArrayList<>(originalRegions);
        regions.sort(Comparator.comparingInt(Region::getStartColumn));
        Region currentRegion = null;
        Region nextRegion = regions.getFirst();
        Fuel currentFuel = null;
        List<Connection> newConnectionList = new ArrayList<>();

        List<Connection> currentConnections = connections.stream()
                .filter(c ->
                        c.getSector().getSectorName().equals(currentSectorName) &&
                                c.getYear().getYear() == currentYear
                ).toList();
        for(Connection c : currentConnections){
            Connection newConnection = new Connection();
            newConnection.setYear(c.getYear());
            newConnection.setRegion(c.getRegion());
            newConnection.setSector(c.getSector());

            Fuel newFuel = new Fuel();
            newFuel.setFuelName(tokens[1].substring(1, tokens[1].length()-1));
            newConnection.setFuel(newFuel);

            newConnectionList.add(newConnection);
        }

        for (int i = 0; i < tokens.length;){
            if(nextRegion != null &&
                    (currentRegion == null || i >= nextRegion.getStartColumn())){
                currentRegion = regions.removeFirst();

                try {
                    nextRegion = regions.getFirst();

                }catch (NoSuchElementException e){
                    nextRegion = new Region();
                    nextRegion.setStartColumn(tokens.length);
                }
                String regionName = currentRegion.getRegionName();
                currentFuel = newConnectionList.stream()
                        .filter(c ->
                                c.getRegion().getRegionName().equals(regionName)
                        ).findFirst()
                        .orElseThrow(() -> new NoSuchElementException("No Region found"))
                        .getFuel();

            }
            while(nextRegion != null && i < nextRegion.getStartColumn()){
                Double value = null;
                try{
                    value = Double.parseDouble(
                            tokens[i]
                    );
                }catch (NumberFormatException ignored){}
                if(value == null){
                    value = 0.0;
                }
                if(currentFuel != null){
                    switch ((i-2)%14){
                        case 0:
                            currentFuel.setSpaceAndWaterHeating(value);
                            break;
                        case 2:
                            currentFuel.setProcessHeatBelow200c(value);
                            break;
                        case 4:
                            currentFuel.setProcessHeatAbove200c(value);
                            break;
                        case 6:
                            currentFuel.setStationaryEngines(value);
                            break;
                        case 8:
                            currentFuel.setTraction(value);
                            break;
                        case 10:
                            currentFuel.setLightingAndComputing(value);
                            break;
                        case 12:
                            currentFuel.setElectrochemicalPurposes(value);
                            break;
                    }
                }
                i++;
            }
        }
        return newConnectionList;
    }
}
