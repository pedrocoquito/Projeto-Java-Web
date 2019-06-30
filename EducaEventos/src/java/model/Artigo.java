/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author coqui
 */
@Entity
@Table(catalog = "educaeventosdb", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"idartigo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Artigo.findAll", query = "SELECT a FROM Artigo a"),
    @NamedQuery(name = "Artigo.findByIdartigo", query = "SELECT a FROM Artigo a WHERE a.idartigo = :idartigo"),
    @NamedQuery(name = "Artigo.findByTitulo", query = "SELECT a FROM Artigo a WHERE a.titulo = :titulo"),
    @NamedQuery(name = "Artigo.findByTexto", query = "SELECT a FROM Artigo a WHERE a.texto = :texto"),
    @NamedQuery(name = "Artigo.findByAprovado", query = "SELECT a FROM Artigo a WHERE a.aprovado = :aprovado"),
    @NamedQuery(name = "Artigo.findByParecer", query = "SELECT a FROM Artigo a WHERE a.parecer = :parecer")})
public class Artigo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idartigo;
    @Basic(optional = false)
    @Column(nullable = false, length = 45)
    private String titulo;
    @Basic(optional = false)
    @Column(nullable = false, length = 240)
    private String texto;
    private Integer aprovado;
    @Column(length = 45)
    private String parecer;
    @JoinColumn(name = "evento_idevento", referencedColumnName = "idevento", nullable = false)
    @ManyToOne(optional = false)
    private Evento eventoIdevento;
    @JoinColumn(name = "usuario_idusuario", referencedColumnName = "idusuario", nullable = false)
    @ManyToOne(optional = false)
    private Usuario usuarioIdusuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "artigo")
    private List<Notaartigo> notaartigoList;
   
    public void media() {
        double media = 0;
        for(Notaartigo na : notaartigoList){
            media += na.getNota();
        }
        media = media/notaartigoList.size();
    }
    
    public Artigo() {
    }

    public Artigo(Integer idartigo) {
        this.idartigo = idartigo;
    }

    public Artigo(Integer idartigo, String titulo, String texto) {
        this.idartigo = idartigo;
        this.titulo = titulo;
        this.texto = texto;
    }

    public Integer getIdartigo() {
        return idartigo;
    }

    public void setIdartigo(Integer idartigo) {
        this.idartigo = idartigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Integer getAprovado() {
        return aprovado;
    }

    public void setAprovado(Integer aprovado) {
        this.aprovado = aprovado;
    }

    public String getParecer() {
        return parecer;
    }

    public void setParecer(String parecer) {
        this.parecer = parecer;
    }

    public Evento getEventoIdevento() {
        return eventoIdevento;
    }

    public void setEventoIdevento(Evento eventoIdevento) {
        this.eventoIdevento = eventoIdevento;
    }

    public Usuario getUsuarioIdusuario() {
        return usuarioIdusuario;
    }

    public void setUsuarioIdusuario(Usuario usuarioIdusuario) {
        this.usuarioIdusuario = usuarioIdusuario;
    }

    @XmlTransient
    public List<Notaartigo> getNotaartigoList() {
        return notaartigoList;
    }

    public void setNotaartigoList(List<Notaartigo> notaartigoList) {
        this.notaartigoList = notaartigoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idartigo != null ? idartigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Artigo)) {
            return false;
        }
        Artigo other = (Artigo) object;
        if ((this.idartigo == null && other.idartigo != null) || (this.idartigo != null && !this.idartigo.equals(other.idartigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Artigo[ idartigo=" + idartigo + " ]";
    }
    
}
