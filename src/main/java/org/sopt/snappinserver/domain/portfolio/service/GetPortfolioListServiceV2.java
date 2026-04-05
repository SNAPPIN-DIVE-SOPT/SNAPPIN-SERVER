package org.sopt.snappinserver.domain.portfolio.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.mood.domain.entity.Mood;
import org.sopt.snappinserver.domain.mood.domain.enums.MoodCategory;
import org.sopt.snappinserver.domain.mood.repository.MoodRepository;
import org.sopt.snappinserver.global.enums.SortType;
import org.sopt.snappinserver.domain.portfolio.repository.PortfolioRepositoryCustom;
import org.sopt.snappinserver.domain.portfolio.service.dto.request.GetPortfolioListQueryV2;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPortfolioCardResultV2;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPortfolioListMetaV2;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPortfolioListResultV2;
import org.sopt.snappinserver.domain.portfolio.service.usecase.GetPortfolioListUseCaseV2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GetPortfolioListServiceV2 implements GetPortfolioListUseCaseV2 {

    private static final int PAGE_SIZE = 30;

    private final MoodRepository moodRepository;
    private final PortfolioRepositoryCustom portfolioRepositoryCustom;

    @Value("${cloud.aws.cloud-front.domain}")
    private String cloudFrontDomain;

    @Override
    public GetPortfolioListResultV2 getPortfolioList(GetPortfolioListQueryV2 query) {
        Map<MoodCategory, List<Long>> moodGroupMap = groupByCategory(query.moodIds());

        List<GetPortfolioCardResultV2> rows = portfolioRepositoryCustom
            .findPortfolioCardsV2(query, moodGroupMap, PAGE_SIZE).stream()
            .map(result -> {
                String imageUrl = (result.imageUrl() != null && !result.imageUrl().isBlank())
                    ? cloudFrontDomain + result.imageUrl()
                    : null;

                return new GetPortfolioCardResultV2(
                    result.id(),
                    imageUrl,
                    result.isLiked(),
                    result.likeCount(),
                    result.averageRating()
                );
            })
            .toList();

        boolean hasNext = rows.size() > PAGE_SIZE;
        List<GetPortfolioCardResultV2> portfolios = hasNext ? rows.subList(0, PAGE_SIZE) : rows;
        String nextCursor = hasNext
            ? buildNextCursor(portfolios.get(portfolios.size() - 1), query.sort())
            : null;

        return new GetPortfolioListResultV2(portfolios,
            new GetPortfolioListMetaV2(hasNext, nextCursor));
    }

    private String buildNextCursor(GetPortfolioCardResultV2 last, SortType sort) {
        SortType resolved = sort == null ? SortType.RECOMMENDED : sort;
        return switch (resolved) {
            case LATEST -> String.valueOf(last.id());
            case POPULAR -> last.likeCount() + ":" + last.id();
            case RECOMMENDED -> last.averageRating() + ":" + last.id();
        };
    }

    private Map<MoodCategory, List<Long>> groupByCategory(List<Long> moodIds) {
        if (moodIds == null || moodIds.isEmpty()) {
            return Map.of();
        }

        return moodRepository.findAllById(moodIds).stream()
            .collect(
                Collectors.groupingBy(
                    Mood::getCategory,
                    Collectors.mapping(Mood::getId, toList())
                )
            );
    }
}
