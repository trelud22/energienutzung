package trelud.energienutzung.database;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import trelud.energienutzung.pojo.Sector;
import trelud.energienutzung.pojo.Year;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitDatabase implements ApplicationRunner {
    public final YearRepository yearRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try{
            InputStream is = this.getClass().getResourceAsStream("/energy.json");

            ObjectMapper objectMapper = new ObjectMapper();

            List<Year> years = objectMapper
                    .readerForListOf(Year.class)
                    .readValue(is);

            years.forEach(y -> {
                y.getSectors().forEach(s -> {
                    s.getFuels().forEach(f -> {
                        f.setSector(s);
                    });
                    s.setYear(y);
                });
            });


            yearRepository.saveAll(years);
        }catch (IOException ioex){
            log.warn("File problem");
            log.debug("File problem " + ioex.getMessage());
            throw new RuntimeException(ioex);
        }
    }
}
