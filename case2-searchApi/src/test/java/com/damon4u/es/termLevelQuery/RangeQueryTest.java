package com.damon4u.es.termLevelQuery;

import com.damon4u.es.BaseTest;
import org.elasticsearch.action.search.SearchResponse;
import org.junit.Test;

import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;

/**
 * Description:
 *
 * @author damon4u
 * @version 2017-02-23 11:12
 */
public class RangeQueryTest extends BaseTest {

    /**
     * 具体的时间比较规则:https://www.elastic.co/guide/en/elasticsearch/reference/5.2/common-options.html#date-math
     */
    @Test
    public void testRangeQuery() {
        SearchResponse response = executeQuery(rangeQuery("发行时间").gte("2012-1-2").lt("2012-1-4"));
        logger.info(response.toString());
    }
}
