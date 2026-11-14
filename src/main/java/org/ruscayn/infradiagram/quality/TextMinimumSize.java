package org.ruscayn.infradiagram.quality;

import java.util.List;
import java.util.Locale;
import java.util.OptionalDouble;
import org.ruscayn.infradiagram.scene.LabelMark;
import org.ruscayn.infradiagram.scene.Scene;

/**
 * 품질 조건 7. 글자가 정해 둔 최소 크기보다 작게 그려지지 않는다.
 *
 * 한 그림의 글자는 모두 같은 크기이므로, 가장 작은 글자 하나만 알린다.
 */
final class TextMinimumSize implements QualityRule {

    private static final double TOLERANCE = 1e-6;

    private final double minFontSize;

    TextMinimumSize(double minFontSize) {
        this.minFontSize = minFontSize;
    }

    @Override
    public List<String> violations(Scene scene) {
        OptionalDouble smallest = scene.labels().stream().mapToDouble(LabelMark::fontSize).min();
        if (smallest.isEmpty() || smallest.getAsDouble() >= minFontSize - TOLERANCE) {
            return List.of();
        }
        String message = "Labels would be drawn %.1f px high. The minimum is %.1f px.";
        return List.of(String.format(Locale.ROOT, message, smallest.getAsDouble(), minFontSize));
    }
}
