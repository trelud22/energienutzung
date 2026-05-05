package trelud.energienutzung.database;

import org.springframework.data.jpa.repository.JpaRepository;
import trelud.energienutzung.pojo.Year;

public interface YearRepository extends JpaRepository<Year, Long> {
}
