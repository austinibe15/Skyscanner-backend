package com.skyscanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.Application; // Corrected import
import io.dropwizard.setup.Bootstrap; // Corrected import
import io.dropwizard.setup.Environment; // Corrected import
import io.dropwizard.jdbi3.DBIFactory; // Add this if you're using JDBI
import org.skife.jdbi.v2.DBI; // Add this if you're using JDBI

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HoenScannerApplication extends Application<HoenScannerConfiguration> {

    private List<SearchResult> searchResults = new ArrayList<>();

    public static void main(final String[] args) throws Exception {
        new HoenScannerApplication().run(args);
    }

    @Override
    public String getName() {
        return "hoen-scanner";
    }

    @Override
    public void initialize(final Bootstrap<HoenScannerConfiguration> bootstrap) {
        // You might need additional initialization here depending on your configuration
    }

    @Override
    public void run(final HoenScannerConfiguration configuration, final Environment environment) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        loadSearchResultsFromFile(mapper, "src/main/resources/hotels.json");
        loadSearchResultsFromFile(mapper, "src/main/resources/rental_cars.json");

        environment.jersey().register(new SearchResource(searchResults));

        //Example of using JDBI if you have a database connection:
        //final DBIFactory factory = new DBIFactory();
        //final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "mydb");


    }

    private void loadSearchResultsFromFile(ObjectMapper mapper, String filepath) throws IOException {
        SearchResult[] results = mapper.readValue(new File(filepath), SearchResult[].class);
        for (SearchResult result : results) {
            searchResults.add(result);
        }
    }
}
