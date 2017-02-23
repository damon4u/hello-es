package com.damon4u.es.document;

import com.damon4u.es.BaseTest;
import org.elasticsearch.action.get.GetResponse;
import org.junit.Test;

/**
 * Description:
 *
 * @author damon4u
 * @version 2017-02-17 17:30
 */
public class GetApiTest extends BaseTest {

    @Test
    public void testGet() throws Exception {
        GetResponse response = client.prepareGet(_INDEX, _TYPE, "1").get();
        handlerResponse(response);
    }

    @Test
    public void testGetField() throws Exception {
        GetResponse response = client.prepareGet(_INDEX, _TYPE, "1")
                .setFetchSource("歌曲名称", null)
                .get();
        handlerResponse(response);
    }
}
