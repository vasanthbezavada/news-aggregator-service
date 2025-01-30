# News Aggregator Microservice

This is a Java-based News Search Microservice designed to find relevant news based on a keyword input from the end user (e.g., "apple"). The service integrates with the Guardian and New York Times APIs to aggregate results, ensuring no duplicates in the final output. The service also supports pagination with default settings if the optional parameters are not provided.

## Features

- **Search News by Keyword**: Allows users to input a keyword and receive relevant news results.
- **Pagination Support**: Displays results with pagination support. Defaults are applied when pagination parameters are missing.
- **News Aggregation**: Combines news from the Guardian and New York Times, eliminating duplicates.
- **Offline Mode**: The service supports offline mode, providing cached results when the public APIs are unavailable.
- **Production Ready**: Designed to be deployed to a live environment.
- **Web and Postman Access**: Can be accessed via a web browser or Postman (support for JavaScript frameworks, HTML, or JSON).

## API Endpoints

### `GET /news/search`

Search for news articles based on the keyword provided.

**Query Parameters:**
- `keyword`: The keyword to search for (e.g., "apple").
- `page`: The page number for pagination (optional, default is 1).
- `limit`: The number of results per page (optional, default is 5).

**Response:**
Returns a list of news articles that match the search keyword.

**Example Request:**
```http
GET /search?keyword=apple&page=1&limit=5
