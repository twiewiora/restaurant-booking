package com.application.restaurantbooking;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class IndexBuilder {

    @PersistenceContext
    private EntityManager entityManager;

    public IndexBuilder() {
        // constructor for spring @Component
    }

    @Transactional
    public void rebuildIndexes() throws InterruptedException {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        fullTextEntityManager.createIndexer().startAndWait();
    }
}
