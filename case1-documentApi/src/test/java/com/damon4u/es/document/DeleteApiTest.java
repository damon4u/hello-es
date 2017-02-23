package com.damon4u.es.document;

import com.damon4u.es.BaseTest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.junit.Test;

/**
 * Description:
 * 这里的delete只是删除文档
 * @author damon4u
 * @version 2017-02-17 17:50
 */
public class DeleteApiTest extends BaseTest {

    @Test
    public void testDelete() {
        DeleteResponse response = client.prepareDelete(_INDEX, _TYPE, "4").get();
        logger.info(response.toString());
    }
}
