package com.talhaunal.backend.repository.elasticsearch;

import com.talhaunal.backend.domain.model.MentorDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface MentorDocumentRepository extends ElasticsearchRepository<MentorDocument, Long> {

    @Query("{\"bool\": {\"should\": ["
            + "{\"match\": {\"description\": {\"query\": \"?0\", \"operator\": \"and\"}}},"
            + "{\"match_phrase\": {\"description\": \"?0\"}},"
            + "{\"query_string\": {\"query\": \"*?0*\", \"default_field\": \"description\"}}"
            + "]}}")
    List<MentorDocument> findByDescriptionContaining(String description);
}
