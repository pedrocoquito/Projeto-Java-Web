package controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "index")
@RequestScoped
public class IndexBean {

    public IndexBean() {

    }

    public String callEventosPage() {
        return "eventos?faces-redirect=true";
    }

    public String callLoginPage() {
        return "login?faces-redirect=true";
    }

    public String callCadastroPage() {
        return "cadastro?faces-redirect=true";
    }

    public String callArtigos() {
        return "artigos?faces-redirect=true";
    }

    public String callEventoAdminPage() {
        return "eventoAdmin?faces-redirect=true";
    }

}
