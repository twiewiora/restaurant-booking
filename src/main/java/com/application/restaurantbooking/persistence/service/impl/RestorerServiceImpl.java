package com.application.restaurantbooking.persistence.service.impl;

import com.application.restaurantbooking.persistence.model.Restorer;
import com.application.restaurantbooking.persistence.repository.RestorerRepository;
import com.application.restaurantbooking.persistence.service.RestorerService;
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
    public Restorer getByUsername(String username) {
        return restorerRepository.findByUsername(username);
    }

    @Override
    public Restorer createRestorer(Restorer restorer) {
        restorerRepository.save(restorer);
        return restorer;
    }
}
