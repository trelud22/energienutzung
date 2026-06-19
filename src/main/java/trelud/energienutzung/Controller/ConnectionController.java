package trelud.energienutzung.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/energy")
@RequiredArgsConstructor
public class ConnectionController {
    @GetMapping("/")
    public ResponseEntity<?> getConnectionsByYearByRegionBySectorByFuelType(
            @RequestParam int year,
            @RequestParam String region,
            @RequestParam String sector,
            @RequestParam String fuelType
    ){

        return null;
    }
}
