package com.damon4u.es.fullTextQuery;

import com.damon4u.es.BaseTest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.junit.Test;

import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

/**
 * Description:
 *
 * @author damon4u
 * @version 2017-02-21 21:45
 */
public class MultiMatchQueryTest extends BaseTest {

    /**
     * 从多个字段中做全文匹配,第一个参数是查询文本,后面是字段名称
     * 可以把歌曲名称为"你好,周杰伦"和歌手名称为"周杰伦"的doc找出来
     */
    @Test
    public void testMultiMatch() {
        SearchResponse response = executeQuery(
                multiMatchQuery("杰伦","歌曲名称","歌手名称")
        );
        logger.info(response.toString());
    }

    /**
     * 字段属性可以使用通配符
     * 下面的例子,可以从"歌曲名称"和"歌手名称"中搜索
     */
    @Test
    public void testMultiMatchWithWildcards() {
        SearchResponse response = executeQuery(
                multiMatchQuery("杰伦","*名称")
        );
        logger.info(response.toString());
    }

    /**
     * boost参数可以控制某个字段的权重,计算得分时,会用原始得分乘以该字段的boost
     * 下面的例子中,歌手名称为"杰伦"的权重要大于歌曲名称为"杰伦"的权重
     */
    @Test
    public void testMultiMatchWithBoost() {
        SearchResponse response = executeQuery(
                multiMatchQuery("杰伦")
                        .field("歌手名称", 3)//歌手名称得分乘以3
                        .field("歌曲名称")
        );
        logger.info(response.toString());
    }

    /**
     * 默认情况下,多字段匹配使用best_fields模式
     * 该模式下,内部构造一个dis_max查询,选择所有字段中,得分最高的那个作为doc的总得分
     * 使用tier_breaker来乘以doc中其它匹配字段的得分加上最高得分作为总得分
     * 默认情况下tie_breaker的值为0.0F,也就是忽略其它匹配字段的得分,指取最高的
     */
    @Test
    public void testMultiMatchBestFields() {
        SearchResponse response = executeQuery(
                multiMatchQuery("杰伦")
                        .field("歌手名称", 3)//歌手名称得分乘以3
                        .field("歌曲名称")
                .type(MultiMatchQueryBuilder.Type.BEST_FIELDS)
        );
        logger.info(response.toString());
    }

    /**
     * most_field模式
     * 该模式下,会将所有匹配字段的得分求和,作为总得分
     * 这种模式适用于一个属性使用多个字段来表达
     * 例如,一个姓名字段,辅助一个拼音字段,再辅助一个昵称字段
     * 这样,三个字段匹配得分求和来衡量相关性
     *
     */
    @Test
    public void testMultiMatchMostFields() {
        SearchResponse response = executeQuery(
                multiMatchQuery("杰伦")
                        .field("歌手名称", 3)//歌手名称得分乘以3
                        .field("歌曲名称")
                        .type(MultiMatchQueryBuilder.Type.MOST_FIELDS)
        );
        logger.info(response.toString());
    }

    /*
    !!!! NOTE:
        对于best_fields和most_fields模式,operator和minimum_should_match参数是针对每个字段的
        有时,我们不想这样:
        例如,有两个字段first_name和last_name,我们要匹配"Will Smith"
        我们的意图其实是想让Will匹配first_name,Smith匹配last_name,
        but!如果我们将operator设置为AND,那完了,由于operator针对单个字段,真是执行的查询是:
        (+first_name:will +first_name:smith) | (+last_name:will  +last_name:smith)
        结果却查不到
        为了解决上面的问题,一种方案是添加一个字段,full_name
        另一种方案是使用下面的cross_fields
     */

    /**
     * cross_fields模式
     * 该模式下,In other words, all terms must be present in at least one field for a document to match.
     * +(first_name:will  last_name:will) +(first_name:smith last_name:smith)
     *
     * 该模式会将具有相同分析器的字段分为一组,不同组之间,还是存在上面的operator参数问题,
     * 所以具有联合意义的字段,还是设置相同分析器吧
     */
    @Test
    public void testMultiMatchCrossFields() {
        SearchResponse response = executeQuery(
                multiMatchQuery("周杰伦")
                        .field("歌手名称", 3)//歌手名称得分乘以3
                        .field("歌曲名称")
                        .type(MultiMatchQueryBuilder.Type.CROSS_FIELDS)
        );
        logger.info(response.toString());
    }

    /**
     * phrase和phrase_prefix模式
     * 这种模式规则与best_fields模式相同,不同的是,内部构造短语匹配,而不是全文匹配
     * 下面的例子,歌手名称为"杰伦"的doc就不会被匹配出来
     */
    @Test
    public void testMultiMatchPhrase() {
        SearchResponse response = executeQuery(
                multiMatchQuery("周杰伦")
                        .field("歌手名称", 3)//歌手名称得分乘以3
                        .field("歌曲名称")
                        .type(MultiMatchQueryBuilder.Type.PHRASE)
        );
        logger.info(response.toString());
    }

}
