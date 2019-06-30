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
    @NamedQuery(name = "Notaartigo.findAll", query = "SELECT n FROM Notaartigo n"),
    @NamedQuery(name = "Notaartigo.findByNota", query = "SELECT n FROM Notaartigo n WHERE n.nota = :nota"),
    @NamedQuery(name = "Notaartigo.findByUsuarioIdusuario", query = "SELECT n FROM Notaartigo n WHERE n.notaartigoPK.usuarioIdusuario = :usuarioIdusuario"),
    @NamedQuery(name = "Notaartigo.findByArtigoIdartigo", query = "SELECT n FROM Notaartigo n WHERE n.notaartigoPK.artigoIdartigo = :artigoIdartigo"),
    @NamedQuery(name = "Notaartigo.findByComentario", query = "SELECT n FROM Notaartigo n WHERE n.comentario = :comentario")})
public class Notaartigo implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected NotaartigoPK notaartigoPK;
    @Basic(optional = false)
    @Column(nullable = false)
    private int nota;
    @Basic(optional = false)
    @Column(nullable = false, length = 45)
    private String comentario;
    @JoinColumn(name = "artigo_idartigo", referencedColumnName = "idartigo", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Artigo artigo;
    @JoinColumn(name = "usuario_idusuario", referencedColumnName = "idusuario", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Usuario usuario;

    public Notaartigo() {
    }

    public Notaartigo(NotaartigoPK notaartigoPK) {
        this.notaartigoPK = notaartigoPK;
    }

    public Notaartigo(NotaartigoPK notaartigoPK, int nota, String comentario) {
        this.notaartigoPK = notaartigoPK;
        this.nota = nota;
        this.comentario = comentario;
    }

    public Notaartigo(int usuarioIdusuario, int artigoIdartigo) {
        this.notaartigoPK = new NotaartigoPK(usuarioIdusuario, artigoIdartigo);
    }

    public NotaartigoPK getNotaartigoPK() {
        return notaartigoPK;
    }

    public void setNotaartigoPK(NotaartigoPK notaartigoPK) {
        this.notaartigoPK = notaartigoPK;
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

    public Artigo getArtigo() {
        return artigo;
    }

    public void setArtigo(Artigo artigo) {
        this.artigo = artigo;
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
        hash += (notaartigoPK != null ? notaartigoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Notaartigo)) {
            return false;
        }
        Notaartigo other = (Notaartigo) object;
        if ((this.notaartigoPK == null && other.notaartigoPK != null) || (this.notaartigoPK != null && !this.notaartigoPK.equals(other.notaartigoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Notaartigo[ notaartigoPK=" + notaartigoPK + " ]";
    }
    
}
