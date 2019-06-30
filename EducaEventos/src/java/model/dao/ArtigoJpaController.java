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
import model.Evento;
import model.Usuario;
import model.Notaartigo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Artigo;
import model.dao.exceptions.IllegalOrphanException;
import model.dao.exceptions.NonexistentEntityException;

/**
 *
 * @author coqui
 */
public class ArtigoJpaController implements Serializable {

    public ArtigoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Artigo artigo) {
        if (artigo.getNotaartigoList() == null) {
            artigo.setNotaartigoList(new ArrayList<Notaartigo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Evento eventoIdevento = artigo.getEventoIdevento();
            if (eventoIdevento != null) {
                eventoIdevento = em.getReference(eventoIdevento.getClass(), eventoIdevento.getIdevento());
                artigo.setEventoIdevento(eventoIdevento);
            }
            Usuario usuarioIdusuario = artigo.getUsuarioIdusuario();
            if (usuarioIdusuario != null) {
                usuarioIdusuario = em.getReference(usuarioIdusuario.getClass(), usuarioIdusuario.getIdusuario());
                artigo.setUsuarioIdusuario(usuarioIdusuario);
            }
            List<Notaartigo> attachedNotaartigoList = new ArrayList<Notaartigo>();
            for (Notaartigo notaartigoListNotaartigoToAttach : artigo.getNotaartigoList()) {
                notaartigoListNotaartigoToAttach = em.getReference(notaartigoListNotaartigoToAttach.getClass(), notaartigoListNotaartigoToAttach.getNotaartigoPK());
                attachedNotaartigoList.add(notaartigoListNotaartigoToAttach);
            }
            artigo.setNotaartigoList(attachedNotaartigoList);
            em.persist(artigo);
            if (eventoIdevento != null) {
                eventoIdevento.getArtigoList().add(artigo);
                eventoIdevento = em.merge(eventoIdevento);
            }
            if (usuarioIdusuario != null) {
                usuarioIdusuario.getArtigoList().add(artigo);
                usuarioIdusuario = em.merge(usuarioIdusuario);
            }
            for (Notaartigo notaartigoListNotaartigo : artigo.getNotaartigoList()) {
                Artigo oldArtigoOfNotaartigoListNotaartigo = notaartigoListNotaartigo.getArtigo();
                notaartigoListNotaartigo.setArtigo(artigo);
                notaartigoListNotaartigo = em.merge(notaartigoListNotaartigo);
                if (oldArtigoOfNotaartigoListNotaartigo != null) {
                    oldArtigoOfNotaartigoListNotaartigo.getNotaartigoList().remove(notaartigoListNotaartigo);
                    oldArtigoOfNotaartigoListNotaartigo = em.merge(oldArtigoOfNotaartigoListNotaartigo);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Artigo artigo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Artigo persistentArtigo = em.find(Artigo.class, artigo.getIdartigo());
            Evento eventoIdeventoOld = persistentArtigo.getEventoIdevento();
            Evento eventoIdeventoNew = artigo.getEventoIdevento();
            Usuario usuarioIdusuarioOld = persistentArtigo.getUsuarioIdusuario();
            Usuario usuarioIdusuarioNew = artigo.getUsuarioIdusuario();
            List<Notaartigo> notaartigoListOld = persistentArtigo.getNotaartigoList();
            List<Notaartigo> notaartigoListNew = artigo.getNotaartigoList();
            List<String> illegalOrphanMessages = null;
            for (Notaartigo notaartigoListOldNotaartigo : notaartigoListOld) {
                if (!notaartigoListNew.contains(notaartigoListOldNotaartigo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Notaartigo " + notaartigoListOldNotaartigo + " since its artigo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (eventoIdeventoNew != null) {
                eventoIdeventoNew = em.getReference(eventoIdeventoNew.getClass(), eventoIdeventoNew.getIdevento());
                artigo.setEventoIdevento(eventoIdeventoNew);
            }
            if (usuarioIdusuarioNew != null) {
                usuarioIdusuarioNew = em.getReference(usuarioIdusuarioNew.getClass(), usuarioIdusuarioNew.getIdusuario());
                artigo.setUsuarioIdusuario(usuarioIdusuarioNew);
            }
            List<Notaartigo> attachedNotaartigoListNew = new ArrayList<Notaartigo>();
            for (Notaartigo notaartigoListNewNotaartigoToAttach : notaartigoListNew) {
                notaartigoListNewNotaartigoToAttach = em.getReference(notaartigoListNewNotaartigoToAttach.getClass(), notaartigoListNewNotaartigoToAttach.getNotaartigoPK());
                attachedNotaartigoListNew.add(notaartigoListNewNotaartigoToAttach);
            }
            notaartigoListNew = attachedNotaartigoListNew;
            artigo.setNotaartigoList(notaartigoListNew);
            artigo = em.merge(artigo);
            if (eventoIdeventoOld != null && !eventoIdeventoOld.equals(eventoIdeventoNew)) {
                eventoIdeventoOld.getArtigoList().remove(artigo);
                eventoIdeventoOld = em.merge(eventoIdeventoOld);
            }
            if (eventoIdeventoNew != null && !eventoIdeventoNew.equals(eventoIdeventoOld)) {
                eventoIdeventoNew.getArtigoList().add(artigo);
                eventoIdeventoNew = em.merge(eventoIdeventoNew);
            }
            if (usuarioIdusuarioOld != null && !usuarioIdusuarioOld.equals(usuarioIdusuarioNew)) {
                usuarioIdusuarioOld.getArtigoList().remove(artigo);
                usuarioIdusuarioOld = em.merge(usuarioIdusuarioOld);
            }
            if (usuarioIdusuarioNew != null && !usuarioIdusuarioNew.equals(usuarioIdusuarioOld)) {
                usuarioIdusuarioNew.getArtigoList().add(artigo);
                usuarioIdusuarioNew = em.merge(usuarioIdusuarioNew);
            }
            for (Notaartigo notaartigoListNewNotaartigo : notaartigoListNew) {
                if (!notaartigoListOld.contains(notaartigoListNewNotaartigo)) {
                    Artigo oldArtigoOfNotaartigoListNewNotaartigo = notaartigoListNewNotaartigo.getArtigo();
                    notaartigoListNewNotaartigo.setArtigo(artigo);
                    notaartigoListNewNotaartigo = em.merge(notaartigoListNewNotaartigo);
                    if (oldArtigoOfNotaartigoListNewNotaartigo != null && !oldArtigoOfNotaartigoListNewNotaartigo.equals(artigo)) {
                        oldArtigoOfNotaartigoListNewNotaartigo.getNotaartigoList().remove(notaartigoListNewNotaartigo);
                        oldArtigoOfNotaartigoListNewNotaartigo = em.merge(oldArtigoOfNotaartigoListNewNotaartigo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = artigo.getIdartigo();
                if (findArtigo(id) == null) {
                    throw new NonexistentEntityException("The artigo with id " + id + " no longer exists.");
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
            Artigo artigo;
            try {
                artigo = em.getReference(Artigo.class, id);
                artigo.getIdartigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The artigo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Notaartigo> notaartigoListOrphanCheck = artigo.getNotaartigoList();
            for (Notaartigo notaartigoListOrphanCheckNotaartigo : notaartigoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Artigo (" + artigo + ") cannot be destroyed since the Notaartigo " + notaartigoListOrphanCheckNotaartigo + " in its notaartigoList field has a non-nullable artigo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Evento eventoIdevento = artigo.getEventoIdevento();
            if (eventoIdevento != null) {
                eventoIdevento.getArtigoList().remove(artigo);
                eventoIdevento = em.merge(eventoIdevento);
            }
            Usuario usuarioIdusuario = artigo.getUsuarioIdusuario();
            if (usuarioIdusuario != null) {
                usuarioIdusuario.getArtigoList().remove(artigo);
                usuarioIdusuario = em.merge(usuarioIdusuario);
            }
            em.remove(artigo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Artigo> findArtigoEntities() {
        return findArtigoEntities(true, -1, -1);
    }

    public List<Artigo> findArtigoEntities(int maxResults, int firstResult) {
        return findArtigoEntities(false, maxResults, firstResult);
    }

    private List<Artigo> findArtigoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Artigo.class));
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

    public Artigo findArtigo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Artigo.class, id);
        } finally {
            em.close();
        }
    }

    public int getArtigoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Artigo> rt = cq.from(Artigo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Artigo> findArtigoValidar(Integer idusuario) {
        EntityManager em = getEntityManager();
        Query q = em.createQuery("SELECT a from Artigo a where a.eventoIdevento.idevento in (SELECT eu.evento.idevento FROM Eventousuario eu WHERE eu.eventousuarioPK.usuarioIdusuario = :id) AND a.aprovado is null");
        q.setParameter("id", idusuario);

        return q.getResultList();
    }

    public List<Artigo> findByEvento(Integer id) {
        EntityManager em = getEntityManager();
        Query q = em.createQuery("SELECT a from Artigo a WHERE a.eventoIdevento.idevento = :id");
        q.setParameter("id", id);
        List<Artigo> l = q.getResultList();
        return l;
    }

    public List<Artigo> findArtigoUser(Integer idusuario) {
        EntityManager em = getEntityManager();
        Query q = em.createQuery("SELECT a from Artigo a where a.usuarioIdusuario.idusuario = :id");
        q.setParameter("id", idusuario);

        return q.getResultList();
    }
    
    public List<Artigo> findLerArtigo(int id) {
        EntityManager em = getEntityManager();
        Query q = em.createQuery("SELECT a from Artigo a where a.aprovado = 1 and a.eventoIdevento.idevento not in (SELECT a.eventoIdevento.idevento from Artigo a where a.usuarioIdusuario.idusuario = :id)");
        q.setParameter("id", id);

        return q.getResultList();
    }
}
