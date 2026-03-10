package com.knubisoft.testapi.repository.es;

import com.knubisoft.testapi.model.ESNews;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

@ConditionalOnExpression("${es.enabled:true}")
public interface ESNewsRepo extends ElasticsearchRepository<ESNews, String> {

    List<ESNews> findByName(String name);
}
