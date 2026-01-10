package org.sopt.snappinserver.domain.question.repository;

import java.util.Optional;
import org.sopt.snappinserver.domain.question.domain.entity.Question;
import org.sopt.snappinserver.domain.question.domain.enums.QuestionDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    Optional<Question> findByQuestionDomainAndStep(QuestionDomain questionDomain, Integer step);

}
