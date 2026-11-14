package org.ruscayn.infradiagram.quality;

import java.util.List;
import org.ruscayn.infradiagram.scene.PillarPair;
import org.ruscayn.infradiagram.scene.Scene;

/**
 * 품질 조건 1. 기둥의 바닥끼리 겹치지 않는다.
 */
final class FootprintsApart implements QualityRule {

    private static final double TOLERANCE = 1e-6;

    @Override
    public List<String> violations(Scene scene) {
        return PillarPair.allOf(scene.pillars()).stream()
                .filter(pair -> pair.groundGap() < -TOLERANCE)
                .map(this::describe)
                .toList();
    }

    private String describe(PillarPair pair) {
        String message = "Pillars '%s' and '%s' overlap on the ground.";
        return message.formatted(pair.first().elementId(), pair.second().elementId());
    }
}
