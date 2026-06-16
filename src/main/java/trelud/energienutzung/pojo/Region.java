package trelud.energienutzung.pojo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import trelud.energienutzung.annotation.ToDto;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "region")
@Data
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long region_id;

    @ToDto
    @JsonAlias({"region_name"})
    @EqualsAndHashCode.Include
    private String regionName;

    @OneToMany(mappedBy = "region",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonManagedReference("regionConnection")
    private List<Connection> connections = new ArrayList<>();

    @Transient
    @JsonIgnore
    private int startColumn;
}
