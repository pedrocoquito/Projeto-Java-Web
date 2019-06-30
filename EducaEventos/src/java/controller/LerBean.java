/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.util.Date;
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
import model.Notaartigo;
import model.NotaartigoPK;
import model.dao.ArtigoJpaController;
import model.dao.EventoJpaController;
import model.dao.NotaartigoJpaController;
import util.EStatus;

/**
 *
 * @author coqui
 */
@ManagedBean(name = "ler")
@ViewScoped
public class LerBean {

    private List<Artigo> artigos;
    private Artigo artigo;
    private int nota;
    private EntityManagerFactory emf;
    private String comentario;
    private EStatus status;
    @ManagedProperty(value = "#{login}")
    private LoginBean login;

    public String retorna() {
        return "artigos?faces-redirect=true";
    }

    public String detalhes(Artigo a) {
        status = EStatus.Insert;
        artigo = a;
        return "";
    }

    public String salvar() throws Exception {
        NotaartigoPK npk = new NotaartigoPK(login.getUsuarioLogado().getIdusuario(), artigo.getIdartigo());
        Notaartigo na = new Notaartigo(npk, nota, comentario);
        new NotaartigoJpaController(emf).create(na);
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Nota salva com sucesso!!!",
                "Salvo");
        FacesContext.getCurrentInstance().addMessage("Salvo com Sucesso", fm);
        status = EStatus.View;
        return "";
    }

    /**
     * Creates a new instance of LerBean
     */
    public LerBean() {
        status = EStatus.View;
        try {
            emf = Persistence.createEntityManagerFactory("EducaEventosPU");
        } catch (Exception e) {
        }

    }

    @PostConstruct
    public void init() {
        try {
            if (login.getUsuarioLogado() == null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
            } else {
                artigos = new ArtigoJpaController(emf).findLerArtigo(login.getUsuarioLogado().getIdusuario());
            }
        } catch (IOException e) {

        }
    }

    public List<Artigo> getArtigos() {
        return artigos;
    }

    public void setArtigos(List<Artigo> artigos) {
        this.artigos = artigos;
    }

    public Artigo getArtigo() {
        return artigo;
    }

    public void setArtigo(Artigo artigo) {
        this.artigo = artigo;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public EStatus getStatus() {
        return status;
    }

    public void setStatus(EStatus status) {
        this.status = status;
    }

    public LoginBean getLogin() {
        return login;
    }

    public void setLogin(LoginBean login) {
        this.login = login;
    }

}
