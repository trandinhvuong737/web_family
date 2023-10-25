package deepstream.ttrack.entity.address;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cities")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private int code;

    @Column(name = "codename")
    private String codename;

    @Column(name = "division_type")
    private String divisionType;

    @Column(name = "phone_code ")
    private int phoneCode;

    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    private List<District> districts;
}
