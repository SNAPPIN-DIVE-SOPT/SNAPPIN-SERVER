package org.sopt.snappinserver.domain.portfolio.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.mood.domain.entity.Mood;
import org.sopt.snappinserver.domain.mood.domain.enums.MoodCategory;
import org.sopt.snappinserver.domain.mood.repository.MoodRepository;
import org.sopt.snappinserver.domain.portfolio.repository.PortfolioRepositoryCustom;
import org.sopt.snappinserver.domain.portfolio.service.dto.request.GetPortfolioListQuery;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPortfolioCardResult;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPortfolioListMeta;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPortfolioListResult;
import org.sopt.snappinserver.domain.portfolio.service.usecase.GetPortfolioListUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GetPortfolioListService implements GetPortfolioListUseCase {

    private static final int PAGE_SIZE = 30;

    private final MoodRepository moodRepository;
    private final PortfolioRepositoryCustom portfolioRepositoryCustom;

    @Value("${cloud.aws.cloud-front.domain}")
    private String cloudFrontDomain;

    @Override
    public GetPortfolioListResult getPortfolioList(GetPortfolioListQuery query) {
        Map<MoodCategory, List<Long>> moodGroupMap = groupByCategory(query.moodIds());
        List<GetPortfolioCardResult> rows = portfolioRepositoryCustom
            .findPortfolioCards(query.cursor(), query, moodGroupMap, PAGE_SIZE).stream()
            .map(result -> {
                String presignedUrl = (result.imageUrl() != null && !result.imageUrl().isBlank())
                    ? cloudFrontDomain + result.imageUrl()
                    : null;

                return new GetPortfolioCardResult(
                    result.id(),
                    presignedUrl
                );
            })
            .toList();

        boolean hasNext = rows.size() > PAGE_SIZE;
        List<GetPortfolioCardResult> portfolios = hasNext ? rows.subList(0, PAGE_SIZE) : rows;
        Long nextCursor = hasNext ? portfolios.get(portfolios.size() - 1).id() : null;

        return new GetPortfolioListResult(
            portfolios,
            new GetPortfolioListMeta(hasNext, nextCursor)
        );
    }

    private Map<MoodCategory, List<Long>> groupByCategory(List<Long> moodIds) {
        if (moodIds == null || moodIds.isEmpty()) {
            return Map.of();
        }

        return moodRepository.findAllById(moodIds).stream()
            .collect(
                Collectors.groupingBy(
                    Mood::getCategory,
                    Collectors.mapping(
                        Mood::getId,
                        toList()
                    )
                )
            );
    }

}
