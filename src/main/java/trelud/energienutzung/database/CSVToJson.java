package trelud.energienutzung.database;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import trelud.energienutzung.pojo.Fuel;
import trelud.energienutzung.pojo.Region;
import trelud.energienutzung.pojo.Sector;
import trelud.energienutzung.pojo.Year;

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

    public final YearRepository yearRepository;

    public static final boolean READ_CSV = false;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(READ_CSV){
            List<Year> years = new ArrayList<>();
            PathMatchingResourcePatternResolver resolver =
                    new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("2005-2024/*.csv");
            for (Resource resource : resources) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(resource.getInputStream()));
                Year currentYear = null;
                List<Region> regions = new ArrayList<>();
                String currentSector = null;
                String line;
                while ((line = reader.readLine()) != null){
                    try{
                        String[] tokens = line.split(",");
                        if(tokens[0].equals("\"Year\"")){
                            int yearNumber = Integer.parseInt(tokens[1].substring(1, tokens[1].length()-1));

                            Year year = years.stream()
                                    .filter(y -> y.getYear() == yearNumber)
                                    .findFirst()
                                    .orElse(null);
                            if(year == null){
                                year = new Year();
                                year.setYear(
                                        yearNumber
                                );
                                years.add(year);
                            }
                            currentYear = year;
                        }else if(tokens[0].isBlank()){
                            switch (tokens[1].substring(1, tokens[1].length()-1)){
                                case "Province (NUTS 2 unit)":
                                    for (Region region : getRegions(tokens)) {
                                        region.setYear(currentYear);
                                        currentYear.getRegions().add(region);
                                        regions.add(region);
                                    }
                                    break;
                                case "Useful energy category":
                                    break;
                                default:
                                    addCells(tokens, regions, currentSector);
                                    break;
                            }
                        } else if (!tokens[0].equals("\"Sector\"")) {
                            currentSector = tokens[0].substring(1, tokens[0].length() - 1);
                            for (Region region : regions) {
                                Sector sector = new Sector();
                                sector.setSectorName(currentSector);
                                sector.setRegion(region);
                                region.getSectors().add(sector);
                            }
                            addCells(tokens, regions, currentSector);
                        }
                    }catch (ArrayIndexOutOfBoundsException ignored){
                        log.info(resource.getFilename() + " IGNORED because " + ignored.getMessage() + "\n" + line);
                    }
                }
            }
            yearRepository.saveAll(years);
            log.info("finished saving CSV");
        }
    }


    private List<Region> getRegions(String[] tokens){
        List<Region> regions = new ArrayList<>();
        for (int i = 2; i < tokens.length; i++) {
            if (!tokens[i].isBlank()){
                Region region = new Region();
                region.setRegionName(tokens[i].substring(1, tokens[i].length() - 1));
                region.setStartColumn(i);
                regions.add(region);
            }
        }
        return regions;
    }

    private void addCells(String[] tokens, List<Region> originalRegions, String currentSectorName){
        List<Region> regions = new ArrayList<>(originalRegions);
        regions.sort(Comparator.comparingInt(Region::getStartColumn));
        Region currentRegion = null;
        Region nextRegion = regions.getFirst();
        Fuel currentFuel = null;
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
                currentFuel = new Fuel();
                currentFuel.setFuelName(tokens[1].substring(1, tokens[1].length()-1));
                Sector currentSector =
                        currentRegion.getSectors().stream()
                                .filter(s -> s.getSectorName().equals(currentSectorName))
                                .findFirst()
                                .orElse(null);
                currentSector.getFuels().add(currentFuel);
                currentFuel.setSector(currentSector);
            }
            while(i < nextRegion.getStartColumn()){
                Double value = null;
                try{
                    value = Double.parseDouble(
                            tokens[i]
                    );
                }catch (NumberFormatException ignored){}
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
                i++;
            }
        }
    }

}
