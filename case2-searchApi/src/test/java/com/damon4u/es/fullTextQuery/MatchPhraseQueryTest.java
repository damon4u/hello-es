package com.damon4u.es.fullTextQuery;

import com.damon4u.es.BaseTest;
import org.elasticsearch.action.search.SearchResponse;
import org.junit.Test;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Description:
 * match_phrase是短语搜索,亦即它会将给定的短语（phrase）当成一个完整的查询条件.
 * 当使用match_phrase进行搜索的时候,你的结果集中,所有的Document都必须包含你指定的查询词组.
 * 这看起来有点像关系型数据库的like查询操作.
 *
 *
 * match搜索与之不同,match搜索是全文搜索,使用分析器拆分成字段,不包含位置信息,然后全文搜索,只要发现相关的doc,就会放到结果集中.
 *
 * 对于匹配了短语"quick brown fox"的文档，下面的条件必须为true：

     quick，brown和fox必须全部出现在某个字段中。
     brown的位置必须比quick的位置大1。
     fox的位置必须比quick的位置大2。
 * 如果以上的任何条件没有被满足，那么文档就不能被匹配。
 *
 * http://blog.csdn.net/dm_vincent/article/details/41941659
 *
 * @author damon4u
 * @version 2017-02-21 15:31
 */
public class MatchPhraseQueryTest extends BaseTest {

    /**
     * 短语匹配,不能匹配到"周杰伦""杰伦"等,不能拆开,不能更换位置
     */
    @Test
    public void testMatchPhrase() {
        SearchResponse response = executeQuery(
                matchPhraseQuery("歌手名称","周伦")
        );
        logger.info(response.toString());
    }

    /**
     * match是全文匹配,只要包含关键词,就会计算得分
     */
    @Test
    public void testMatch() {
        SearchResponse response = executeQuery(
                matchQuery("歌手名称","周伦")
        );
        logger.info(response.toString());
    }

    /**
     * slop参数控制允许位置差距,这样就能匹配到"周杰伦"了
     */
    @Test
    public void testMatchPhraseWithSlop() {
        SearchResponse response = executeQuery(
                matchPhraseQuery("歌手名称", "周伦")
                        .slop(1)
        );
        logger.info(response.toString());
    }


}
