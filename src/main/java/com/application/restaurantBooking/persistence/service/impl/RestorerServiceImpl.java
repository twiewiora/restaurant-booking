package com.application.restaurantBooking.persistence.service.impl;

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
    public Restorer getByUsername(String username) {
        return restorerRepository.findByUsername(username);
    }

    @Override
    public Restorer createRestorer(Restorer restorer) {
        restorerRepository.save(restorer);
        return restorer;
    }
}
