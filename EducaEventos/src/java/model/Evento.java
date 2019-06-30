/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author coqui
 */
@Entity
@Table(catalog = "educaeventosdb", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"idevento"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Evento.findAll", query = "SELECT e FROM Evento e"),
    @NamedQuery(name = "Evento.findByIdevento", query = "SELECT e FROM Evento e WHERE e.idevento = :idevento"),
    @NamedQuery(name = "Evento.findByDataFinal", query = "SELECT e FROM Evento e WHERE e.dataFinal = :dataFinal"),
    @NamedQuery(name = "Evento.findByNome", query = "SELECT e FROM Evento e WHERE e.nome = :nome"),
    @NamedQuery(name = "Evento.findByInicioEvento", query = "SELECT e FROM Evento e WHERE e.inicioEvento = :inicioEvento"),
    @NamedQuery(name = "Evento.findByFimEvento", query = "SELECT e FROM Evento e WHERE e.fimEvento = :fimEvento"),
    @NamedQuery(name = "Evento.findByDataInicial", query = "SELECT e FROM Evento e WHERE e.dataInicial = :dataInicial")})
public class Evento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idevento;
    @Basic(optional = false)
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFinal;
    @Basic(optional = false)
    @Column(nullable = false, length = 45)
    private String nome;
    @Basic(optional = false)
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date inicioEvento;
    @Basic(optional = false)
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fimEvento;
    @Basic(optional = false)
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInicial;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eventoIdevento")
    private List<Artigo> artigoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evento")
    private List<Eventousuario> eventousuarioList;

    public int status() {
        /*
         0 - nada
         1 - em submissão
         2 - encerrada submissão
         3 - evento ocorrendo
         4 - evento encerrado
         */

        Date dt = new Date();
        if (dt.before(dataInicial)) {
            return 0;
        } else if (dt.after(dataInicial) && dt.before(dataFinal)) {
            return 1;
        } else if (dt.after(dataFinal) && dt.before(inicioEvento)) {
            return 2;
        } else if (dt.after(inicioEvento) && dt.before(fimEvento)) {
            return 3;
        } else {
            return 4;
        }
    }

    public Evento() {
    }

    public Evento(Integer idevento) {
        this.idevento = idevento;
    }

    public Evento(Integer idevento, Date dataFinal, String nome, Date inicioEvento, Date fimEvento, Date dataInicial) {
        this.idevento = idevento;
        this.dataFinal = dataFinal;
        this.nome = nome;
        this.inicioEvento = inicioEvento;
        this.fimEvento = fimEvento;
        this.dataInicial = dataInicial;
    }

    public Integer getIdevento() {
        return idevento;
    }

    public void setIdevento(Integer idevento) {
        this.idevento = idevento;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getInicioEvento() {
        return inicioEvento;
    }

    public void setInicioEvento(Date inicioEvento) {
        this.inicioEvento = inicioEvento;
    }

    public Date getFimEvento() {
        return fimEvento;
    }

    public void setFimEvento(Date fimEvento) {
        this.fimEvento = fimEvento;
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    @XmlTransient
    public List<Artigo> getArtigoList() {
        return artigoList;
    }

    public void setArtigoList(List<Artigo> artigoList) {
        this.artigoList = artigoList;
    }

    @XmlTransient
    public List<Eventousuario> getEventousuarioList() {
        return eventousuarioList;
    }

    public void setEventousuarioList(List<Eventousuario> eventousuarioList) {
        this.eventousuarioList = eventousuarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idevento != null ? idevento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Evento)) {
            return false;
        }
        Evento other = (Evento) object;
        if ((this.idevento == null && other.idevento != null) || (this.idevento != null && !this.idevento.equals(other.idevento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Evento[ idevento=" + idevento + " ]";
    }

}
