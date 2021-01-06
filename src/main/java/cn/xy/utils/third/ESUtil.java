package cn.xy.utils.third;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
  * @description: elasticsearch util, 使用的是rest-high-level-client
  * @author: xy
  */
@Component
public class ESUtil {
    @Autowired
    private RestHighLevelClient restClient;
}
