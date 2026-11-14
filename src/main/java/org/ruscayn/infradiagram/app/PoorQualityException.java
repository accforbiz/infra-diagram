package org.ruscayn.infradiagram.app;

import java.util.List;

/**
 * 그려 본 결과가 품질 조건을 어겨서 그림을 저장하지 않았음을 알린다.
 *
 * 상한 안의 입력이라도 요소와 글자가 많으면 그림 전체가 줄어들어 글자를 읽을 수 없게 된다.
 * 그런 그림을 내놓는 대신, 무엇이 얼마나 모자라는지와 고치는 방법, 항상 그려지는 범위를 알린다.
 */
public class PoorQualityException extends RuntimeException {

    private static final int SHOWN = 5;
    private static final String ADVICE = "Use fewer elements, fewer labels, or shorter labels.";

    public PoorQualityException(List<String> violations) {
        super(describe(violations));
    }

    private static String describe(List<String> violations) {
        StringBuilder message = new StringBuilder("This input cannot be drawn with acceptable quality.");
        violations.stream().limit(SHOWN).forEach(violation -> line(message, "  - " + violation));
        if (violations.size() > SHOWN) {
            line(message, "  - and %d more".formatted(violations.size() - SHOWN));
        }
        line(message, ADVICE);
        return line(message, GuaranteedRange.standard().describe()).toString();
    }

    private static StringBuilder line(StringBuilder message, String text) {
        return message.append(System.lineSeparator()).append(text);
    }
}
