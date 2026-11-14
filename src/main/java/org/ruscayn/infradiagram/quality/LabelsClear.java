package org.ruscayn.infradiagram.quality;

import java.util.ArrayList;
import java.util.List;
import org.ruscayn.infradiagram.scene.LabelMark;
import org.ruscayn.infradiagram.scene.Pillar;
import org.ruscayn.infradiagram.scene.Scene;

/**
 * 품질 조건 4. 글자는 기둥이나 다른 글자와 겹치지 않는다.
 *
 * 글자와 색 점을 감싼 사각형이 기둥의 그림이나 다른 글자의 사각형과 겹치는지 본다.
 * 화살표는 기둥을 가리키려고 기둥 쪽으로 뻗는 것이어서 비교에 넣지 않는다.
 */
final class LabelsClear implements QualityRule {

    @Override
    public List<String> violations(Scene scene) {
        List<String> found = new ArrayList<>();
        List<LabelMark> labels = scene.labels();
        for (int index = 0; index < labels.size(); index++) {
            LabelMark label = labels.get(index);
            found.addAll(overPillars(label, scene.pillars()));
            found.addAll(overLabels(label, labels.subList(index + 1, labels.size())));
        }
        return found;
    }

    private List<String> overPillars(LabelMark label, List<Pillar> pillars) {
        return pillars.stream()
                .filter(pillar -> label.captionBox().overlaps(pillar.bounds()))
                .map(pillar -> "Label of '%s' overlaps pillar '%s'.".formatted(label.elementId(), pillar.elementId()))
                .toList();
    }

    private List<String> overLabels(LabelMark label, List<LabelMark> others) {
        return others.stream()
                .filter(other -> label.captionBox().overlaps(other.captionBox()))
                .map(other -> "Labels of '%s' and '%s' overlap.".formatted(label.elementId(), other.elementId()))
                .toList();
    }
}
