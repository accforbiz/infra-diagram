package org.ruscayn.infradiagram.app;

import java.util.List;

public class PoorQualityException extends RuntimeException {

    private static final int SHOWN = 5;

    public PoorQualityException(List<String> violations) {
        super(describe(violations));
    }

    private static String describe(List<String> violations) {
        StringBuilder message = new StringBuilder("This input cannot be drawn with acceptable quality.");
        violations.stream().limit(SHOWN).forEach(violation -> message.append(System.lineSeparator()).append("  - ").append(violation));
        if (violations.size() > SHOWN) {
            message.append(System.lineSeparator()).append("  - and %d more".formatted(violations.size() - SHOWN));
        }
        return message.toString();
    }
}
