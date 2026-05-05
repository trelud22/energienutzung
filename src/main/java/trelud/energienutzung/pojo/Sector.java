package trelud.energienutzung.pojo;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Sector {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long sector_id;

    String sector;

    @OneToMany(
            mappedBy = "sector",
            cascade = CascadeType.ALL
    )
    @JsonManagedReference
    List<Year> years;
}
