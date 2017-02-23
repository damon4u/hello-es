package com.damon4u.es;

import com.damon4u.es.bean.Song;
import com.damon4u.es.client.ClientFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;



/**
 * Description:
 *
 * @author damon4u
 * @version 2017-02-17 17:34
 */
public class BaseTest {
    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

    protected static TransportClient client;

    protected static ObjectMapper objectMapper;

    protected static final String _INDEX = "音乐";

    protected static final String _INDEX_COMMON_TERMS = "common_terms测试";


    protected static final String _TYPE = "流行";

    @BeforeClass
    public static void initClass() {
        client = ClientFactory.getDefaultClient();
        objectMapper = new ObjectMapper();
    }

    protected void handlerResponse(GetResponse response) throws IOException {
        if (response.isExists()) {
            String sourceAsString = response.getSourceAsString();
            logger.info(sourceAsString);
            Song song = objectMapper.readValue(sourceAsString, Song.class);
            System.out.println(song);
        }
    }

    protected SearchResponse executeQuery(QueryBuilder query) {
        return client.prepareSearch(_INDEX)
                .setTypes(_TYPE)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(query)
                .get();
    }
}
