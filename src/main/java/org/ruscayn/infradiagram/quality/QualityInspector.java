package org.ruscayn.infradiagram.quality;

import java.util.List;
import org.ruscayn.infradiagram.scene.Scene;
import org.ruscayn.infradiagram.settings.CanvasSettings;
import org.ruscayn.infradiagram.settings.QualityThresholds;

/**
 * "PPT에 넣을 만한 퀄리티"를 일곱 가지 조건으로 검사한다. 어긴 조건이 없으면 빈 목록을 돌려준다.
 */
public final class QualityInspector {

    private final List<QualityRule> rules;

    public QualityInspector(List<QualityRule> rules) {
        this.rules = List.copyOf(rules);
    }

    public static QualityInspector standard(CanvasSettings canvas, QualityThresholds thresholds) {
        return new QualityInspector(List.of(
                new FootprintsApart(),
                new TopsVisible(),
                new ClustersApart(),
                new LabelsClear(),
                new InsideCanvas(canvas.margin()),
                new PillarMinimumSize(thresholds.minPillarRadius()),
                new TextMinimumSize(thresholds.minFontSize())));
    }

    public List<String> inspect(Scene scene) {
        return rules.stream().flatMap(rule -> rule.violations(scene).stream()).toList();
    }
}
