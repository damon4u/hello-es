package com.damon4u.es.document;

import com.damon4u.es.BaseTest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Description:
 *
 * @author damon4u
 * @version 2017-02-17 17:59
 */
public class UpdateApiTest extends BaseTest {

    @Test
    public void testUpdateExistField() throws IOException {
        UpdateResponse response = client.prepareUpdate(_INDEX, _TYPE, "1")
                .setDoc(jsonBuilder()
                        .startObject()
                        .field("评论数", 12345)
                        .endObject())
                .get();
        logger.info(response.toString());
    }

    /**
     * 如果属性之前不存在,那么会添加一个
     * @throws IOException
     */
    @Test
    public void testUpdateNotExistField() throws IOException {
        UpdateResponse response = client.prepareUpdate(_INDEX, _TYPE, "1")
                .setDoc(jsonBuilder()
                        .startObject()
                            .field("是否收藏", true)
                        .endObject())
                .get();
        logger.info(response.toString());
    }

    @Test
    public void testUpsert() throws IOException, ExecutionException, InterruptedException {
        IndexRequest indexRequest = new IndexRequest(_INDEX,_TYPE,"6")
                .source(jsonBuilder()
                .startObject()
                .field("歌曲名称", "一路向北")
                .field("歌手名称", "周杰伦")
                .field("发行时间", "2011-12-12")
                .field("评论数", 2222L)
                .endObject());
        UpdateRequest updateRequest = new UpdateRequest(_INDEX, _TYPE, "1")
                .doc(jsonBuilder().startObject().field("是否收藏", false).endObject()).upsert(indexRequest);
        UpdateResponse response = client.update(updateRequest).get();
        logger.info(response.toString());
    }

    @Test
    public void testUpsertDocNotExist() throws IOException, ExecutionException, InterruptedException {
        IndexRequest indexRequest = new IndexRequest(_INDEX,_TYPE,"6")
                .source(jsonBuilder()
                        .startObject()
                        .field("歌曲名称", "一路向北")
                        .field("歌手名称", "周杰伦")
                        .field("发行时间", "2011-12-12")
                        .field("是否收藏", false)
                        .field("评论数", 2222L)
                        .endObject());
        UpdateRequest updateRequest = new UpdateRequest(_INDEX, _TYPE, "6")
                .doc(jsonBuilder().startObject().field("是否收藏", false).endObject()).upsert(indexRequest);
        UpdateResponse response = client.update(updateRequest).get();
        logger.info(response.toString());
    }
}
