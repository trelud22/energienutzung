package trelud.energienutzung.database;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import trelud.energienutzung.pojo.Year;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CSVToJson implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Year> years = new ArrayList<>();
        PathMatchingResourcePatternResolver resolver =
                new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("2005-2024/*.csv");
        for (Resource resource : resources) {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null){
                String[] tokens = line.split(",");
                if(tokens[0].equals("\"Year\"")){
                    Integer yearNumber = Integer.parseInt(tokens[1].substring(1, tokens[1].length()-1));

                    Year year = years.stream()
                            .filter(y -> y.getYear().equals(yearNumber))
                            .findFirst()
                            .orElse(null);
                    if(year == null){
                        year = new Year();
                        year.setYear(
                                yearNumber
                        );

                        years.add(year);
                    }
                }
            }
        }
        log.info(years.toString());
    }
}
