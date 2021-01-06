package cn.xy.utils.third;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

/**
  * @description: mongodb util
  * @author: xy
  */
@Component
public class MongoUtil {
    @Autowired
    private MongoTemplate mongoTemplate;
}
