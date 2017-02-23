package com.damon4u.es.termLevelQuery;

import com.damon4u.es.BaseTest;
import org.elasticsearch.action.search.SearchResponse;
import org.junit.Test;

import static org.elasticsearch.index.query.QueryBuilders.existsQuery;

/**
 * Description:
 *
 * @author damon4u
 * @version 2017-02-23 11:18
 */
public class ExistQueryTest extends BaseTest {

    /**
     * 返回有评论的doc
     */
    @Test
    public void testExists() {
        SearchResponse response = executeQuery(existsQuery("评论数"));
        logger.info(response.toString());
    }
}
