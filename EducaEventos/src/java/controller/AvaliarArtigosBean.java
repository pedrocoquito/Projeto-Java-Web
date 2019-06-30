/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import model.Artigo;

import model.dao.ArtigoJpaController;
import model.dao.exceptions.NonexistentEntityException;
import util.EStatus;

/**
 *
 * @author coqui
 */
@ManagedBean(name = "artigos")
@ViewScoped
public class AvaliarArtigosBean {

    private EntityManagerFactory emf;
    private List<Artigo> artigos;
    private Artigo artigo;
    private EStatus status;
    private String parecer;
    @ManagedProperty(value = "#{login}")
    private LoginBean login;

    public String retorna() {
        return "artigos?faces-redirect=true";
    }

    public String validar(Artigo a) {
        status = EStatus.Insert;
        artigo = a;
        return "";
    }

    public String aprovar() throws Exception {
        artigo.setAprovado(1);
        artigo.setParecer(parecer);
        new ArtigoJpaController(emf).edit(artigo);
        status = EStatus.View;
        artigos = new ArtigoJpaController(emf).findArtigoValidar(login.getUsuarioLogado().getIdusuario());
        return "";
    }

    public String reprovar() throws NonexistentEntityException, Exception {
        artigo.setAprovado(2);
        artigo.setParecer(parecer);
        new ArtigoJpaController(emf).edit(artigo);
        status = EStatus.View;
        artigos = new ArtigoJpaController(emf).findArtigoValidar(login.getUsuarioLogado().getIdusuario());
        return "";
    }

    public AvaliarArtigosBean() {
        try {
            emf = Persistence.createEntityManagerFactory("EducaEventosPU");
        } catch (Exception e) {
        }

        status = EStatus.View;
    }

    @PostConstruct
    public void init() {
        try {
            if (login.getUsuarioLogado() == null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
            } else {
                artigos = new ArtigoJpaController(emf).findArtigoValidar(login.getUsuarioLogado().getIdusuario());
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

    public Artigo getArtigo() {
        return artigo;
    }

    public void setArtigo(Artigo artigo) {
        this.artigo = artigo;
    }

    public String getParecer() {
        return parecer;
    }

    public void setParecer(String parecer) {
        this.parecer = parecer;
    }

}
