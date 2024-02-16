package com.javareact.spring6restmvc.services;

import com.javareact.spring6restmvc.entities.Beer;
import com.javareact.spring6restmvc.mappers.BeerMapper;
import com.javareact.spring6restmvc.model.BeerDTO;
import com.javareact.spring6restmvc.model.BeerStyle;
import com.javareact.spring6restmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;


@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;

/**
 * This method is used to list beers based on the provided parameters.
 * It uses the buildPageRequest method to create a PageRequest object for pagination.
 * The method then checks the provided beer name and beer style to determine the appropriate method to call for retrieving beers.
 * If the showInventory parameter is false, the quantity on hand for each beer is set to null.
 * The method returns a Page of BeerDTO objects.
 *
 * @param beerName The name of the beer to be retrieved. If this parameter is not null or empty, beers with this name will be retrieved.
 * @param beerStyle The style of the beer to be retrieved. If this parameter is not null, beers with this style will be retrieved.
 * @param showInventory If this parameter is false, the quantity on hand for each beer is set to null.
 * @param pageNumber The number of the page to be retrieved. Page numbers start from 1.
 * @param pageSize The number of records to be retrieved in a single page. The maximum page size is 1000.
 * @return Page of BeerDTO objects.
 */
@Override
public Page<BeerDTO> listBeers(String beerName,
                               BeerStyle beerStyle,
                               Boolean showInventory,
                               Integer pageNumber,
                               Integer pageSize) {

    // Create a PageRequest object for pagination.
    PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

    // Declare a Page object to hold the beers to be retrieved.
    Page<Beer> beerPage;

    // Check the provided beer name and beer style to determine the appropriate method to call for retrieving beers.
    if (StringUtils.hasText(beerName) && beerStyle == null) {
        // If the beer name is not null or empty and the beer style is null, retrieve beers with the provided name.
        beerPage = listBeersByName(beerName, pageRequest);
    }
    else if (!StringUtils.hasText(beerName) && beerStyle != null) {
        // If the beer name is null or empty and the beer style is not null, retrieve beers with the provided style.
        beerPage = listBeerByStyle(beerStyle, pageRequest);
    }
    else if (StringUtils.hasText(beerName) && beerStyle != null) {
        // If both the beer name and beer style are not null or empty, retrieve beers with the provided name and style.
        beerPage = listBeersByNameAndStyle(beerName, beerStyle, pageRequest);
    }
    else {
        // If both the beer name and beer style are null or empty, retrieve all beers.
        beerPage = beerRepository.findAll(pageRequest);
    }

    // If the showInventory parameter is false, set the quantity on hand for each beer to null.
    if(showInventory != null && ! showInventory) {
        beerPage.forEach(beer -> beer.setQuantityOnHand(null));
    }

    // Return a Page of BeerDTO objects.
    return beerPage.map((beerMapper::beerToBeerDto));
}


    /**
     * This method is used to build a PageRequest object which is used for pagination in Spring Data JPA.
     * The PageRequest object contains information about the page number, page size and sorting details.
     *
     * @param pageNumber The number of the page to be retrieved. Page numbers start from 1.
     * @param pageSize The number of records to be retrieved in a single page. The maximum page size is 1000.
     * @return PageRequest object with the provided page number, page size and a Sort object to sort the results in ascending order by beer name.
     */
    public PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        // The page number for the query. If the provided page number is null or less than 1, the default page number is used.
        int queryPageNumber;

        // The page size for the query. If the provided page size is null, the default page size is used.
        // If the provided page size is more than 1000, the page size is set to 1000.
        int queryPageSize;

        // If the page number is not null and is greater than 0, the page number is set to the provided page number minus 1.
        // This is because page numbers in Spring Data JPA start from 0.
        if (pageNumber != null && pageNumber > 0) {
            queryPageNumber = pageNumber - 1;
        } else {
            queryPageNumber = DEFAULT_PAGE_NUMBER;
        }

        // If the page size is null, the default page size is used.
        // If the page size is more than 1000, the page size is set to 1000.
        // Otherwise, the provided page size is used.
        if (pageSize == null) {
            queryPageSize = DEFAULT_PAGE_SIZE;
        } else {
            if(pageSize > 1000) {
                queryPageSize = 1000;
            } else {
                queryPageSize = pageSize;
            }
        }

        // A Sort object is created to sort the results in ascending order by beer name.
        Sort sort = Sort.by(Sort.Order.asc("beerName"));

        // A PageRequest object is created with the page number, page size and Sort object.
        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }


    private Page<Beer> listBeersByNameAndStyle(String beerName, BeerStyle beerStyle, Pageable pageable) {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("%" + beerName + "%", beerStyle, pageable);
    }

    public Page<Beer> listBeerByStyle(BeerStyle beerStyle, Pageable pageable) {
        return beerRepository.findAllByBeerStyle(beerStyle, pageable);
    }

    public Page<Beer> listBeersByName(String beerName, Pageable pageable){
        return beerRepository.findAllByBeerNameIsLikeIgnoreCase("%" + beerName + "%", pageable);
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID beerId) {
        return Optional.ofNullable(beerMapper.beerToBeerDto(beerRepository.findById(beerId).orElse(null)));
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {
        return beerMapper.beerToBeerDto(beerRepository.save(beerMapper.beerDtoToBeer(beer)));
    }

    @Override
    public Optional<BeerDTO> updateById(UUID beerid, BeerDTO beer) {
        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();
        beerRepository.findById(beerid).ifPresentOrElse(beer1 -> {
            beer1.setBeerName(beer.getBeerName());
            beer1.setBeerStyle(beer.getBeerStyle());
            beer1.setPrice(beer.getPrice());
            beer1.setUpc(beer.getUpc());
            beer1.setQuantityOnHand(beer.getQuantityOnHand());
            atomicReference.set(Optional.of(beerMapper.beerToBeerDto(beerRepository.save(beer1))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    @Override
    public Boolean deletById(UUID beerId) {
        if (beerRepository.existsById(beerId)) {
            beerRepository.deleteById(beerId);
            return true;
        }
        return false;
    }

    @Override
    public Optional<BeerDTO> patchByBeerId(UUID beerId, BeerDTO beer) {

        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();

        beerRepository.findById(beerId).ifPresentOrElse(foundBeer -> {
            if (StringUtils.hasText(beer.getBeerName())){
                foundBeer.setBeerName(beer.getBeerName());
            }
            if (beer.getBeerStyle() != null){
                foundBeer.setBeerStyle(beer.getBeerStyle());
            }
            if (StringUtils.hasText(beer.getUpc())){
                foundBeer.setUpc(beer.getUpc());
            }
            if (beer.getPrice() != null){
                foundBeer.setPrice(beer.getPrice());
            }
            if (beer.getQuantityOnHand() != null){
                foundBeer.setQuantityOnHand(beer.getQuantityOnHand());
            }
            atomicReference.set(Optional.of(beerMapper
                    .beerToBeerDto(beerRepository.save(foundBeer))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }
}
