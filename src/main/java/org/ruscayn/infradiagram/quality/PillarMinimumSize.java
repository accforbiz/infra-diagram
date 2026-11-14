package org.ruscayn.infradiagram.quality;

import java.util.List;
import java.util.Locale;
import org.ruscayn.infradiagram.scene.Scene;

/**
 * 품질 조건 6. 기둥이 정해 둔 최소 크기보다 작게 그려지지 않는다.
 */
final class PillarMinimumSize implements QualityRule {

    private static final double TOLERANCE = 1e-6;

    private final double minRadius;

    PillarMinimumSize(double minRadius) {
        this.minRadius = minRadius;
    }

    @Override
    public List<String> violations(Scene scene) {
        String message = "Pillar '%s' is too small: radius %.1f px, minimum %.1f px.";
        return scene.pillars().stream()
                .filter(pillar -> pillar.radius() < minRadius - TOLERANCE)
                .map(pillar -> String.format(Locale.ROOT, message, pillar.elementId(), pillar.radius(), minRadius))
                .toList();
    }
}
