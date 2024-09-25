package com.clf.dao;

import com.clf.model.Owner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class OwnerDao implements Dao<Owner> {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("cats-owners");

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public void save(Owner owner) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(owner);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public Owner findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Owner.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Owner> findAll() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("from Owner", Owner.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Owner owner) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(owner);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Owner owner) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.remove(em.contains(owner) ? owner : em.merge(owner));
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Long id) {
        EntityManager em = getEntityManager();
        try {
            Owner owner = em.find(Owner.class, id);
            if (owner != null) {
                em.getTransaction().begin();
                em.remove(owner);
                em.getTransaction().commit();
            }
        } finally {
            em.close();
        }
    }
}
