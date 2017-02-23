package com.damon4u.es.termLevelQuery;

import com.damon4u.es.BaseTest;
import org.elasticsearch.action.search.SearchResponse;
import org.junit.Test;

import static org.elasticsearch.index.query.QueryBuilders.prefixQuery;

/**
 * Description:
 *
 * @author damon4u
 * @version 2017-02-23 11:21
 */
public class PrefixQueryTest extends BaseTest {

    @Test
    public void testPrefixQuery() {
        SearchResponse response = executeQuery(prefixQuery("歌手名称", "周"));
        logger.info(response.toString());
    }

    /**
     * Can only use prefix queries on keyword and text fields
     */
    @Test(expected = Exception.class)
    public void testPrefixQueryDate() {
        SearchResponse response = executeQuery(prefixQuery("评论数", "1"));
        logger.info(response.toString());
    }

}
