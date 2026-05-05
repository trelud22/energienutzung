package trelud.energienutzung.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import trelud.energienutzung.database.YearRepository;
import trelud.energienutzung.pojo.Year;

import java.util.List;

@RestController
@RequestMapping("/energienutzung/years")
@RequiredArgsConstructor
public class YearController {
    public final YearRepository yearRepository;

    @GetMapping("/")
    public ResponseEntity<List<Year>> getYears(){
        return ResponseEntity.ok(yearRepository.findAll());
    }
}
