package trelud.energienutzung.pojo;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Year {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long year_id;

    Integer year;

    @OneToMany(
            mappedBy = "year",
            cascade = CascadeType.ALL
    )
    @JsonManagedReference
    List<Sector> sectors;
}
