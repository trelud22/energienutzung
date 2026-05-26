package trelud.energienutzung.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import trelud.energienutzung.database.YearRepository;
import trelud.energienutzung.pojo.Year;
import trelud.energienutzung.service.YearService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/energienutzung/years")
@RequiredArgsConstructor
public class YearController {
    public final YearService yearService;

    @GetMapping("/")
    public ResponseEntity<List<Map<String, Object>>> getYears(){
        return ResponseEntity.ok(yearService.getYears());
    }
}
