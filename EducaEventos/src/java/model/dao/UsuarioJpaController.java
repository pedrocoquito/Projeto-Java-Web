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
import javax.persistence.NoResultException;
import model.Notaartigo;
import model.Eventousuario;
import model.Usuario;
import model.dao.exceptions.IllegalOrphanException;
import model.dao.exceptions.NonexistentEntityException;

/**
 *
 * @author coqui
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) {
        if (usuario.getArtigoList() == null) {
            usuario.setArtigoList(new ArrayList<Artigo>());
        }
        if (usuario.getNotaartigoList() == null) {
            usuario.setNotaartigoList(new ArrayList<Notaartigo>());
        }
        if (usuario.getEventousuarioList() == null) {
            usuario.setEventousuarioList(new ArrayList<Eventousuario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Artigo> attachedArtigoList = new ArrayList<Artigo>();
            for (Artigo artigoListArtigoToAttach : usuario.getArtigoList()) {
                artigoListArtigoToAttach = em.getReference(artigoListArtigoToAttach.getClass(), artigoListArtigoToAttach.getIdartigo());
                attachedArtigoList.add(artigoListArtigoToAttach);
            }
            usuario.setArtigoList(attachedArtigoList);
            List<Notaartigo> attachedNotaartigoList = new ArrayList<Notaartigo>();
            for (Notaartigo notaartigoListNotaartigoToAttach : usuario.getNotaartigoList()) {
                notaartigoListNotaartigoToAttach = em.getReference(notaartigoListNotaartigoToAttach.getClass(), notaartigoListNotaartigoToAttach.getNotaartigoPK());
                attachedNotaartigoList.add(notaartigoListNotaartigoToAttach);
            }
            usuario.setNotaartigoList(attachedNotaartigoList);
            List<Eventousuario> attachedEventousuarioList = new ArrayList<Eventousuario>();
            for (Eventousuario eventousuarioListEventousuarioToAttach : usuario.getEventousuarioList()) {
                eventousuarioListEventousuarioToAttach = em.getReference(eventousuarioListEventousuarioToAttach.getClass(), eventousuarioListEventousuarioToAttach.getEventousuarioPK());
                attachedEventousuarioList.add(eventousuarioListEventousuarioToAttach);
            }
            usuario.setEventousuarioList(attachedEventousuarioList);
            em.persist(usuario);
            for (Artigo artigoListArtigo : usuario.getArtigoList()) {
                Usuario oldUsuarioIdusuarioOfArtigoListArtigo = artigoListArtigo.getUsuarioIdusuario();
                artigoListArtigo.setUsuarioIdusuario(usuario);
                artigoListArtigo = em.merge(artigoListArtigo);
                if (oldUsuarioIdusuarioOfArtigoListArtigo != null) {
                    oldUsuarioIdusuarioOfArtigoListArtigo.getArtigoList().remove(artigoListArtigo);
                    oldUsuarioIdusuarioOfArtigoListArtigo = em.merge(oldUsuarioIdusuarioOfArtigoListArtigo);
                }
            }
            for (Notaartigo notaartigoListNotaartigo : usuario.getNotaartigoList()) {
                Usuario oldUsuarioOfNotaartigoListNotaartigo = notaartigoListNotaartigo.getUsuario();
                notaartigoListNotaartigo.setUsuario(usuario);
                notaartigoListNotaartigo = em.merge(notaartigoListNotaartigo);
                if (oldUsuarioOfNotaartigoListNotaartigo != null) {
                    oldUsuarioOfNotaartigoListNotaartigo.getNotaartigoList().remove(notaartigoListNotaartigo);
                    oldUsuarioOfNotaartigoListNotaartigo = em.merge(oldUsuarioOfNotaartigoListNotaartigo);
                }
            }
            for (Eventousuario eventousuarioListEventousuario : usuario.getEventousuarioList()) {
                Usuario oldUsuarioOfEventousuarioListEventousuario = eventousuarioListEventousuario.getUsuario();
                eventousuarioListEventousuario.setUsuario(usuario);
                eventousuarioListEventousuario = em.merge(eventousuarioListEventousuario);
                if (oldUsuarioOfEventousuarioListEventousuario != null) {
                    oldUsuarioOfEventousuarioListEventousuario.getEventousuarioList().remove(eventousuarioListEventousuario);
                    oldUsuarioOfEventousuarioListEventousuario = em.merge(oldUsuarioOfEventousuarioListEventousuario);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getIdusuario());
            List<Artigo> artigoListOld = persistentUsuario.getArtigoList();
            List<Artigo> artigoListNew = usuario.getArtigoList();
            List<Notaartigo> notaartigoListOld = persistentUsuario.getNotaartigoList();
            List<Notaartigo> notaartigoListNew = usuario.getNotaartigoList();
            List<Eventousuario> eventousuarioListOld = persistentUsuario.getEventousuarioList();
            List<Eventousuario> eventousuarioListNew = usuario.getEventousuarioList();
            List<String> illegalOrphanMessages = null;
            for (Artigo artigoListOldArtigo : artigoListOld) {
                if (!artigoListNew.contains(artigoListOldArtigo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Artigo " + artigoListOldArtigo + " since its usuarioIdusuario field is not nullable.");
                }
            }
            for (Notaartigo notaartigoListOldNotaartigo : notaartigoListOld) {
                if (!notaartigoListNew.contains(notaartigoListOldNotaartigo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Notaartigo " + notaartigoListOldNotaartigo + " since its usuario field is not nullable.");
                }
            }
            for (Eventousuario eventousuarioListOldEventousuario : eventousuarioListOld) {
                if (!eventousuarioListNew.contains(eventousuarioListOldEventousuario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Eventousuario " + eventousuarioListOldEventousuario + " since its usuario field is not nullable.");
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
            usuario.setArtigoList(artigoListNew);
            List<Notaartigo> attachedNotaartigoListNew = new ArrayList<Notaartigo>();
            for (Notaartigo notaartigoListNewNotaartigoToAttach : notaartigoListNew) {
                notaartigoListNewNotaartigoToAttach = em.getReference(notaartigoListNewNotaartigoToAttach.getClass(), notaartigoListNewNotaartigoToAttach.getNotaartigoPK());
                attachedNotaartigoListNew.add(notaartigoListNewNotaartigoToAttach);
            }
            notaartigoListNew = attachedNotaartigoListNew;
            usuario.setNotaartigoList(notaartigoListNew);
            List<Eventousuario> attachedEventousuarioListNew = new ArrayList<Eventousuario>();
            for (Eventousuario eventousuarioListNewEventousuarioToAttach : eventousuarioListNew) {
                eventousuarioListNewEventousuarioToAttach = em.getReference(eventousuarioListNewEventousuarioToAttach.getClass(), eventousuarioListNewEventousuarioToAttach.getEventousuarioPK());
                attachedEventousuarioListNew.add(eventousuarioListNewEventousuarioToAttach);
            }
            eventousuarioListNew = attachedEventousuarioListNew;
            usuario.setEventousuarioList(eventousuarioListNew);
            usuario = em.merge(usuario);
            for (Artigo artigoListNewArtigo : artigoListNew) {
                if (!artigoListOld.contains(artigoListNewArtigo)) {
                    Usuario oldUsuarioIdusuarioOfArtigoListNewArtigo = artigoListNewArtigo.getUsuarioIdusuario();
                    artigoListNewArtigo.setUsuarioIdusuario(usuario);
                    artigoListNewArtigo = em.merge(artigoListNewArtigo);
                    if (oldUsuarioIdusuarioOfArtigoListNewArtigo != null && !oldUsuarioIdusuarioOfArtigoListNewArtigo.equals(usuario)) {
                        oldUsuarioIdusuarioOfArtigoListNewArtigo.getArtigoList().remove(artigoListNewArtigo);
                        oldUsuarioIdusuarioOfArtigoListNewArtigo = em.merge(oldUsuarioIdusuarioOfArtigoListNewArtigo);
                    }
                }
            }
            for (Notaartigo notaartigoListNewNotaartigo : notaartigoListNew) {
                if (!notaartigoListOld.contains(notaartigoListNewNotaartigo)) {
                    Usuario oldUsuarioOfNotaartigoListNewNotaartigo = notaartigoListNewNotaartigo.getUsuario();
                    notaartigoListNewNotaartigo.setUsuario(usuario);
                    notaartigoListNewNotaartigo = em.merge(notaartigoListNewNotaartigo);
                    if (oldUsuarioOfNotaartigoListNewNotaartigo != null && !oldUsuarioOfNotaartigoListNewNotaartigo.equals(usuario)) {
                        oldUsuarioOfNotaartigoListNewNotaartigo.getNotaartigoList().remove(notaartigoListNewNotaartigo);
                        oldUsuarioOfNotaartigoListNewNotaartigo = em.merge(oldUsuarioOfNotaartigoListNewNotaartigo);
                    }
                }
            }
            for (Eventousuario eventousuarioListNewEventousuario : eventousuarioListNew) {
                if (!eventousuarioListOld.contains(eventousuarioListNewEventousuario)) {
                    Usuario oldUsuarioOfEventousuarioListNewEventousuario = eventousuarioListNewEventousuario.getUsuario();
                    eventousuarioListNewEventousuario.setUsuario(usuario);
                    eventousuarioListNewEventousuario = em.merge(eventousuarioListNewEventousuario);
                    if (oldUsuarioOfEventousuarioListNewEventousuario != null && !oldUsuarioOfEventousuarioListNewEventousuario.equals(usuario)) {
                        oldUsuarioOfEventousuarioListNewEventousuario.getEventousuarioList().remove(eventousuarioListNewEventousuario);
                        oldUsuarioOfEventousuarioListNewEventousuario = em.merge(oldUsuarioOfEventousuarioListNewEventousuario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getIdusuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdusuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Artigo> artigoListOrphanCheck = usuario.getArtigoList();
            for (Artigo artigoListOrphanCheckArtigo : artigoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Artigo " + artigoListOrphanCheckArtigo + " in its artigoList field has a non-nullable usuarioIdusuario field.");
            }
            List<Notaartigo> notaartigoListOrphanCheck = usuario.getNotaartigoList();
            for (Notaartigo notaartigoListOrphanCheckNotaartigo : notaartigoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Notaartigo " + notaartigoListOrphanCheckNotaartigo + " in its notaartigoList field has a non-nullable usuario field.");
            }
            List<Eventousuario> eventousuarioListOrphanCheck = usuario.getEventousuarioList();
            for (Eventousuario eventousuarioListOrphanCheckEventousuario : eventousuarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Eventousuario " + eventousuarioListOrphanCheckEventousuario + " in its eventousuarioList field has a non-nullable usuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Usuario findByLoginAndSenha(String login, String senha) {
        EntityManager em = getEntityManager();
        Query q = em.createQuery("select u from Usuario u "
                + "where u.login = :log and u.senha = :sen");
        q.setParameter("log", login);
        q.setParameter("sen", senha);
        try {
            return (Usuario) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
