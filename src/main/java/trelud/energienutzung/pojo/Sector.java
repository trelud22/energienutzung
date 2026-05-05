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
    Long sector_id;

    String sector;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "year_id")
    @JsonBackReference
    Year year;

    @OneToMany(
            mappedBy = "sector",
            cascade = CascadeType.ALL
    )
    @JsonManagedReference
    List<Fuel> fuels;
}
