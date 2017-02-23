package com.damon4u.es.document;

import com.damon4u.es.BaseTest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.junit.Test;

/**
 * Description:
 *
 * @author damon4u
 * @version 2017-02-20 14:14
 */
public class MultiGetApiTest extends BaseTest {

    @Test
    public void testMultiGet() {
        MultiGetResponse responses = client.prepareMultiGet().add(_INDEX, _TYPE, "1").add(_INDEX, _TYPE, "2", "3", "4").get();
        for (MultiGetItemResponse itemResponse : responses) {
            logger.info("id:" + itemResponse.getId());
            GetResponse response = itemResponse.getResponse();
            if (response.isExists()) {
                logger.info(response.getSourceAsString());
            }
        }
    }
}
