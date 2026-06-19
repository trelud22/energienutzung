package trelud.energienutzung.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import trelud.energienutzung.service.ConnectionService;

import java.rmi.NoSuchObjectException;

@RestController
@RequestMapping("/energy")
@RequiredArgsConstructor
public class ConnectionController {

    private final ConnectionService connectionService;

    @GetMapping("/")
    public ResponseEntity<?> getConnectionsByYearByRegionBySectorByFuelType(
            @RequestParam int year,
            @RequestParam String region,
            @RequestParam String sector,
            @RequestParam String fuelType
    ){
        try {
            return ResponseEntity.ok(connectionService.getConnectionsByYearByRegionBySectorByFuelType(year, region, sector, fuelType));
        } catch (NoSuchObjectException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}
