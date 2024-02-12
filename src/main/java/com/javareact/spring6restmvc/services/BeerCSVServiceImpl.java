package com.javareact.spring6restmvc.services;

import com.javareact.spring6restmvc.model.BeerCSVRecord;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;


@Service
public class BeerCSVServiceImpl implements BeerCSVService {

    @Override
    /**
     * This method is used to convert a CSV file into a list of BeerCSVRecord objects.
     *
     * @param csvFile This is the CSV file that will be converted.
     * @return List<BeerCSVRecord> This returns a list of BeerCSVRecord objects.
     * @throws RuntimeException if the file is not found.
     */
    public List<BeerCSVRecord> convertCSV(File csvFile) {

        try {
            // Create a new CsvToBeanBuilder object with the FileReader of the csvFile
            // Set the type of the CsvToBeanBuilder to BeerCSVRecord.class
            // Build and parse the CsvToBeanBuilder to get a list of BeerCSVRecord objects
            List<BeerCSVRecord> beerCSVRecords = new CsvToBeanBuilder<BeerCSVRecord>(new FileReader(csvFile))
                    .withType(BeerCSVRecord.class)
                    .build().parse();

            // Return the list of BeerCSVRecord objects
            return beerCSVRecords;
        } catch (FileNotFoundException e) {
            // If the file is not found, throw a RuntimeException
            throw new RuntimeException(e);
        }
    }
}
