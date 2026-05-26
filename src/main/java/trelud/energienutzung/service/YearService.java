package trelud.energienutzung.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import trelud.energienutzung.database.YearRepository;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class YearService {
    private final YearRepository yearRepository;

    public List<Map<String, Object>> getYears(){
        return DtoService.toDto(yearRepository.findAll());
    }
}
