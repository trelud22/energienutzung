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
    private Long year_id;

    private Integer year;

    @OneToMany(
            mappedBy = "year",
            cascade = CascadeType.ALL
    )
    @JsonManagedReference
    private List<Sector> sectors;
}
