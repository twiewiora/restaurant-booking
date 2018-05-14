package com.application.restaurantBooking.persistence.service.impl;

import com.application.restaurantBooking.persistence.builder.RestorerBuilder;
import com.application.restaurantBooking.persistence.model.Restorer;
import com.application.restaurantBooking.persistence.repository.RestorerRepository;
import com.application.restaurantBooking.persistence.service.RestorerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestorerServiceImpl implements RestorerService {

    private RestorerRepository restorerRepository;

    @Autowired
    public RestorerServiceImpl(RestorerRepository restorerRepository) {
        this.restorerRepository = restorerRepository;
    }

    @Override
    public Restorer getById(Long id) {
        return restorerRepository.findById(id).orElse(null);
    }

    @Override
    public Restorer createRestorer(String login, String password) {
        Restorer restorer = new RestorerBuilder().login(login).password(password).build();
        restorerRepository.save(restorer);
        return restorer;
    }

    @Override
    public void deleteRestorer(Long id) {
        restorerRepository.deleteById(id);
    }
}
