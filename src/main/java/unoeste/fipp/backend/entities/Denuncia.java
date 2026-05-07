package unoeste.fipp.backend.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="denuncia")
public class Denuncia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="den_id")
    private Long id;

    @Column(name="den_titulo")
    private String titulo;

    @Column(name="den_texto")
    private String texto;

    @Column(name="den_urgencia")
    private int urgencia;

    @JoinColumn(name="org_id")
    @ManyToOne(fetch=FetchType.LAZY)
    private Orgao orgao;

    @Column(name="den_data")
    private LocalDate data;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="tip_id")
    private Tipo tipo;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="usu_id")
    private Usuario usuario;

    @OneToOne(mappedBy = "denuncia", cascade = CascadeType.REMOVE)
    private Feedback feedback;

    @OneToMany(mappedBy="denuncia",fetch = FetchType.EAGER)
    private List<Imagens> imagens = new ArrayList<>();

    public Denuncia() {
        this(0L,"","",0,null,null,null,null);
    }

    public Denuncia(Long id, String titulo, String texto, int urgencia, Orgao orgao, Tipo tipo, Usuario usuario, LocalDate data) {
        this.id = id;
        this.titulo = titulo;
        this.texto = texto;
        this.urgencia = urgencia;
        this.orgao = orgao;
        this.tipo = tipo;
        this.usuario = usuario;
        this.data = data;
    }

    public Denuncia(String titulo, String texto, int urgencia, LocalDate data, Orgao orgao, Tipo tipo, Usuario usuario) {
        this(0L,titulo,texto,urgencia,orgao,tipo,usuario,data);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public int getUrgencia() {
        return urgencia;
    }

    public void setUrgencia(int urgencia) {
        this.urgencia = urgencia;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Orgao getOrgao() {
        return orgao;
    }

    public void setOrgao(Orgao orgao) {
        this.orgao = orgao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public List<Imagens> getImagens(){
        return imagens;
    }

    public Feedback getFeedback(){
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
        feedback.setDenuncia(this);
    }

    public void addImagem(Imagens imagem) {
        this.imagens.add(imagem);
        imagem.setDenuncia(this);
    }

}
