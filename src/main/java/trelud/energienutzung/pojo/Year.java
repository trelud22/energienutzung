package trelud.energienutzung.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    List<Fuel> fuels;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "sector_id")
    @JsonBackReference
    Sector sector;
}
