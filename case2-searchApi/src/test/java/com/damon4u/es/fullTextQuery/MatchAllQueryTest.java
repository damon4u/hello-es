package com.damon4u.es.fullTextQuery;

import com.damon4u.es.BaseTest;
import org.elasticsearch.action.search.SearchResponse;
import org.junit.Test;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Description:
 *
 * @author damon4u
 * @version 2017-02-20 15:29
 */
public class MatchAllQueryTest extends BaseTest {

    @Test
    public void testMatchAllQuery() {
        SearchResponse response = executeQuery(matchAllQuery());
        logger.info(response.toString());
    }
}
