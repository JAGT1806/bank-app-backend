package com.jagt.hexagonal.bankapp.common.infrastructure.web.response;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        String code,
        String message,
        List<String> details,
        LocalDateTime timestamp
) {
}
