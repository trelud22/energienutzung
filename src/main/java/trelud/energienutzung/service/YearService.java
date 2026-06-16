package trelud.energienutzung.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import trelud.energienutzung.database.YearRepository;
import trelud.energienutzung.pojo.Year;

import java.rmi.NoSuchObjectException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class YearService {
    private final YearRepository yearRepository;

    public List<Map<String, Object>> getYears(){
        return DtoService.convertList(yearRepository.findAll());
    }

    public List<Map<String, Object>> getByYear(int year) throws NoSuchObjectException {
        Year year1 = yearRepository.findByYear(year);
        if(year1 == null) throw new NoSuchObjectException("No data for year " + year + " found");
        return DtoService.convertList(year1.getConnections());
    }
}
