package trelud.energienutzung.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Sector {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long sector_id;

    private String sector;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "year_id")
    @JsonBackReference
    private Year year;

    @OneToMany(
            mappedBy = "sector",
            cascade = CascadeType.ALL
    )
    @JsonManagedReference
    private List<Fuel> fuels;
}
