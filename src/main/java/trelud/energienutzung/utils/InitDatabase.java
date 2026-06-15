//package trelud.energienutzung.database;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import trelud.energienutzung.pojo.Year;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.List;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class InitDatabase implements ApplicationRunner {
//    public final YearRepository yearRepository;
//
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        if(!CSVToJson.READ_CSV && CSVToJson.READ_FROM_FILE){
//            try {
//                InputStream is = this.getClass().getResourceAsStream("/data.json");
//
//                ObjectMapper objectMapper = new ObjectMapper();
//
//                List<Year> years = objectMapper
//                        .readerForListOf(Year.class)
//                        .readValue(is);
//
//                years.forEach(y -> {
//                    y.getRegions().forEach(r -> {
//                        r.getSectors().forEach(s -> {
//                            s.getFuels().forEach(f -> {
//                                f.setSector(s);
//                            });
//                            s.setRegion(r);
//                        });
//                        r.setYear(y);
//                    });
//                });
//
//                yearRepository.saveAll(years);
//                log.info("finished saving JSON");
//            } catch (IOException ioex) {
//                log.warn("File problem");
//                log.debug("File problem " + ioex.getMessage());
//                throw new RuntimeException(ioex);
//            }
//        }
//
//    }
//}