package org.sopt.snappinserver.domain.curation.repository;

import static org.sopt.snappinserver.domain.curation.domain.entity.QCuration.curation;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CurationRepositoryImpl implements CurationRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Long> findTop3MoodIdsByUserId(Long userId) {
        return jpaQueryFactory
            .select(curation.mood.id)
            .from(curation)
            .where(curation.user.id.eq(userId))
            .orderBy(curation.createdAt.desc())
            .limit(3)
            .fetch();
    }
}
