package cn.annna.elasticsearch;

import cn.annna.entity.Video;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface VideoRepository extends ElasticsearchRepository<Video,Integer> {
}
