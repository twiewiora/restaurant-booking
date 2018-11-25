package com.application.restaurantbooking.persistence.service.impl;

import com.application.restaurantbooking.persistence.model.Client;
import com.application.restaurantbooking.persistence.model.Price;
import com.application.restaurantbooking.persistence.model.Tag;
import com.application.restaurantbooking.persistence.repository.ClientRepository;
import com.application.restaurantbooking.persistence.service.ClientService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;

@Service
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;

    private SessionFactory sessionFactory;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository,
                             EntityManagerFactory factory){
        this.clientRepository = clientRepository;
        if(factory.unwrap(SessionFactory.class) == null){
            throw new NullPointerException("factory is not a hibernate factory");
        }
        this.sessionFactory = factory.unwrap(SessionFactory.class);
    }

    @Override
    public Client getById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public Client getByUsername(String username) {
        return clientRepository.findByUsername(username);
    }

    @Override
    public Client createClient(Client client) {
        clientRepository.save(client);
        return client;
    }

    @Override
    public void updateClient(Client client) {
        clientRepository.save(client);
    }

    @Override
    public Integer getStatisticsForPrice(Long clientId, Price price) {
        Session session = sessionFactory.openSession();
        String hql = "select count(c.id) from Client c\n" +
                "        inner join c.reservations res on c.id = res.client\n" +
                "        inner join res.restaurantTable rt on res.restaurantTable = rt.id\n" +
                "        inner join rt.restaurant rest on rt.restaurant = rest.id\n" +
                "        where c.id = :clientId and :price = rest.price";
        Query query = session.createQuery(hql);
        query.setParameter("clientId", clientId);
        query.setParameter("price", price);
        Integer result = ((Long) query.uniqueResult()).intValue();
        session.close();
        return result;
    }

    @Override
    public Integer getStatisticsForTag(Long clientId, Tag tag) {
        Session session = sessionFactory.openSession();
        String hql = "select count(c.id) from Client c\n" +
                "        inner join c.reservations res on c.id = res.client\n" +
                "        inner join res.restaurantTable rt on res.restaurantTable = rt.id\n" +
                "        inner join rt.restaurant rest on rt.restaurant = rest.id\n" +
                "        where c.id = :clientId and :tag in elements(rest.tags)";
        Query query = session.createQuery(hql);
        query.setParameter("clientId", clientId);
        query.setParameter("tag", tag);
        Integer result = ((Long) query.uniqueResult()).intValue();
        session.close();
        return result;
    }

    @Override
    public Integer getStatisticsForRestaurant(Long clientId, Long restaurantId) {
        Session session = sessionFactory.openSession();
        String hql = "select count(c.id) from Client c\n" +
                "        inner join c.reservations res on c.id = res.client\n" +
                "        inner join res.restaurantTable rt on res.restaurantTable = rt.id\n" +
                "        inner join rt.restaurant rest on rt.restaurant = rest.id\n" +
                "        where c.id = :clientId and :restaurantId = rest.id";
        Query query = session.createQuery(hql);
        query.setParameter("clientId", clientId);
        query.setParameter("restaurantId", restaurantId);
        Integer result = ((Long) query.uniqueResult()).intValue();
        session.close();
        return result;
    }

    @Override
    public Integer getAmountClientReservations(Long clientId) {
        Session session = sessionFactory.openSession();
        String hql = "select count(c.id) from Client c\n" +
                "        inner join c.reservations res on c.id = res.client\n" +
                "        where c.id = :clientId";
        Query query = session.createQuery(hql);
        query.setParameter("clientId", clientId);
        Integer result = ((Long) query.uniqueResult()).intValue();
        session.close();
        return result;
    }
}
