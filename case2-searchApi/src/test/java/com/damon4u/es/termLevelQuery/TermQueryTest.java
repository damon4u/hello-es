package com.damon4u.es.termLevelQuery;

import com.damon4u.es.BaseTest;
import org.elasticsearch.action.search.SearchResponse;
import org.junit.Test;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Description:
 * 首先要理解term query和match query的区别
 * <p>
 * 这样从字段类型上说起.
 * 对于text类型的字段,例如title,body,content等,属于full text,会使用analyzer分析,拆分成多个terms.
 * 而keyword,date,number等类型的字段,不会经过分析阶段,这个字段的内容会完成的成为一个term.
 * <p>
 * 因此,对于非full text字段,使用term查询,直接就会从倒排索引中查询,不会用到分析器
 *
 * @author damon4u
 * @version 2017-02-23 10:31
 */
public class TermQueryTest extends BaseTest {

    /**
     * 我们可以理解为,这里的查询是字段精确匹配,没有一首歌的名字叫"周杰伦"
     */
    @Test
    public void testTermQueryOnText() {
        SearchResponse response = executeQuery(termQuery("歌曲名称", "周杰伦"));
        logger.info(response.toString());
    }

    /**
     * 由于我们在添加字段mapping时,没有指定"歌手名称"字段的类型,默认识别为string的text类型了
     * 但是会隐含一个"歌手名称.keyword"字段,类型是keyword的
     *
     * 我们也可以在创建索引时,指定一个string字段为keyword类型
     */
    @Test
    public void testTermQuery() {
        SearchResponse response = executeQuery(termQuery("歌手名称.keyword", "周杰伦"));
        logger.info(response.toString());
    }

    /**
     * date类型,同样精确匹配
     */
    @Test
    public void testTermQueryDate() {
        SearchResponse response = executeQuery(termQuery("发行时间", "2012-1-4"));
        logger.info(response.toString());
    }

}
