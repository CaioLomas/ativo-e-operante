package unoeste.fipp.backend.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="tipo")
public class Tipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="tip_id")
    private Long id;

    @Column(name="tip_nome")
    private String nome;

    @OneToMany(mappedBy = "tipo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Denuncia> denuncias;

    public Tipo(){this(0L,"");}

    public Tipo(String nome) {
        this.nome = nome;
    }

    public Tipo(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
