package org.sopt.snappinserver.domain.question.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.snappinserver.domain.question.domain.enums.QuestionDomain;
import org.sopt.snappinserver.domain.question.domain.exception.QuestionErrorCode;
import org.sopt.snappinserver.domain.question.domain.exception.QuestionException;
import org.sopt.snappinserver.global.entity.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Question extends BaseEntity {

    private static final int MAX_CONTENTS_LENGTH = 200;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionDomain questionDomain;

    @Column(nullable = false)
    private Integer step;

    @Column(nullable = false, length = MAX_CONTENTS_LENGTH)
    private String contents;

    @Builder(access = AccessLevel.PRIVATE)
    private Question(QuestionDomain questionDomain, Integer step, String contents) {
        this.questionDomain = questionDomain;
        this.step = step;
        this.contents = contents;
    }

    public static Question create(QuestionDomain questionDomain, Integer step, String contents) {
        validateQuestion(questionDomain, step, contents);
        return Question.builder()
            .questionDomain(questionDomain)
            .contents(contents)
            .build();
    }

    private static void validateQuestion(
        QuestionDomain questionDomain,
        Integer step,
        String contents
    ) {
        validateQuestionDomainExists(questionDomain);
        validateStepExists(step);
        validateContents(contents);
    }

    private static void validateQuestionDomainExists(QuestionDomain questionDomain) {
        if (questionDomain == null) {
            throw new QuestionException(QuestionErrorCode.QUESTION_DOMAIN_REQUIRED);
        }
    }

    private static void validateStepExists(Integer step) {
        if (step == null) {
            throw new QuestionException(QuestionErrorCode.STEP_REQUIRED);
        }
    }

    private static void validateContents(String contents) {
        validateContentsExists(contents);
        validateContentsLength(contents);
    }

    private static void validateContentsExists(String contents) {
        if (contents == null || contents.isBlank()) {
            throw new QuestionException(QuestionErrorCode.CONTENTS_REQUIRED);
        }
    }

    private static void validateContentsLength(String contents) {
        if (contents.length() > MAX_CONTENTS_LENGTH) {
            throw new QuestionException(QuestionErrorCode.CONTENTS_TOO_LONG);
        }
    }

}
