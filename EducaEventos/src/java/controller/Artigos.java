/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import model.Artigo;
import model.Evento;
import model.dao.EventoJpaController;
import util.EStatus;

/**
 *
 * @author coqui
 */
@ManagedBean(name = "pgArtigos")
@ViewScoped
public class Artigos {

    @ManagedProperty(value = "#{login}")
    private LoginBean loginMB;

    public String callCadArtigo() {
        return "cadArtigo?faces-redirect=true";
    }

    public String callLerArtigos() {
        return "lerArtigos?faces-redirect=true";
    }

    public String callMeusArtigos() {
        return "meusArtigos?faces-redirect=true";
    }

    public String callAvaliarArtigos() {
        return "avaliarArtigos?faces-redirect=true";
    }

    /**
     * Creates a new instance of Artigos
     */
    public Artigos() {
    }

    @PostConstruct
    public void init() {
        try {
            if (loginMB.getUsuarioLogado() == null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
            }
        } catch (IOException e) {

        }
    }

    public LoginBean getLoginMB() {
        return loginMB;
    }

    public void setLoginMB(LoginBean loginMB) {
        this.loginMB = loginMB;
    }

}
