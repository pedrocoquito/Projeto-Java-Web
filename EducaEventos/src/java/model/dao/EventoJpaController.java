/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Artigo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Evento;
import model.Eventousuario;
import model.dao.exceptions.IllegalOrphanException;
import model.dao.exceptions.NonexistentEntityException;

/**
 *
 * @author coqui
 */
public class EventoJpaController implements Serializable {

    public EventoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Evento evento) {
        if (evento.getArtigoList() == null) {
            evento.setArtigoList(new ArrayList<Artigo>());
        }
        if (evento.getEventousuarioList() == null) {
            evento.setEventousuarioList(new ArrayList<Eventousuario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Artigo> attachedArtigoList = new ArrayList<Artigo>();
            for (Artigo artigoListArtigoToAttach : evento.getArtigoList()) {
                artigoListArtigoToAttach = em.getReference(artigoListArtigoToAttach.getClass(), artigoListArtigoToAttach.getIdartigo());
                attachedArtigoList.add(artigoListArtigoToAttach);
            }
            evento.setArtigoList(attachedArtigoList);
            List<Eventousuario> attachedEventousuarioList = new ArrayList<Eventousuario>();
            for (Eventousuario eventousuarioListEventousuarioToAttach : evento.getEventousuarioList()) {
                eventousuarioListEventousuarioToAttach = em.getReference(eventousuarioListEventousuarioToAttach.getClass(), eventousuarioListEventousuarioToAttach.getEventousuarioPK());
                attachedEventousuarioList.add(eventousuarioListEventousuarioToAttach);
            }
            evento.setEventousuarioList(attachedEventousuarioList);
            em.persist(evento);
            for (Artigo artigoListArtigo : evento.getArtigoList()) {
                Evento oldEventoIdeventoOfArtigoListArtigo = artigoListArtigo.getEventoIdevento();
                artigoListArtigo.setEventoIdevento(evento);
                artigoListArtigo = em.merge(artigoListArtigo);
                if (oldEventoIdeventoOfArtigoListArtigo != null) {
                    oldEventoIdeventoOfArtigoListArtigo.getArtigoList().remove(artigoListArtigo);
                    oldEventoIdeventoOfArtigoListArtigo = em.merge(oldEventoIdeventoOfArtigoListArtigo);
                }
            }
            for (Eventousuario eventousuarioListEventousuario : evento.getEventousuarioList()) {
                Evento oldEventoOfEventousuarioListEventousuario = eventousuarioListEventousuario.getEvento();
                eventousuarioListEventousuario.setEvento(evento);
                eventousuarioListEventousuario = em.merge(eventousuarioListEventousuario);
                if (oldEventoOfEventousuarioListEventousuario != null) {
                    oldEventoOfEventousuarioListEventousuario.getEventousuarioList().remove(eventousuarioListEventousuario);
                    oldEventoOfEventousuarioListEventousuario = em.merge(oldEventoOfEventousuarioListEventousuario);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Evento evento) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Evento persistentEvento = em.find(Evento.class, evento.getIdevento());
            List<Artigo> artigoListOld = persistentEvento.getArtigoList();
            List<Artigo> artigoListNew = evento.getArtigoList();
            List<Eventousuario> eventousuarioListOld = persistentEvento.getEventousuarioList();
            List<Eventousuario> eventousuarioListNew = evento.getEventousuarioList();
            List<String> illegalOrphanMessages = null;
            for (Artigo artigoListOldArtigo : artigoListOld) {
                if (!artigoListNew.contains(artigoListOldArtigo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Artigo " + artigoListOldArtigo + " since its eventoIdevento field is not nullable.");
                }
            }
            for (Eventousuario eventousuarioListOldEventousuario : eventousuarioListOld) {
                if (!eventousuarioListNew.contains(eventousuarioListOldEventousuario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Eventousuario " + eventousuarioListOldEventousuario + " since its evento field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Artigo> attachedArtigoListNew = new ArrayList<Artigo>();
            for (Artigo artigoListNewArtigoToAttach : artigoListNew) {
                artigoListNewArtigoToAttach = em.getReference(artigoListNewArtigoToAttach.getClass(), artigoListNewArtigoToAttach.getIdartigo());
                attachedArtigoListNew.add(artigoListNewArtigoToAttach);
            }
            artigoListNew = attachedArtigoListNew;
            evento.setArtigoList(artigoListNew);
            List<Eventousuario> attachedEventousuarioListNew = new ArrayList<Eventousuario>();
            for (Eventousuario eventousuarioListNewEventousuarioToAttach : eventousuarioListNew) {
                eventousuarioListNewEventousuarioToAttach = em.getReference(eventousuarioListNewEventousuarioToAttach.getClass(), eventousuarioListNewEventousuarioToAttach.getEventousuarioPK());
                attachedEventousuarioListNew.add(eventousuarioListNewEventousuarioToAttach);
            }
            eventousuarioListNew = attachedEventousuarioListNew;
            evento.setEventousuarioList(eventousuarioListNew);
            evento = em.merge(evento);
            for (Artigo artigoListNewArtigo : artigoListNew) {
                if (!artigoListOld.contains(artigoListNewArtigo)) {
                    Evento oldEventoIdeventoOfArtigoListNewArtigo = artigoListNewArtigo.getEventoIdevento();
                    artigoListNewArtigo.setEventoIdevento(evento);
                    artigoListNewArtigo = em.merge(artigoListNewArtigo);
                    if (oldEventoIdeventoOfArtigoListNewArtigo != null && !oldEventoIdeventoOfArtigoListNewArtigo.equals(evento)) {
                        oldEventoIdeventoOfArtigoListNewArtigo.getArtigoList().remove(artigoListNewArtigo);
                        oldEventoIdeventoOfArtigoListNewArtigo = em.merge(oldEventoIdeventoOfArtigoListNewArtigo);
                    }
                }
            }
            for (Eventousuario eventousuarioListNewEventousuario : eventousuarioListNew) {
                if (!eventousuarioListOld.contains(eventousuarioListNewEventousuario)) {
                    Evento oldEventoOfEventousuarioListNewEventousuario = eventousuarioListNewEventousuario.getEvento();
                    eventousuarioListNewEventousuario.setEvento(evento);
                    eventousuarioListNewEventousuario = em.merge(eventousuarioListNewEventousuario);
                    if (oldEventoOfEventousuarioListNewEventousuario != null && !oldEventoOfEventousuarioListNewEventousuario.equals(evento)) {
                        oldEventoOfEventousuarioListNewEventousuario.getEventousuarioList().remove(eventousuarioListNewEventousuario);
                        oldEventoOfEventousuarioListNewEventousuario = em.merge(oldEventoOfEventousuarioListNewEventousuario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = evento.getIdevento();
                if (findEvento(id) == null) {
                    throw new NonexistentEntityException("The evento with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Evento evento;
            try {
                evento = em.getReference(Evento.class, id);
                evento.getIdevento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The evento with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Artigo> artigoListOrphanCheck = evento.getArtigoList();
            for (Artigo artigoListOrphanCheckArtigo : artigoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Evento (" + evento + ") cannot be destroyed since the Artigo " + artigoListOrphanCheckArtigo + " in its artigoList field has a non-nullable eventoIdevento field.");
            }
            List<Eventousuario> eventousuarioListOrphanCheck = evento.getEventousuarioList();
            for (Eventousuario eventousuarioListOrphanCheckEventousuario : eventousuarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Evento (" + evento + ") cannot be destroyed since the Eventousuario " + eventousuarioListOrphanCheckEventousuario + " in its eventousuarioList field has a non-nullable evento field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(evento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Evento> findEventoEntities() {
        return findEventoEntities(true, -1, -1);
    }

    public List<Evento> findEventoEntities(int maxResults, int firstResult) {
        return findEventoEntities(false, maxResults, firstResult);
    }

    private List<Evento> findEventoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Evento.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Evento findEvento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Evento.class, id);
        } finally {
            em.close();
        }
    }

    public int getEventoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Evento> rt = cq.from(Evento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Evento> findEventoCoordenadorId(int coordenador) {
        EntityManager em = getEntityManager();
        Query q = em.createQuery("SELECT e FROM Evento e WHERE e.idevento in (SELECT eu.evento.idevento FROM Eventousuario eu WHERE eu.eventousuarioPK.usuarioIdusuario = :id and eu.tipo LIKE '%COORD%' )");
        q.setParameter("id", coordenador);

        List<Evento> eventos = q.getResultList();
        return eventos;
    }

    public List<Evento> findIdDisponiveis(Integer idusuario) {
        EntityManager em = getEntityManager();
        Query q = em.createQuery("SELECT e FROM Evento e WHERE e.idevento not in (SELECT eu.evento.idevento FROM Eventousuario eu WHERE eu.eventousuarioPK.usuarioIdusuario = :id)");
        q.setParameter("id", idusuario);

        List<Evento> eventos = q.getResultList();
        return eventos;
    }
}
