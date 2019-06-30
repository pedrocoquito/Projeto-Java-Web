/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author coqui
 */
@Embeddable
public class EventousuarioPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "usuario_idusuario", nullable = false)
    private int usuarioIdusuario;
    @Basic(optional = false)
    @Column(name = "evento_idevento", nullable = false)
    private int eventoIdevento;

    public EventousuarioPK() {
    }

    public EventousuarioPK(int usuarioIdusuario, int eventoIdevento) {
        this.usuarioIdusuario = usuarioIdusuario;
        this.eventoIdevento = eventoIdevento;
    }

    public int getUsuarioIdusuario() {
        return usuarioIdusuario;
    }

    public void setUsuarioIdusuario(int usuarioIdusuario) {
        this.usuarioIdusuario = usuarioIdusuario;
    }

    public int getEventoIdevento() {
        return eventoIdevento;
    }

    public void setEventoIdevento(int eventoIdevento) {
        this.eventoIdevento = eventoIdevento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) usuarioIdusuario;
        hash += (int) eventoIdevento;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EventousuarioPK)) {
            return false;
        }
        EventousuarioPK other = (EventousuarioPK) object;
        if (this.usuarioIdusuario != other.usuarioIdusuario) {
            return false;
        }
        if (this.eventoIdevento != other.eventoIdevento) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.EventousuarioPK[ usuarioIdusuario=" + usuarioIdusuario + ", eventoIdevento=" + eventoIdevento + " ]";
    }
    
}
