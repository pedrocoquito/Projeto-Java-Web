package controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import model.Artigo;
import model.Evento;
import model.Eventousuario;
import model.dao.ArtigoJpaController;
import model.dao.EventoJpaController;
import model.dao.EventousuarioJpaController;
import util.EStatus;

@ManagedBean(name = "eventos")
@RequestScoped
public class EventosBean {

    private List<Evento> eventos;
    private Evento evento;
    private EntityManagerFactory emf;
    private Date datas;
    private Date hoje;
    private List<Artigo> artigos;
    private EStatus status;
    private List<Eventousuario> avaliadores;
    @ManagedProperty(value = "#{login}")
    private LoginBean login;

    public String voltar() {
        status = EStatus.View;
        return "";
    }

    public String detalhesEventos(Evento e) {
        status = EStatus.Check;
        artigos = new ArtigoJpaController(emf).findByEvento(e.getIdevento());
        avaliadores = new EventousuarioJpaController(emf).findEventoAvaliadores(e.getIdevento());
        return "";
    }

    public EventosBean() {
        status = EStatus.View;
        try {
            emf = Persistence.createEntityManagerFactory("EducaEventosPU");
        } catch (Exception e) {
        }
        
        hoje = new Date();
    }

    @PostConstruct
    public void init() {
        try {
            if (login.getUsuarioLogado() == null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
            } else {
                eventos = new EventoJpaController(emf).findEventoCoordenadorId(login.getUsuarioLogado().getIdusuario());
            }
        } catch (IOException e) {

        }
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Date getDatas() {
        return datas;
    }

    public void setDatas(Date datas) {
        this.datas = datas;
    }

    public List<Artigo> getArtigos() {
        return artigos;
    }

    public void setArtigos(List<Artigo> artigos) {
        this.artigos = artigos;
    }

    public EStatus getStatus() {
        return status;
    }

    public void setStatus(EStatus status) {
        this.status = status;
    }

    public Date getHoje() {
        return hoje;
    }

    public void setHoje(Date hoje) {
        this.hoje = hoje;
    }

    public LoginBean getLogin() {
        return login;
    }

    public void setLogin(LoginBean login) {
        this.login = login;
    }

    public List<Eventousuario> getAvaliadores() {
        return avaliadores;
    }

    public void setAvaliadores(List<Eventousuario> avaliadores) {
        this.avaliadores = avaliadores;
    }

}
