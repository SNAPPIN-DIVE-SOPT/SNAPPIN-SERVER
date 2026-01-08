package org.sopt.snappinserver.domain.review.repository;

import java.util.List;
import org.sopt.snappinserver.domain.review.domain.entity.ReviewPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewPhotoRepository extends JpaRepository<ReviewPhoto, Long> {

    @Query("""
            select reviewPhoto
            from ReviewPhoto reviewPhoto
            join fetch reviewPhoto.photo
            where reviewPhoto.review.id in :reviewIds
            order by reviewPhoto.review.id, reviewPhoto.displayOrder
        """)
    List<ReviewPhoto> findAllByReviewIds(
        @Param("reviewIds") List<Long> reviewIds
    );
}
