package com.damon4u.es.termLevelQuery;

import com.damon4u.es.BaseTest;
import org.elasticsearch.action.search.SearchResponse;
import org.junit.Test;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Description:
 *
 * @author damon4u
 * @version 2017-02-23 11:03
 */
public class TermsQueryTest extends BaseTest {

    @Test
    public void testTermsQuery() {
        SearchResponse response = executeQuery(
                termsQuery("发行时间","2012-1-4","2012-1-1")
        );
        logger.info(response.toString());
    }
}
