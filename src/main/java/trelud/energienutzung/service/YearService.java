package trelud.energienutzung.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import trelud.energienutzung.database.YearRepository;
import trelud.energienutzung.pojo.Year;

import java.rmi.NoSuchObjectException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class YearService {
    private final YearRepository yearRepository;

    public List<Map<String, Object>> getYears(){
        return DtoService.convertList(yearRepository.findAll());
    }

    public List<Map<String, Object>> getByYear(int year) throws NoSuchObjectException {
        Year yearObject = yearRepository.findByYear(year);
        if(yearObject == null) throw new NoSuchObjectException("No data for year " + year + " found");
        return DtoService.convertList(yearObject.getConnections());
        //return yearObject.getConnections();
    }
}
