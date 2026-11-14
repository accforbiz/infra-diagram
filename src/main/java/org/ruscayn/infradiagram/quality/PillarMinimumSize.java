package org.ruscayn.infradiagram.quality;

import java.util.List;
import java.util.Locale;
import java.util.OptionalDouble;
import org.ruscayn.infradiagram.scene.Pillar;
import org.ruscayn.infradiagram.scene.Scene;

/**
 * 품질 조건 6. 기둥이 정해 둔 최소 크기보다 작게 그려지지 않는다.
 *
 * 기둥이 많으면 같은 안내가 수십 줄 나오므로, 가장 작은 기둥 하나만 알린다.
 */
final class PillarMinimumSize implements QualityRule {

    private static final double TOLERANCE = 1e-6;

    private final double minRadius;

    PillarMinimumSize(double minRadius) {
        this.minRadius = minRadius;
    }

    @Override
    public List<String> violations(Scene scene) {
        OptionalDouble smallest = scene.pillars().stream().mapToDouble(Pillar::radius).min();
        if (smallest.isEmpty() || smallest.getAsDouble() >= minRadius - TOLERANCE) {
            return List.of();
        }
        String message = "The smallest pillar would be drawn with a radius of %.1f px. The minimum is %.1f px.";
        return List.of(String.format(Locale.ROOT, message, smallest.getAsDouble(), minRadius));
    }
}
