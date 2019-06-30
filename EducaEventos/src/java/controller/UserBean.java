package controller;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import model.Usuario;
import model.dao.UsuarioJpaController;

@ManagedBean(name = "user")
@RequestScoped
public class UserBean {

    private Usuario usuario; 
    private EntityManagerFactory emf;
    
    public String salvarUsuario() {
        try {
            new UsuarioJpaController(emf).create(usuario);
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    usuario.getNome() + " salvo com sucesso!!!",
                    "Salvo");
            FacesContext.getCurrentInstance().addMessage("Salvo com Sucesso", fm);
            usuario = new Usuario();
        } catch (Exception e) {
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erro ao Salvar",
                    "Salvo");
            FacesContext.getCurrentInstance().addMessage("ErroSalvar", fm);
        }
        return "";
    }

    public UserBean() {
        usuario = new Usuario();
        try {
            emf = Persistence.createEntityManagerFactory("EducaEventosPU");
        } catch (Exception e) {
        }
        
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
}
