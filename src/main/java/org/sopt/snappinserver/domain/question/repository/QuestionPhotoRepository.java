package org.sopt.snappinserver.domain.question.repository;

import java.util.List;
import org.sopt.snappinserver.domain.question.domain.entity.Question;
import org.sopt.snappinserver.domain.question.domain.entity.QuestionPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionPhotoRepository extends JpaRepository<QuestionPhoto, Long> {

    List<QuestionPhoto> findAllByQuestion(Question question);
}
