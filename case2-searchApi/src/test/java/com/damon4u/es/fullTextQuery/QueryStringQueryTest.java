package com.damon4u.es.fullTextQuery;

import com.damon4u.es.BaseTest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.Operator;
import org.junit.Test;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Description:
 * 使用query parse解析查询文本,然后构造查询
 * query:可以被解析的查询文本,使用独特的Query string syntax.
 * default_field:指定默认的查询字段,如果query中没有特别指定,就使用这个字段查询
 * default_operator:指定默认的operator,如果query中没有指定,就使用这个operator
 *
 * @author damon4u
 * @version 2017-02-22 23:01
 */
public class QueryStringQueryTest extends BaseTest {

    /**
     * 一个简单的全文匹配
     * 查询文本不包含字段和operator信息,全部使用默认
     * 默认的查询字段为_all
     * 默认的operator为or
     */
    @Test
    public void testDefaultQuery() {
        SearchResponse response = executeQuery(
                queryStringQuery("周杰伦")
        );
        logger.info(response.toString());
    }

    /**
     * 指定默认查询字段,如果query中不包含字段信息,使用这个指定字段
     * 下面的例子中,不会匹配歌曲名称字段
     */
    @Test
    public void testDefaultFieldQuery() {
        SearchResponse response = executeQuery(
                queryStringQuery("周杰伦")
                        .defaultField("歌手名称")
        );
        logger.info(response.toString());
    }

    /**
     * 如果query中指定了字段,例如下面使用":"分隔,指定从歌曲名称中查询
     */
    @Test
    public void testDefaultFieldWithQuery() {
        SearchResponse response = executeQuery(
                queryStringQuery("歌曲名称:周杰伦")
                        .defaultField("歌手名称")
                        .defaultOperator(Operator.AND)
        );
        logger.info(response.toString());
    }

    /**
     * 除了指定默认匹配字段,还可以指定多个(默认)匹配字段
     * 当然,如果query包含字段信息,依然会以query为主
     */
    @Test
    public void testMultiFieldQuery() {
        SearchResponse response = executeQuery(
                queryStringQuery("周杰伦")
                        .field("歌曲名称")
                        .field("歌手名称")
                        .defaultField("歌手名称")
                        .defaultOperator(Operator.AND)
        );
        logger.info(response.toString());
    }

    /**
     * 指定字段名称
     * 格式为 [filed_name:value]
     *
     * 注意会以query中指定的字段为主
     */
    @Test
    public void testQueryFiledName() {
        SearchResponse response = executeQuery(
                queryStringQuery("歌手名称:周杰伦")
                        .field("歌曲名称")
                        .field("歌手名称")
                        .defaultField("歌手名称")
        );
        logger.info(response.toString());
    }

    /**
     * 指定operator
     * 默认的operator为OR
     * 可以在query中手动指定为AND
     *
     * 下面的例子,歌手名称需要包含"周",并且包含"杰"或者"伦"
     */
    @Test
    public void testQueryAND() {
        SearchResponse response = executeQuery(
                queryStringQuery("歌手名称:(周 AND (杰 OR 伦))")
        );
        logger.info(response.toString());
    }

    /**
     * 字段匹配
     * 格式 [field_name:"value"]
     * 使用引号括起来,那么就会认为是一个短语,不会拆开
     */
    @Test
    public void testQueryPhrase() {
        SearchResponse response = executeQuery(
                queryStringQuery("歌手名称:\"周杰伦\"")
        );
        logger.info(response.toString());
    }

    /**
     * 字段可以使用通配符
     * 注意需要使用\转义
     */
    @Test
    public void testWildcardField() {
        SearchResponse response = executeQuery(
                queryStringQuery("\\*名称:\"周杰伦\"")
        );
        logger.info(response.toString());
    }

    /**
     * 检查某个字段不为空
     * 格式 [_exists_:field_name]
     */
    @Test
    public void testExists() {
        SearchResponse response = executeQuery(
                queryStringQuery("_exists_:评论数")
        );
        logger.info(response.toString());
    }

    /**
     * All days in 2012
     *  date:[2012-01-01 TO 2012-12-31]
     * Numbers 1..5
     *  count:[1 TO 5]
     * Numbers from 10 upwards
     *  count:[10 TO *]
     * Dates before 2012
     *  date:{* TO 2012-01-01}
     * Numbers from 1 up to but not including 5
     *  count:[1 TO 5}
     */
    @Test
    public void testRange() {
        SearchResponse response = executeQuery(
                queryStringQuery("发行时间:[2012-1-1 TO 2012-1-3]")
        );
        logger.info(response.toString());
    }

    /**
     * 简化版比较
     * 使用AND拼接
     */
    @Test
    public void testRangeSimplified() {
        SearchResponse response = executeQuery(
                queryStringQuery("发行时间:(+>=2012-1-1 +<2012-1-3)")
        );
        logger.info(response.toString());
    }

}
