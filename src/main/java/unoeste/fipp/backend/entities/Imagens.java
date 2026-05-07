package unoeste.fipp.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name="imagens")
public class Imagens {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="img_id")
    private Long id;

    @Column(name="img_name")
    private String name;

    @JsonIgnore
    @JoinColumn(name="den_id")
    @ManyToOne(fetch=FetchType.LAZY)
    private Denuncia denuncia;

    public Imagens(){this(0L,"",null);}

    public Imagens(Long id, String name, Denuncia denuncia) {
        this.id = id;
        this.name = name;
        this.denuncia = denuncia;
    }

    public Imagens(String name, Denuncia denuncia) {
        this.name = name;
        this.denuncia = denuncia;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Denuncia getDenuncia() {
        return denuncia;
    }

    public void setDenuncia(Denuncia denuncia) {
        this.denuncia = denuncia;
    }
}
