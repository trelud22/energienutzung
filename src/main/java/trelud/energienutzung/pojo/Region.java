package trelud.energienutzung.pojo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import trelud.energienutzung.annotation.DtoEntity;
import trelud.energienutzung.annotation.ToDto;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "region")
@Data
@DtoEntity
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long region_id;

    @ToDto
    @JsonAlias({"region_name"})
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
