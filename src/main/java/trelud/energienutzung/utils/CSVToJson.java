package trelud.energienutzung.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import trelud.energienutzung.database.ConnectionRepository;
import trelud.energienutzung.pojo.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.NoSuchObjectException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CSVToJson implements ApplicationRunner {

    private final ConnectionRepository connectionRepository;

    public static final boolean READ_CSV = true;
    public static final boolean READ_FROM_FILE = true;

    @Override
    public void run(@NonNull ApplicationArguments args) throws Exception {
        if(READ_CSV && READ_FROM_FILE){
            List<Connection> connections = new ArrayList<>();
            PathMatchingResourcePatternResolver resolver =
                    new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("2005-2024/*.csv");
            for (Resource resource : resources) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(resource.getInputStream()));
                Year currentYear = null;
                List<Region> regions = new ArrayList<>();
                List<Connection> yearRegionConnection = new ArrayList<>();
                List<Connection> yearRegionSectorConnection = new ArrayList<>();
                String currentSector = null;
                String line;
                while ((line = reader.readLine()) != null){
                    try{
                        String[] tokens = line.split(",");
                        if(tokens[0].equals("\"Year\"")){
                            int yearNumber = Integer.parseInt(tokens[1].substring(1, tokens[1].length()-1));

                            if(currentYear == null || currentYear.getYear() != yearNumber){
                                Year year = new Year();
                                year.setYear(yearNumber);
                                currentYear = year;
                            }

                        }else if(tokens[0].isBlank()){
                            switch (tokens[1].substring(1, tokens[1].length()-1)){
                                case "Province (NUTS 2 unit)":
                                    for (Region region : getRegions(tokens)) {
                                        Connection newConnection = new Connection();
                                        newConnection.setYear(currentYear);
                                        newConnection.setRegion(region);

                                        regions.add(region);
                                        yearRegionConnection.add(newConnection);
                                    }
                                    break;
                                case "Useful energy category":
                                    break;
                                default:
                                    addCells(tokens, regions, currentSector, yearRegionSectorConnection);
                                    break;
                            }
                        } else if (!tokens[0].equals("\"Sector\"")) {
                            currentSector = tokens[0].substring(1, tokens[0].length() - 1);

                            Sector sector = new Sector();
                            sector.setSectorName(currentSector);
                            for(Connection c : yearRegionConnection){
                                Connection newConnection = new Connection();
                                newConnection.setRegion(c.getRegion());
                                newConnection.setYear(c.getYear());
                                newConnection.setSector(sector);

                                c.getYear().getConnections().add(newConnection);
                                c.getRegion().getConnections().add(newConnection);
                                sector.getConnections().add(newConnection);

                                yearRegionSectorConnection.add(newConnection);
                            }

                            addCells(tokens, regions, currentSector, yearRegionSectorConnection);
                        }
                    }catch (ArrayIndexOutOfBoundsException ex){
                        log.warn("{} IGNORED because {}\n{}", resource.getFilename(), ex.getMessage(), line);
                    }
                }
                connections.addAll(yearRegionSectorConnection);
            }
            connectionRepository.saveAll(connections);
            log.info("finished saving CSV");
        }
    }

    private List<Region> getRegions(String[] tokens){
        List<Region> regions = new ArrayList<>();
        for (int i = 2; i < tokens.length; i++) {
            if (!tokens[i].isBlank()){
                Region region = new Region();
                region.setRegionName(tokens[i].substring(1, tokens[i].length() - 8));
                region.setStartColumn(i);
                regions.add(region);
            }
        }
        return regions;
    }

    private void addCells(String[] tokens, List<Region> originalRegions, String currentSectorName, List<Connection> connections){
        List<Region> regions = new ArrayList<>(originalRegions);
        regions.sort(Comparator.comparingInt(Region::getStartColumn));
        Region currentRegion = null;
        Region nextRegion = regions.getFirst();
        Fuel currentFuel = null;

        List<Connection> currentConnections = connections.stream()
                .filter(c ->
                        c.getSector().getSectorName().equals(currentSectorName)
                ).toList();
        for(Connection c : currentConnections){
            Fuel newFuel = new Fuel();
            newFuel.setFuelName(tokens[1].substring(1, tokens[1].length()-1));
            newFuel.setConnection(c);

            c.setFuel(newFuel);

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

                try{
                    currentFuel = currentRegion.getConnections().stream()
                            .filter(c -> c.getSector().getSectorName().equals(currentSectorName))
                            .findFirst()
                            .orElseThrow(()-> new NoSuchObjectException(""))
                            .getFuel();
                } catch (NoSuchObjectException ignored) {}

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
    }

}
