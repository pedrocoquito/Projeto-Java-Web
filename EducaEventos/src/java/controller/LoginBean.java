package controller;

import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import model.Eventousuario;
import model.Usuario;
import model.dao.EventousuarioJpaController;
import model.dao.UsuarioJpaController;

@ManagedBean(name = "login")
@SessionScoped
public class LoginBean {

    private String login, senha;
    private Usuario usuarioLogado;
    private EntityManagerFactory emf;
    private boolean coordenador;
    
    public String login(){
        try {
            usuarioLogado = new UsuarioJpaController(emf).findByLoginAndSenha(login, senha);
            if (usuarioLogado == null) {
                FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Login ou senha incorreta",
                        "Login");
                FacesContext.getCurrentInstance().addMessage("Login", fm);
                return "";
            }else{
                List<Eventousuario>listaEventos = new EventousuarioJpaController(emf).findEventoCoordenador(usuarioLogado.getIdusuario());
                if(listaEventos.isEmpty()){
                    this.coordenador = false;
                    return "index?faces-redirect=true";
                }else{
                    this.coordenador = true;
                    return "index?faces-redirect=true";
                }
                
            }
        } catch (Exception e) {
             FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erro ao Logar - Banco "+e.getMessage(),
                    "Salvo");
            FacesContext.getCurrentInstance().addMessage("ErroSalvar", fm);
            return "";
        }
    }
    
    public String logout(){
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index?faces-redirect=true";
    }
    
    public LoginBean() {
        try {
            emf = Persistence.createEntityManagerFactory("EducaEventosPU");
        } catch (Exception e) {
        }
        
    }

    public boolean isCoordenador() {
        return coordenador;
    }

    public void setCoordenador(boolean coordenador) {
        this.coordenador = coordenador;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }
    
}
