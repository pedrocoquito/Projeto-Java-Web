package controller;

import java.io.IOException;
import java.util.ArrayList;
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
import model.Evento;
import model.Eventousuario;
import model.EventousuarioPK;
import model.Usuario;
import model.dao.EventoJpaController;
import model.dao.EventousuarioJpaController;
import model.dao.UsuarioJpaController;
import org.primefaces.event.SelectEvent;
import util.EStatus;

@ManagedBean(name = "cadEvento")
@ViewScoped
public class CadEventoBean {

    private EStatus status;
    private List<Usuario> usuarios;
    private List<Evento> eventos;
    private Evento evento;
    private EntityManagerFactory emf;
    private Date datas;
    private int coordenador;
    private List<String> avaliadores;
    @ManagedProperty(value = "#{login}")
    private LoginBean login;

    public String salvarEvento() throws Exception {
        new EventoJpaController(emf).create(evento);
        cadastrarUsuarioEvento(coordenador, avaliadores, evento.getIdevento());
        status = EStatus.View;
        eventos = new EventoJpaController(emf).findEventoEntities();
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO,
                evento.getNome() + " salvo com sucesso!!!",
                "Salvo");
        FacesContext.getCurrentInstance().addMessage("Salvo com Sucesso", fm);
        status = EStatus.View;
        return "";
    }

    private void cadastrarUsuarioEvento(int coordenador, List<String> avaliadores, Integer idevento) throws Exception {
        Evento ev = new EventoJpaController(emf).findEvento(idevento);
        for(String a : avaliadores){
            int i = Integer.parseInt(a);
            Usuario u = new UsuarioJpaController(emf).findUsuario(i);
            EventousuarioPK uPK = new EventousuarioPK(i, idevento);
            Eventousuario uHE = new Eventousuario(uPK, "AVALIADOR");
            uHE.setEvento(ev);
            uHE.setUsuario(u);
            new EventousuarioJpaController(emf).create(uHE);
        }
        EventousuarioPK uPKCoord = new EventousuarioPK(coordenador, idevento);
        Eventousuario uHECoord = new Eventousuario(uPKCoord ,"COORDENADOR");
        Usuario u = new UsuarioJpaController(emf).findUsuario(coordenador);
        uHECoord.setEvento(ev);
        uHECoord.setUsuario(u);
        new EventousuarioJpaController(emf).create(uHECoord);
    }

    public String criarEvento() {
        status = EStatus.Insert;
        evento = new Evento();
        avaliadores = new ArrayList<String>();
        return "";
    }
    
    public Date hoje() {
        return new Date();
    }
    
    public void onDateSelect(SelectEvent e) {
        evento.setDataFinal(evento.getDataInicial());
    }

    public String voltar() {
        status = EStatus.View;
        return "";
    }

    @PostConstruct
    public void init() {
        try {
            if (login.getUsuarioLogado() == null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
            } else if (!"admin".equals(login.getUsuarioLogado().getLogin())) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
            }
        } catch (IOException e) {

        }
    }

    public CadEventoBean() {
        
        try {
            emf = Persistence.createEntityManagerFactory("EducaEventosPU");
        } catch (Exception e) {
        }
        
        usuarios = new UsuarioJpaController(emf).findUsuarioEntities();
        eventos = new EventoJpaController(emf).findEventoEntities();
        status = EStatus.View;
    }

    public EStatus getStatus() {
        return status;
    }

    public void setStatus(EStatus status) {
        this.status = status;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
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

    public LoginBean getLogin() {
        return login;
    }

    public void setLogin(LoginBean login) {
        this.login = login;
    }

    public Date getDatas() {
        return datas;
    }

    public void setDatas(Date datas) {
        this.datas = datas;
    }

    public int getCoordenador() {
        return coordenador;
    }

    public void setCoordenador(int coordenador) {
        this.coordenador = coordenador;
    }

    public List<String> getAvaliadores() {
        return avaliadores;
    }

    public void setAvaliadores(List<String> avaliadores) {
        this.avaliadores = avaliadores;
    }

}
