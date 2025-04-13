package com.teameight.tourplanner.repository;

import com.teameight.tourplanner.model.TourLog;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;
import java.util.Optional;

public class TourLogRepositoryOrm implements TourLogRepository {

    private final EntityManagerFactory entityManagerFactory;

    public TourLogRepositoryOrm() {
        this.entityManagerFactory =
                Persistence.createEntityManagerFactory("hibernate");
    }

    @Override
    public Optional<TourLog> find(String id) {
        CriteriaBuilder cb = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<TourLog> query = cb.createQuery(TourLog.class);
        Root<TourLog> root = query.from(TourLog.class);

        query.select(root).where(cb.equal(root.get("id"), id));

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return Optional.ofNullable(
                    entityManager.createQuery(query).getSingleResultOrNull()
            );
        }
    }

    @Override
    public List<TourLog> findAll() {
        CriteriaBuilder cb = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<TourLog> query = cb.createQuery(TourLog.class);
        Root<TourLog> root = query.from(TourLog.class);

        query.select(root);

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery(query).getResultList();
        }
    }

    @Override
    public TourLog save(TourLog entity) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();

            transaction.begin();
            if (find(entity.getId()).isEmpty()) {
                entityManager.persist(entity);
            } else {
                entity = entityManager.merge(entity);
            }
            transaction.commit();

            return entity;
        }
    }

    @Override
    public TourLog delete(TourLog entity) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();

            transaction.begin();
            if (!entityManager.contains(entity)) {
                entity = entityManager.merge(entity);
            }
            entityManager.remove(entity);
            transaction.commit();

            return entity;
        }
    }

    @Override
    public List<TourLog> deleteAll() {
        List<TourLog> logs = findAll();

        CriteriaBuilder cb = entityManagerFactory.getCriteriaBuilder();
        CriteriaDelete<TourLog> query = cb.createCriteriaDelete(TourLog.class);
        query.from(TourLog.class);

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();

            transaction.begin();
            entityManager.createQuery(query).executeUpdate();
            transaction.commit();
        }

        return logs;
    }

    @Override
    public List<TourLog> findByTourId(String tourId) {
        CriteriaBuilder cb = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<TourLog> query = cb.createQuery(TourLog.class);
        Root<TourLog> root = query.from(TourLog.class);

        query.select(root).where(cb.equal(root.get("tourId"), tourId));

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery(query).getResultList();
        }
    }

    @Override
    public List<TourLog> deleteByTourId(String tourId) {
        List<TourLog> logs = findByTourId(tourId);

        CriteriaBuilder cb = entityManagerFactory.getCriteriaBuilder();
        CriteriaDelete<TourLog> query = cb.createCriteriaDelete(TourLog.class);
        Root<TourLog> root = query.from(TourLog.class);

        query.where(cb.equal(root.get("tourId"), tourId));

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();

            transaction.begin();
            entityManager.createQuery(query).executeUpdate();
            transaction.commit();
        }

        return logs;
    }
}
