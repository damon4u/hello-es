package com.damon4u.es.fullTextQuery;

import com.damon4u.es.BaseTest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.Operator;
import org.junit.Test;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

/**
 * Description:
 * match查询
 * 使用分析器分析文本,然后构造一个boolean查询
 *  operator参数可以设置为OR活着AND,默认是OR,用来控制boolean子句的关系
 *  minimum_should_match参数用来控制should子句的最小匹配数
 *  analyzer参数用来控制分析器的类型,默认是使用mapping中指定的分析器,或者是默认分析器
 *  lenient参数设为true用来忽略类型不匹配异常,默认是false
 *  zero_terms_query参数:如果分析器将文本中的token全部移除,那么默认情况是没有匹配的doc,为了改变这种行为,可以将这个参数设置为all,对应matchAll查询
 *
 *  cutoff_frequency
 *  这个参数可以用来动态限制高频词被匹配的可能性
 *  这设置一个绝对值或者相对值来作为词频阈值
 *  如果term词频高于阈值,那么只有当低频term匹配上了(如果是OR,那么保证至少一个低频匹配;如果是AND,那么要保证所有低频term都匹配),才会计算高频term的得分
 *  这样就能保证优先考虑低频term匹配
 *
 * @author damon4u
 * @version 2017-02-21 11:32
 */
public class MatchQueryTest extends BaseTest {

    /**
     * 文本分析器,也能匹配到"杰伦",不过得分会低一些
     */
    @Test
    public void testMatchText() {
        SearchResponse response = executeQuery(
                matchQuery("歌手名称","周杰伦")
        );
        logger.info(response.toString());
    }

    /**
     * 使用date分析器,只能精确匹配,不能匹配2015-12-11这种
     */
    @Test
    public void testMatchDate() {
        SearchResponse response = executeQuery(
                matchQuery("发行时间","2015-12-12")
        );
        logger.info(response.toString());
    }

    /**
     * 使用lenient参数忽略格式转换异常
     */
    @Test
    public void testMatchLenient() {
        SearchResponse response = executeQuery(
                matchQuery("发行时间","2015:12-12")
                .lenient(true)
        );
        logger.info(response.toString());
    }

    /**
     * 对于全文匹配,首先会使用分析器将查询文本拆分成term,然后根据operator参数,构造一个boolean查询
     * operator默认值是OR,也就是说,只要包含任何一个term的doc都会计算得分加入到结果集
     * 下面的例子中,将operator设置为AND,那么需要doc包含所有term
     * 对于歌手名称为"杰伦"的doc,不包含"周",不满足匹配规则
     */
    @Test
    public void testMatchAND() {
        SearchResponse response = executeQuery(
                matchQuery("歌手名称", "周杰伦")
                        .operator(Operator.AND)
        );
        logger.info(response.toString());
    }

}
