package org.sopt.snappinserver.global.util;

import org.sopt.snappinserver.global.enums.SortType;

public record ParsedCursor(Long cursorId, Long cursorLikeCount, Double cursorAvgRating) {

    public static ParsedCursor of(String cursor, SortType sort) {
        if (cursor == null) {
            return new ParsedCursor(null, null, null);
        }
        return switch (sort) {
            case LATEST -> new ParsedCursor(Long.parseLong(cursor), null, null);
            case POPULAR -> {
                String[] parts = cursor.split(":");
                yield new ParsedCursor(
                    Long.parseLong(parts[1]),
                    Long.parseLong(parts[0]),
                    null
                );
            }
            case RECOMMENDED -> {
                String[] parts = cursor.split(":");
                Double avgRating =
                    "null".equals(parts[0]) ? null : Double.parseDouble(parts[0]);
                yield new ParsedCursor(
                    Long.parseLong(parts[1]),
                    null,
                    avgRating
                );
            }
        };
    }
}
