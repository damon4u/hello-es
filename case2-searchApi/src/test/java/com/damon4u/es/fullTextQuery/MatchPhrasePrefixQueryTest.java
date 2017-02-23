package com.damon4u.es.fullTextQuery;

import com.damon4u.es.BaseTest;
import org.elasticsearch.action.search.SearchResponse;
import org.junit.Test;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Description:
 * match_phrase_prefix也是短语匹配,不同的是,最后一个term支持前缀匹配
 * 这个功能可以支持简单的输入提示(search-as-you-type)
 * 但是,如果想要更智能的提示,这个还无法实现,例如,我输入I Wanna Kn开头的,限制提示数量为2,那么可能会提示"I Wanna Know1","I Wanna Know1",但是没有我想要的"I Wanna Know"
 *
 * 智能提示参考https://www.elastic.co/guide/en/elasticsearch/reference/5.2/search-suggesters-completion.html
 * 和https://www.elastic.co/guide/en/elasticsearch/guide/2.x/_index_time_search_as_you_type.html
 * @author damon4u
 * @version 2017-02-21 21:21
 */
public class MatchPhrasePrefixQueryTest extends BaseTest {

    /**
     * 参数max_expansions控制提示数量
     */
    @Test
    public void testMatchPhrasePrefixQuery() {
        SearchResponse response = executeQuery(
            matchPhrasePrefixQuery("歌曲名称","I Wanna Kn")
                    .maxExpansions(1)
        );
        logger.info(response.toString());
    }

    /**
     * 普通的短语匹配无法匹配,最后一个term不存在
     */
    @Test
    public void testMatchPhrase() {
        SearchResponse response = executeQuery(
                matchPhraseQuery("歌曲名称","I Wanna Kn")
        );
        logger.info(response.toString());
    }
}
