package trelud.energienutzung.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import trelud.energienutzung.pojo.Region;
import trelud.energienutzung.pojo.Sector;

@Repository
public interface SectorRepository extends JpaRepository<Sector, Long> {
    @Query("SELECT s FROM Sector s WHERE s.sectorName = :sectorName")
    Sector getSectorByName(
            @Param("sectorName") String sectorName
    );
}
