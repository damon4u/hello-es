package com.damon4u.es.document;

import com.damon4u.es.BaseTest;
import com.damon4u.es.bean.Song;
import com.damon4u.es.util.DateTimeUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.elasticsearch.action.index.IndexResponse;
import org.junit.Test;

import java.io.IOException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.junit.Assert.assertEquals;

/**
 * Description:
 * 创建文档
 *
 * @author damon4u
 * @version 2017-02-17 16:36
 */
public class IndexApiTest extends BaseTest {
    /**
     * 如果之前没有index或者type,在插入数据的同时,会使用默认设置和mapping创建index,type
     * 如果指定id的文档之前不存在,那么会新建一个文档
     * 如果指定id的文档之前存在,那么会更新文档,version增加
     * 如果不指定id,那么会使用一个UUID
     */
    @Test
    public void testCreateIndexOnInsert() throws IOException {
        String id = "4";
        IndexResponse response = client.prepareIndex(_INDEX, _TYPE, id)
                .setSource(jsonBuilder()
                        .startObject()
                        .field("歌曲名称", "无与伦比演唱会")
                        .field("歌手名称", "杰伦")
                        .field("发行时间", "2012-1-4")
                        .field("评论数", 4)
                        .endObject())
                .get();
        assertEquals(_INDEX, response.getIndex());
        assertEquals(_TYPE, response.getType());
        assertEquals(id, response.getId());
    }

    /**
     * 更新指定版本号的文档
     * 如果文档之前不存在,那么会抛异常
     * 如果之前文档的版本与指定版本相同,才会更新
     * 否则同样会抛异常
     * [音乐/o4R2wKNmTfmWq_Tm9pTLdg][[音乐][0]] VersionConflictEngineException[[流行][2]: version conflict, current version [-1] is different than the one provided [1]]
     *
     * @throws IOException
     */
    @Test
    public void testCreateWithVersion() throws IOException {
        IndexResponse response = client.prepareIndex(_INDEX, _TYPE, "2")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("歌曲名称", "演员")
                        .field("歌手名称", "薛之谦")
                        .field("发行时间", "2015-12-12")
                        .field("评论数", 33901L)
                        .endObject())
                .setVersion(1)
                .get();
        assertEquals(_INDEX, response.getIndex());
        assertEquals(_TYPE, response.getType());
        assertEquals("2", response.getId());
    }

    @Test
    public void testIndexInJackson() throws JsonProcessingException {
        Song song = new Song("夜曲", "周杰伦", DateTimeUtil.parseToDate("2007-12-12"), 12341);
        IndexResponse response = client.prepareIndex(_INDEX, _TYPE, "4")
                .setSource(objectMapper.writeValueAsBytes(song))
                .get();
        assertEquals(_INDEX, response.getIndex());
        assertEquals(_TYPE, response.getType());
        assertEquals("4", response.getId());
    }
}
