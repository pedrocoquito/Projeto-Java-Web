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
import model.Artigo;
import model.Notaartigo;
import model.NotaartigoPK;
import model.Usuario;
import model.dao.exceptions.NonexistentEntityException;
import model.dao.exceptions.PreexistingEntityException;

/**
 *
 * @author coqui
 */
public class NotaartigoJpaController implements Serializable {

    public NotaartigoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Notaartigo notaartigo) throws PreexistingEntityException, Exception {
        if (notaartigo.getNotaartigoPK() == null) {
            notaartigo.setNotaartigoPK(new NotaartigoPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Artigo artigo = notaartigo.getArtigo();
            if (artigo != null) {
                artigo = em.getReference(artigo.getClass(), artigo.getIdartigo());
                notaartigo.setArtigo(artigo);
            }
            Usuario usuario = notaartigo.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getIdusuario());
                notaartigo.setUsuario(usuario);
            }
            em.persist(notaartigo);
            if (artigo != null) {
                artigo.getNotaartigoList().add(notaartigo);
                artigo = em.merge(artigo);
            }
            if (usuario != null) {
                usuario.getNotaartigoList().add(notaartigo);
                usuario = em.merge(usuario);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findNotaartigo(notaartigo.getNotaartigoPK()) != null) {
                throw new PreexistingEntityException("Notaartigo " + notaartigo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Notaartigo notaartigo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Notaartigo persistentNotaartigo = em.find(Notaartigo.class, notaartigo.getNotaartigoPK());
            Artigo artigoOld = persistentNotaartigo.getArtigo();
            Artigo artigoNew = notaartigo.getArtigo();
            Usuario usuarioOld = persistentNotaartigo.getUsuario();
            Usuario usuarioNew = notaartigo.getUsuario();
            if (artigoNew != null) {
                artigoNew = em.getReference(artigoNew.getClass(), artigoNew.getIdartigo());
                notaartigo.setArtigo(artigoNew);
            }
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getIdusuario());
                notaartigo.setUsuario(usuarioNew);
            }
            notaartigo = em.merge(notaartigo);
            if (artigoOld != null && !artigoOld.equals(artigoNew)) {
                artigoOld.getNotaartigoList().remove(notaartigo);
                artigoOld = em.merge(artigoOld);
            }
            if (artigoNew != null && !artigoNew.equals(artigoOld)) {
                artigoNew.getNotaartigoList().add(notaartigo);
                artigoNew = em.merge(artigoNew);
            }
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.getNotaartigoList().remove(notaartigo);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.getNotaartigoList().add(notaartigo);
                usuarioNew = em.merge(usuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                NotaartigoPK id = notaartigo.getNotaartigoPK();
                if (findNotaartigo(id) == null) {
                    throw new NonexistentEntityException("The notaartigo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(NotaartigoPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Notaartigo notaartigo;
            try {
                notaartigo = em.getReference(Notaartigo.class, id);
                notaartigo.getNotaartigoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The notaartigo with id " + id + " no longer exists.", enfe);
            }
            Artigo artigo = notaartigo.getArtigo();
            if (artigo != null) {
                artigo.getNotaartigoList().remove(notaartigo);
                artigo = em.merge(artigo);
            }
            Usuario usuario = notaartigo.getUsuario();
            if (usuario != null) {
                usuario.getNotaartigoList().remove(notaartigo);
                usuario = em.merge(usuario);
            }
            em.remove(notaartigo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Notaartigo> findNotaartigoEntities() {
        return findNotaartigoEntities(true, -1, -1);
    }

    public List<Notaartigo> findNotaartigoEntities(int maxResults, int firstResult) {
        return findNotaartigoEntities(false, maxResults, firstResult);
    }

    private List<Notaartigo> findNotaartigoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Notaartigo.class));
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

    public Notaartigo findNotaartigo(NotaartigoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Notaartigo.class, id);
        } finally {
            em.close();
        }
    }

    public int getNotaartigoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Notaartigo> rt = cq.from(Notaartigo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<Notaartigo> findNotasArtigo(int id) {
        EntityManager em = getEntityManager();
        Query q = em.createQuery("SELECT na from Notaartigo na where na.artigo.idartigo = :id");
        q.setParameter("id", id);

        return q.getResultList();
    }
}
