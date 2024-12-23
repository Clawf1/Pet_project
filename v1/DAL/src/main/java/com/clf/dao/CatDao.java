package com.clf.dao;

import com.clf.model.Cat;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class CatDao implements Dao<Cat> {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("cats-owners");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public void save(Cat cat) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(cat);
            em.getTransaction().commit();
        }
        catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
        finally {
            em.close();
        }
    }

    @Override
    public Cat findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cat.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Cat> findAll() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("from Cat", Cat.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Cat cat) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(cat);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Cat cat) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.remove(em.contains(cat) ? cat : em.merge(cat));
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Long id) {
        EntityManager em = getEntityManager();
        try {
            Cat cat = em.find(Cat.class, id);
            if (cat != null) {
                em.getTransaction().begin();
                em.remove(cat);
                em.getTransaction().commit();
            }
        } finally {
            em.close();
        }
    }
}
