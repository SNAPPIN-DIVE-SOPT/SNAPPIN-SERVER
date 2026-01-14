package org.sopt.snappinserver.domain.reservation.repository;

import static org.sopt.snappinserver.domain.place.domain.entity.QPlace.place;
import static org.sopt.snappinserver.domain.reservation.domain.entity.QReservation.reservation;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.place.domain.entity.Place;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ReservationRepositoryImpl implements ReservationRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Place> findTop5MostReservedPlacesInLastMonth() {
        Instant oneMonthAgo =
            LocalDateTime.now()
                .minusMonths(1)
                .atZone(ZoneId.systemDefault())
                .toInstant();

        return jpaQueryFactory
            .select(reservation.place)
            .from(reservation)
            .join(reservation.place, place)
            .where(
                reservation.createdAt.goe(oneMonthAgo)
            )
            .groupBy(reservation.place)
            .orderBy(reservation.count().desc())
            .limit(5)
            .fetch();
    }
}
