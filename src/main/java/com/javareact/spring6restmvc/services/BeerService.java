package com.javareact.spring6restmvc.services;

import com.javareact.spring6restmvc.model.BeerDTO;
import com.javareact.spring6restmvc.model.BeerStyle;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;


public interface BeerService {
    Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize);

    Optional<BeerDTO> getBeerById(UUID beerId);

    BeerDTO saveNewBeer(BeerDTO beer);

    Optional<BeerDTO> updateById(UUID beerid, BeerDTO beer);

    Boolean deletById(UUID beerId);

    Optional<BeerDTO> patchByBeerId(UUID beerId, BeerDTO beer);
}
