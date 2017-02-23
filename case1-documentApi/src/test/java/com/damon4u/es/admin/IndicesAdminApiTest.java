package com.damon4u.es.admin;

import com.damon4u.es.client.ClientFactory;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.admin.indices.refresh.RefreshResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.IndexNotFoundException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * Description:
 * 索引管理
 * 包括创建索引,设置settings,设置mapping等
 *
 * @author damon4u
 * @version 2017-02-17 14:49
 */
public class IndicesAdminApiTest {
    private static final Logger logger = LoggerFactory.getLogger(IndicesAdminApiTest.class);

    private static TransportClient client;

    private static final String _INDEX = "音乐";

    private static final String _TYPE = "流行";

    @BeforeClass
    public static void initClass() {
        client = ClientFactory.getDefaultClient();
    }

//    @Before
    public void deleteIndex() {
        /**
         * client.prepareDelete() 只能指定index,type和id删除一个文档
         */
        //client.prepareDelete().setType(_INDEX).get();
        /**
         * 使用下面的方式删除整个index
         */
        try {
            logger.info("deleting index: " + _INDEX);
            DeleteIndexResponse response = client.admin().indices().prepareDelete(_INDEX).get();
            if (response.isAcknowledged()) {
                logger.info("deleted index: " + _INDEX);
            }
        } catch (IndexNotFoundException e) {
            logger.info("index not found: " + _INDEX);
        }
    }

    @Test
    public void testCreateIndexWithDefault() {
        deleteIndex();
        CreateIndexResponse response = client.admin().indices().prepareCreate(_INDEX).get();
        logger.info("testCreateIndexWithDefault response: " + response.isShardsAcked());
    }

    @Test
    public void testCreateIndexWithSettingsAndMappings() throws IOException {
//        deleteIndex();
        CreateIndexResponse response = client.admin().indices().prepareCreate(_INDEX)
                .setSettings(Settings.builder()
                        .put("index.number_of_shards", 3)
                        .put("index.number_of_replicas", 2))
                .addMapping(_TYPE, "{\n" +
                        "  \"流行\":{\n" +
                        "    \"properties\":{\n" +
                        "      \"发行时间\":{\n" +
                        "        \"type\":\"date\",\n" +
                        "        \"format\":\"yyyy-MM-dd\"\n" +
                        "      }\n" +
                        "    }\n" +
                        "  }\n" +
                        "}")
                .get();
        logger.info("testCreateIndexWithSettingsAndMappings response: " + response.isShardsAcked());
    }

    @Test
    public void testAddMappingToExistIndex() {
        PutMappingResponse response = client.admin().indices().preparePutMapping(_INDEX)
                .setType(_TYPE)
                .setSource("{\n" +
                        "    \"properties\":{\n" +
                        "      \"评论数\":{\n" +
                        "        \"type\":\"long\"\n" +
                        "      }\n" +
                        "    }\n" +
                        "}")
                .get();
        logger.info("testAddMappingToExistIndex response: " + response.isAcknowledged());
    }

    /**
     * 现在不能删除某个type(mapping)了
     * It is no longer possible to delete the mapping for a type. Instead you should delete the index and recreate it with the new mappings.
     */
    public void testDeleteMapping() {
        //http://stackoverflow.com/questions/16546294/how-to-delete-document-types-in-elasticsearch
    }

    @Test
    public void testRefreshIndex() {
        RefreshResponse response = client.admin().indices().prepareRefresh(_INDEX).get();
        logger.info("testRefreshIndex response: " + response.getTotalShards());
    }

}