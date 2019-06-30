package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import model.Artigo;
import model.Evento;
import model.Notaartigo;
import model.dao.ArtigoJpaController;
import model.dao.EventoJpaController;
import model.dao.NotaartigoJpaController;
import util.EStatus;

@ManagedBean(name = "meusArtigos")
@ViewScoped
public class MeusArtigosBeans {

    private List<Artigo> artigos;
    private List<Notaartigo> notas;
    private EntityManagerFactory emf;
    private List<Evento> eventos;
    private String idEvento, titulo, texto;
    private EStatus status;
    private Artigo parecer;
    @ManagedProperty(value = "#{login}")
    private LoginBean loginMB;

    public MeusArtigosBeans() {
        emf = Persistence.createEntityManagerFactory("EducaEventosPU");
        eventos = new ArrayList<Evento>();
        status = EStatus.View;
    }

    public String retorna() {
        return "artigos?faces-redirect=true";
    }
    
    public String detalhes(int id) {
        status = EStatus.Check;
        notas = new NotaartigoJpaController(emf).findNotasArtigo(id);
        parecer = new ArtigoJpaController(emf).findArtigo(id);
        return "";
    }

    @PostConstruct
    public void init() {
        try {
            if (loginMB.getUsuarioLogado() == null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
            } else {
                artigos = new ArtigoJpaController(emf).findArtigoUser(loginMB.getUsuarioLogado().getIdusuario());
            }
        } catch (IOException e) {

        }
    }

    public EStatus getStatus() {
        return status;
    }

    public void setStatus(EStatus status) {
        this.status = status;
    }

    public List<Artigo> getArtigos() {
        return artigos;
    }

    public void setArtigos(List<Artigo> artigos) {
        this.artigos = artigos;
    }

    public List<Notaartigo> getNotas() {
        return notas;
    }

    public void setNotas(List<Notaartigo> notas) {
        this.notas = notas;
    }

    public LoginBean getLoginMB() {
        return loginMB;
    }

    public void setLoginMB(LoginBean loginMB) {
        this.loginMB = loginMB;
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }

    public String getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(String idEvento) {
        this.idEvento = idEvento;
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

    public Artigo getParecer() {
        return parecer;
    }

    public void setParecer(Artigo parecer) {
        this.parecer = parecer;
    }

}
