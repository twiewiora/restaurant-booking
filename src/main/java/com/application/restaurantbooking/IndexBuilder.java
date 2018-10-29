package com.application.restaurantbooking;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class IndexBuilder {

    private static final Logger LOGGER = Logger.getLogger(IndexBuilder.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    public IndexBuilder() {
        // constructor for spring @Component
    }

    @Transactional
    public void rebuildIndexes() {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        try {
            fullTextEntityManager.createIndexer().startAndWait();
        } catch (InterruptedException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }
    }
}
