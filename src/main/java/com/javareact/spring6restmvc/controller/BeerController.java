package com.javareact.spring6restmvc.controller;

import com.javareact.spring6restmvc.model.BeerDTO;
import com.javareact.spring6restmvc.model.BeerStyle;
import com.javareact.spring6restmvc.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/beer")
public class BeerController {

    private final BeerService beerService;

    @PatchMapping("{beerId}")
    public ResponseEntity updateBeerPatchById(@PathVariable("beerId") UUID beerId, @RequestBody BeerDTO beer) {
        beerService.patchByBeerId(beerId, beer);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{beerId}")
    public ResponseEntity deleteById(@PathVariable("beerId") UUID beerId) {
        if (!beerService.deletById(beerId)){
            throw new NotFoundException();
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{beerId}")
    public ResponseEntity updateById(@PathVariable UUID beerid, @RequestBody BeerDTO beer) {

       if  (beerService.updateById(beerid, beer).isEmpty()) {
           throw new NotFoundException();
       }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity handlePost(@Validated @RequestBody BeerDTO beer){
        BeerDTO savedBeer= beerService.saveNewBeer(beer);

        //create a header object
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/beer/" + savedBeer.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<BeerDTO> listBeers(@RequestParam(required = false) String beerName,
                                   @RequestParam(required = false)BeerStyle beerStyle,
                                   @RequestParam(required = false) Boolean showInventory,
                                   @RequestParam(required = false) Integer pageNumber,
                                   @RequestParam(required = false) Integer pageSize) {

        log.debug("List Beers in controller ");

        return beerService.listBeers(beerName, beerStyle, showInventory, 1, 25);
    }

    @RequestMapping(value = "{beerId}", method = RequestMethod.GET)
    public BeerDTO getBeerById(@PathVariable("beerId")  UUID beerId)  {

        log.debug("Get BeerDTO by id in controller ");

        return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }
}
