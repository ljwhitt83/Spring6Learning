package com.javareact.spring6restmvc.services;

import com.javareact.spring6restmvc.model.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface BeerService {
    List<BeerDTO> listBeers();

    Optional<BeerDTO> getBeerById(UUID beerId);

    BeerDTO saveNewBeer(BeerDTO beer);

    Optional<BeerDTO> updateById(UUID beerid, BeerDTO beer);

    Boolean deletById(UUID beerId);

    Optional<BeerDTO> patchByBeerId(UUID beerId, BeerDTO beer);
}
