package com.damon4u.es.document;

import com.damon4u.es.BaseTest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.junit.Test;

import java.io.IOException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Description:
 * 管道执行多条创建doc和删除doc的操作
 * @author damon4u
 * @version 2017-02-20 14:22
 */
public class BulkApiTest extends BaseTest {

    @Test
    public void testBulkApi() throws IOException {
        BulkRequestBuilder bulkRequest = client.prepareBulk();

        // either use client#prepare, or use Requests# to directly build index/delete requests
        bulkRequest.add(client.prepareIndex(_INDEX, _TYPE, "7")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("歌曲名称", "绅士")
                        .field("歌手名称", "薛之谦")
                        .field("发行时间", "2015-12-12")
                        .field("评论数", 44444L)
                        .endObject()
                )
        );

        // 指定version,出错
        bulkRequest.add(client.prepareIndex(_INDEX, _TYPE, "1")
                .setVersion(1)
                .setSource(jsonBuilder()
                        .startObject()
                        .field("歌曲名称", "白玫瑰")
                        .field("歌手名称", "陈奕迅")
                        .field("发行时间", "2010-1-1")
                        .field("评论数", 55555L)
                        .endObject()
                )
        );

        BulkResponse bulkResponse = bulkRequest.get();
        if (bulkResponse.hasFailures()) {
            // process failures by iterating through each bulk response item
            for (BulkItemResponse response : bulkResponse) {
                logger.error(response.getFailureMessage());
            }
        }
    }
}
