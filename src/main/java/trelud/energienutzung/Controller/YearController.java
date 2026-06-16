package trelud.energienutzung.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trelud.energienutzung.database.YearRepository;
import trelud.energienutzung.pojo.Year;
import trelud.energienutzung.service.YearService;

import java.rmi.NoSuchObjectException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/energy/years")
@RequiredArgsConstructor
public class YearController {
    public final YearService yearService;

    @GetMapping("/{year}")
    public ResponseEntity<?> getByYear(
            @PathVariable int year
    ){
        try {
            return ResponseEntity.ok(yearService.getByYear(year));
        } catch (NoSuchObjectException e) {
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(404))
                    .body(e.getMessage());
        }
    }
}
