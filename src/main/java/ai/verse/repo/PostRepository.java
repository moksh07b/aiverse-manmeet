package ai.verse.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    @Query("SELECT u.sentiment as sentiment, count(u.sentiment) as count FROM PostEntity as u group by u.sentiment")
    List findAggregateData();

    @Query("SELECT u FROM PostEntity u WHERE u.sentiment is null")
    List<PostEntity> findRowsWithNoSentiment();

}