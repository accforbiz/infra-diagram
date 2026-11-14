package org.ruscayn.infradiagram.quality;

import java.util.List;
import java.util.Locale;
import org.ruscayn.infradiagram.scene.Scene;

/**
 * 품질 조건 7. 글자가 정해 둔 최소 크기보다 작게 그려지지 않는다.
 */
final class TextMinimumSize implements QualityRule {

    private static final double TOLERANCE = 1e-6;

    private final double minFontSize;

    TextMinimumSize(double minFontSize) {
        this.minFontSize = minFontSize;
    }

    @Override
    public List<String> violations(Scene scene) {
        String message = "Label of '%s' is too small: %.1f px, minimum %.1f px.";
        return scene.labels().stream()
                .filter(label -> label.fontSize() < minFontSize - TOLERANCE)
                .map(label -> String.format(Locale.ROOT, message, label.elementId(), label.fontSize(), minFontSize))
                .toList();
    }
}
