/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Evento;
import model.Eventousuario;
import model.EventousuarioPK;
import model.Usuario;
import model.dao.exceptions.NonexistentEntityException;
import model.dao.exceptions.PreexistingEntityException;

/**
 *
 * @author coqui
 */
public class EventousuarioJpaController implements Serializable {

    public EventousuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Eventousuario eventousuario) throws PreexistingEntityException, Exception {
        if (eventousuario.getEventousuarioPK() == null) {
            eventousuario.setEventousuarioPK(new EventousuarioPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Evento evento = eventousuario.getEvento();
            if (evento != null) {
                evento = em.getReference(evento.getClass(), evento.getIdevento());
                eventousuario.setEvento(evento);
            }
            Usuario usuario = eventousuario.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getIdusuario());
                eventousuario.setUsuario(usuario);
            }
            em.persist(eventousuario);
            if (evento != null) {
                evento.getEventousuarioList().add(eventousuario);
                evento = em.merge(evento);
            }
            if (usuario != null) {
                usuario.getEventousuarioList().add(eventousuario);
                usuario = em.merge(usuario);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEventousuario(eventousuario.getEventousuarioPK()) != null) {
                throw new PreexistingEntityException("Eventousuario " + eventousuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Eventousuario eventousuario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Eventousuario persistentEventousuario = em.find(Eventousuario.class, eventousuario.getEventousuarioPK());
            Evento eventoOld = persistentEventousuario.getEvento();
            Evento eventoNew = eventousuario.getEvento();
            Usuario usuarioOld = persistentEventousuario.getUsuario();
            Usuario usuarioNew = eventousuario.getUsuario();
            if (eventoNew != null) {
                eventoNew = em.getReference(eventoNew.getClass(), eventoNew.getIdevento());
                eventousuario.setEvento(eventoNew);
            }
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getIdusuario());
                eventousuario.setUsuario(usuarioNew);
            }
            eventousuario = em.merge(eventousuario);
            if (eventoOld != null && !eventoOld.equals(eventoNew)) {
                eventoOld.getEventousuarioList().remove(eventousuario);
                eventoOld = em.merge(eventoOld);
            }
            if (eventoNew != null && !eventoNew.equals(eventoOld)) {
                eventoNew.getEventousuarioList().add(eventousuario);
                eventoNew = em.merge(eventoNew);
            }
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.getEventousuarioList().remove(eventousuario);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.getEventousuarioList().add(eventousuario);
                usuarioNew = em.merge(usuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                EventousuarioPK id = eventousuario.getEventousuarioPK();
                if (findEventousuario(id) == null) {
                    throw new NonexistentEntityException("The eventousuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(EventousuarioPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Eventousuario eventousuario;
            try {
                eventousuario = em.getReference(Eventousuario.class, id);
                eventousuario.getEventousuarioPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The eventousuario with id " + id + " no longer exists.", enfe);
            }
            Evento evento = eventousuario.getEvento();
            if (evento != null) {
                evento.getEventousuarioList().remove(eventousuario);
                evento = em.merge(evento);
            }
            Usuario usuario = eventousuario.getUsuario();
            if (usuario != null) {
                usuario.getEventousuarioList().remove(eventousuario);
                usuario = em.merge(usuario);
            }
            em.remove(eventousuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Eventousuario> findEventousuarioEntities() {
        return findEventousuarioEntities(true, -1, -1);
    }

    public List<Eventousuario> findEventousuarioEntities(int maxResults, int firstResult) {
        return findEventousuarioEntities(false, maxResults, firstResult);
    }

    private List<Eventousuario> findEventousuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Eventousuario.class));
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

    public Eventousuario findEventousuario(EventousuarioPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Eventousuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getEventousuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Eventousuario> rt = cq.from(Eventousuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Eventousuario> findEventoAvaliadores(int idEvento) {
        EntityManager em = getEntityManager();
        Query q = em.createQuery("SELECT e FROM Eventousuario e WHERE e.eventousuarioPK.eventoIdevento = :id and e.tipo LIKE '%AVA%'");
        q.setParameter("id", idEvento);

        return q.getResultList();
    }

    public List<Eventousuario> findEventoCoordenador(int idUser) {
        EntityManager em = getEntityManager();
        Query q = em.createQuery("SELECT e FROM Eventousuario e WHERE e.eventousuarioPK.usuarioIdusuario = :id and e.tipo LIKE '%COORD%'");
        q.setParameter("id", idUser);

        return q.getResultList();
    }
}
