package org.ruscayn.infradiagram.quality;

import java.util.List;
import java.util.stream.Stream;
import org.ruscayn.infradiagram.scene.Box;
import org.ruscayn.infradiagram.scene.Scene;

/**
 * 품질 조건 5. 모든 기둥과 글자가 여백을 두고 그림 안에 들어온다.
 */
final class InsideCanvas implements QualityRule {

    private static final double TOLERANCE = 1e-6;

    private final double margin;

    InsideCanvas(double margin) {
        this.margin = margin;
    }

    @Override
    public List<String> violations(Scene scene) {
        Box frame = new Box(0, 0, scene.width(), scene.height()).shrunkBy(margin - TOLERANCE);
        Stream<String> pillars = scene.pillars().stream()
                .filter(pillar -> !frame.contains(pillar.bounds()))
                .map(pillar -> "Pillar '%s' reaches outside the picture margin.".formatted(pillar.elementId()));
        Stream<String> labels = scene.labels().stream()
                .filter(label -> !frame.contains(label.bounds()))
                .map(label -> "Label of '%s' reaches outside the picture margin.".formatted(label.elementId()));
        return Stream.concat(pillars, labels).toList();
    }
}
