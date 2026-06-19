package trelud.energienutzung.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import trelud.energienutzung.service.RegionService;

import java.rmi.NoSuchObjectException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/energy/region")
@RequiredArgsConstructor
public class RegionController {
    private final RegionService regionService;

//    @GetMapping("/{region}")
//    public ResponseEntity<List<Map<String, Object>>> getRegionByName(){
//
//    }
}
