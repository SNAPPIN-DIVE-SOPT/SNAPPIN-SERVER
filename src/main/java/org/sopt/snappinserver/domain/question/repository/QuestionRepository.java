package org.sopt.snappinserver.domain.question.repository;

import org.sopt.snappinserver.domain.question.domain.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

}
