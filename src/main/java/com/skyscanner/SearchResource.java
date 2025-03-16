package com.skyscanner;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Path("/search")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SearchResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchResource.class);
    private final List<SearchResult> searchResults;

    // Constructor to initialize searchResults
    public SearchResource(List<SearchResult> searchResults) {
        this.searchResults = searchResults;
    }

    @POST
    public Response search(Search search) {
        String city = search.getCity();

        // Validate input
        if (city == null || city.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("City must not be empty"))
                    .build();
        }

        // Normalize city to lowercase
        city = city.toLowerCase();

        // Search Logic
        LOGGER.info("Searching for city: {}", city);
        List<SearchResult> results = searchResults.stream()
                .filter(result -> city.equals(result.getCity().toLowerCase())) // Simplified comparison
                .collect(Collectors.toList());

        // Handle the response
        return results.isEmpty()
                ? Response.noContent().build() // No content found
                : Response.ok(results).build(); // Return results
    }

    // Inner class for error responses
    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}