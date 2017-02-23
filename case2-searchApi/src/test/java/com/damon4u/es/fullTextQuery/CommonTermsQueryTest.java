package com.damon4u.es.fullTextQuery;

import com.damon4u.es.BaseTest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.junit.Test;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Description:
 * common terms查询是为了削弱常用词的影响
 * 类似"the","a","and"等常用词(stopwords),可能会匹配很多doc
 * 为了削弱常用词影响,common terms查询会将term分为两类:常用词和非常用词,分类方法可以根据当前index中的词频
 * 首先匹配非常用词,计算得分
 * 之后进行第二步匹配,仅计算前一步匹配成功doc的得分
 * <p>
 * 如果一个查询仅包含常用词,那么就会构造一个AND查询,要求常用term都存在才算匹配
 * 或者是OR查询,但是要规定一个minimum_should_match参数,大于这个参数才算匹配
 * <p>
 * 分类阈值使用cutoff_frequency参数控制,可以是绝对频率(>=1的值)或者相对频率(0.0 .. 1.0)
 * NOTE: 计算频率时,仅针对当前分片.
 *
 * @author damon4u
 * @version 2017-02-22 15:42
 */
public class CommonTermsQueryTest extends BaseTest {

    /*
        再次申明:计算频率时,仅针对当前分片.
        为了测试这个接口,重新创建了index,只保留一个分片 :p
     */

    /**
     * 现在index里歌曲名称包含下面的记录:
     * I Wanna Know
     * I Wanna Know1
     * I Wanna
     * I
     * 然后将cutoff_frequency设置为4,绝对词频.如果词频(包含term的doc数)小于等于4,都认为是非常用词
     * 默认情况下,非常用词会使用OR查询,只要包含任意一个term,都算是匹配
     * 下面的例子中,就能匹配所有四首歌
     */
    @Test
    public void testCommonTermsQuery() {
        SearchResponse response = executeQuery(
                commonTermsQuery("歌曲名称", "I Wanna Know1")
                        .cutoffFrequency(4F)
        );
        logger.info(response.toString());
    }

    /**
     * 现在把cutoff_frequency调整到比较小的3
     * 此时,"I"和"Wanna"都成为了常用词,而"Know1"是非常用词
     * 首先用非常用词来匹配,这样,歌曲名称为"I",的doc就被排除了
     */
    @Test
    public void testCommonTermsQueryLowCutoff() {
        SearchResponse response = executeQuery(
                commonTermsQuery("歌曲名称", "I Wanna Know1")
                        .cutoffFrequency(3F)
        );
        logger.info(response.toString());
    }


    /**
     * 现在把cutoff_frequency调整到比较小的2
     * 此时,"I"成为了常用词,而"Wanna"和"Know1"是非常用词
     * 首先用非常用词来匹配,这样,歌曲名称为"I","I Wanna"和"I Wanna Know"的doc就都被排除了,
     * 因为它们都不包含非常用词"Know1",仅返回1条记录
     */
    @Test
    public void testCommonTermsQueryMoreLowCutoff() {
        SearchResponse response = executeQuery(
                commonTermsQuery("歌曲名称", "I Wanna Know1")
                        .cutoffFrequency(2F)
        );
        logger.info(response.toString());
    }

    /**
     * 将cutoff_frequency设置为4,这样所有term都是非常用词
     * 之后将非关键词的operator设置为AND
     * 这样只能匹配到"I Wanna Know1"一条记录
     */
    @Test
    public void testCommonTermsQueryALLCommonAnd() {
        SearchResponse response = executeQuery(
                commonTermsQuery("歌曲名称", "I Wanna Know1")
                        .cutoffFrequency(4F)
                        .lowFreqOperator(Operator.AND)
        );
        logger.info(response.toString());
    }

    @Override
    protected SearchResponse executeQuery(QueryBuilder query) {
        return client.prepareSearch(_INDEX_COMMON_TERMS)
                .setTypes(_TYPE)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(query)
                .get();
    }

}
