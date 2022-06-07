package cn.annna.elasticsearch;

import cn.annna.entity.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserRepository extends ElasticsearchRepository<User,Integer> {
}
