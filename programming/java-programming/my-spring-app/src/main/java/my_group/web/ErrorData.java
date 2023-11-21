package my_group.web;

import java.util.Optional;

public record ErrorData(String code, Optional<String> message)
{
    public ErrorData(String code) {
        this(code, Optional.empty());
    }

    public ErrorData(String code, String message) {
        this(code, Optional.of(message));
    }
}
