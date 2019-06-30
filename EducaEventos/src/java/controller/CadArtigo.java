/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import model.Artigo;
import model.Evento;
import model.dao.ArtigoJpaController;
import model.dao.EventoJpaController;
import util.EStatus;

/**
 *
 * @author coqui
 */
@ManagedBean
@RequestScoped
public class CadArtigo {

    private EntityManagerFactory emf;
    private List<Evento> eventos;
    private String idEvento, titulo, texto;
    @ManagedProperty(value = "#{login}")
    private LoginBean loginMB;

    private void preencheEventosDisponiveis() {
        List<Evento> lista = new EventoJpaController(emf).findIdDisponiveis(loginMB.getUsuarioLogado().getIdusuario());
        for (Evento e : lista) {
            if (e.status() == 1) {
                eventos.add(e);
            }
        }
    }

    public String retorna() {
        return "artigos?faces-redirect=true";
    }

    public String cadastra() {
        Artigo a = new Artigo();
        a.setTexto(texto);
        a.setTitulo(titulo);
        a.setUsuarioIdusuario(loginMB.getUsuarioLogado());
        Evento ev = new EventoJpaController(emf).findEvento(Integer.parseInt(idEvento));
        a.setEventoIdevento(ev);

        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Artigo salvo com sucesso!!!",
                "Salvo");
        FacesContext.getCurrentInstance().addMessage("Salvo com Sucesso", fm);
        new ArtigoJpaController(emf).create(a);
        return "";
    }

    /**
     * Creates a new instance of CadArtigo
     */
    public CadArtigo() {
        emf = Persistence.createEntityManagerFactory("EducaEventosPU");
        eventos = new ArrayList<Evento>();
    }

    @PostConstruct
    public void init() {
        try {
            if (loginMB.getUsuarioLogado() == null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
            } else {
                preencheEventosDisponiveis();
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

    public LoginBean getLoginMB() {
        return loginMB;
    }

    public void setLoginMB(LoginBean loginMB) {
        this.loginMB = loginMB;
    }

}
