/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author coqui
 */
@Entity
@Table(catalog = "educaeventosdb", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Eventousuario.findAll", query = "SELECT e FROM Eventousuario e"),
    @NamedQuery(name = "Eventousuario.findByUsuarioIdusuario", query = "SELECT e FROM Eventousuario e WHERE e.eventousuarioPK.usuarioIdusuario = :usuarioIdusuario"),
    @NamedQuery(name = "Eventousuario.findByEventoIdevento", query = "SELECT e FROM Eventousuario e WHERE e.eventousuarioPK.eventoIdevento = :eventoIdevento"),
    @NamedQuery(name = "Eventousuario.findByTipo", query = "SELECT e FROM Eventousuario e WHERE e.tipo = :tipo")})
public class Eventousuario implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EventousuarioPK eventousuarioPK;
    @Basic(optional = false)
    @Column(nullable = false, length = 11)
    private String tipo;
    @JoinColumn(name = "evento_idevento", referencedColumnName = "idevento", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Evento evento;
    @JoinColumn(name = "usuario_idusuario", referencedColumnName = "idusuario", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Usuario usuario;

    public Eventousuario() {
    }

    public Eventousuario(EventousuarioPK eventousuarioPK) {
        this.eventousuarioPK = eventousuarioPK;
    }

    public Eventousuario(EventousuarioPK eventousuarioPK, String tipo) {
        this.eventousuarioPK = eventousuarioPK;
        this.tipo = tipo;
    }

    public Eventousuario(int usuarioIdusuario, int eventoIdevento) {
        this.eventousuarioPK = new EventousuarioPK(usuarioIdusuario, eventoIdevento);
    }

    public EventousuarioPK getEventousuarioPK() {
        return eventousuarioPK;
    }

    public void setEventousuarioPK(EventousuarioPK eventousuarioPK) {
        this.eventousuarioPK = eventousuarioPK;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eventousuarioPK != null ? eventousuarioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Eventousuario)) {
            return false;
        }
        Eventousuario other = (Eventousuario) object;
        if ((this.eventousuarioPK == null && other.eventousuarioPK != null) || (this.eventousuarioPK != null && !this.eventousuarioPK.equals(other.eventousuarioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Eventousuario[ eventousuarioPK=" + eventousuarioPK + " ]";
    }
    
}
