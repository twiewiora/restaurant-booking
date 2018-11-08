package com.application.restaurantbooking.controllers.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ProposalHoursDTO {

    private List<String> proposalHours;

}
