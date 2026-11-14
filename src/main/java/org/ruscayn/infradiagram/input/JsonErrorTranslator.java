package org.ruscayn.infradiagram.input;

import java.util.List;
import java.util.stream.Collectors;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.exc.MismatchedInputException;
import tools.jackson.databind.exc.UnrecognizedPropertyException;

/**
 * JSON을 읽다가 난 오류를 사람이 고칠 수 있는 안내문으로 바꾼다.
 *
 * Jackson의 오류 글에는 자바 클래스 이름과 설정 이름이 섞여 있어 입력을 쓰는 사람이 읽기 어렵다.
 * 자주 나는 두 가지는 직접 쓴 글로 바꾼다.
 * - 모르는 항목: 항목 이름과 쓸 수 있는 항목을 알린다.
 * - 값의 종류가 틀림: 어떤 종류의 값이 와야 하는지 알린다. 예: "id": 5는 "text in double quotes"가 와야 한다.
 * 나머지(문법 오류 등)는 Jackson의 글에서 내부 정보를 떼어 내고 쓴다.
 */
final class JsonErrorTranslator {

    private static final String SOURCE_NOTE = "\\s*\\(for \\w+ starting at \\[Source:[^\\]]*\\]\\)";

    private final JsonErrorPlace place = new JsonErrorPlace();

    InvalidInputException translate(JacksonException error) {
        return new InvalidInputException(describe(error));
    }

    private String describe(JacksonException error) {
        if (error instanceof UnrecognizedPropertyException unknown) {
            return "Unknown field '%s'%s. Allowed fields: %s."
                    .formatted(unknown.getPropertyName(), place.of(error), allowedFields(unknown));
        }
        if (error instanceof MismatchedInputException mismatch && mismatch.getTargetType() != null) {
            return "Wrong kind of value%s: expected %s.".formatted(place.of(error), expected(mismatch.getTargetType()));
        }
        return "Invalid JSON%s: %s".formatted(place.of(error), error.getOriginalMessage().replaceAll(SOURCE_NOTE, ""));
    }

    private String allowedFields(UnrecognizedPropertyException error) {
        return error.getKnownPropertyIds().stream()
                .map(String::valueOf)
                .sorted()
                .collect(Collectors.joining(", "));
    }

    private String expected(Class<?> type) {
        if (type == String.class) {
            return "text in double quotes";
        }
        if (type == Boolean.class) {
            return "true or false";
        }
        return List.class.isAssignableFrom(type) ? "a list in square brackets" : "an object in curly braces";
    }
}
