package com.javareact.spring6restmvc.mappers;

import com.javareact.spring6restmvc.entities.Beer;
import com.javareact.spring6restmvc.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    Beer beerDtoToBeer(BeerDTO dto);

    BeerDTO beerToBeerDto(Beer beer);
}
