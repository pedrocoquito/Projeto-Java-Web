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
public class NotaartigoPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "usuario_idusuario", nullable = false)
    private int usuarioIdusuario;
    @Basic(optional = false)
    @Column(name = "artigo_idartigo", nullable = false)
    private int artigoIdartigo;

    public NotaartigoPK() {
    }

    public NotaartigoPK(int usuarioIdusuario, int artigoIdartigo) {
        this.usuarioIdusuario = usuarioIdusuario;
        this.artigoIdartigo = artigoIdartigo;
    }

    public int getUsuarioIdusuario() {
        return usuarioIdusuario;
    }

    public void setUsuarioIdusuario(int usuarioIdusuario) {
        this.usuarioIdusuario = usuarioIdusuario;
    }

    public int getArtigoIdartigo() {
        return artigoIdartigo;
    }

    public void setArtigoIdartigo(int artigoIdartigo) {
        this.artigoIdartigo = artigoIdartigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) usuarioIdusuario;
        hash += (int) artigoIdartigo;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotaartigoPK)) {
            return false;
        }
        NotaartigoPK other = (NotaartigoPK) object;
        if (this.usuarioIdusuario != other.usuarioIdusuario) {
            return false;
        }
        if (this.artigoIdartigo != other.artigoIdartigo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.NotaartigoPK[ usuarioIdusuario=" + usuarioIdusuario + ", artigoIdartigo=" + artigoIdartigo + " ]";
    }
    
}
