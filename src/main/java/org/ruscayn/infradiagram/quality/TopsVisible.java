package org.ruscayn.infradiagram.quality;

import java.util.ArrayList;
import java.util.List;
import org.ruscayn.infradiagram.scene.Pillar;
import org.ruscayn.infradiagram.scene.PillarPair;
import org.ruscayn.infradiagram.scene.Scene;
import org.ruscayn.infradiagram.scene.ScreenPoint;

/**
 * 품질 조건 2. 모든 기둥의 윗면이 가려지지 않는다.
 *
 * 두 기둥 가운데 앞에 있는 기둥이 뒤에 있는 기둥의 윗면을 덮는지 본다.
 * 윗면은 타원으로 보고, 타원의 가운데와 둘레 위의 점 24개를 골라
 * 그 점이 앞 기둥의 그림 안에 들어가는지 확인한다.
 * 각기둥의 윗면은 이 타원 안에 있으므로, 타원이 보이면 각기둥의 윗면도 보인다.
 */
final class TopsVisible implements QualityRule {

    private static final int RIM_SAMPLES = 24;

    @Override
    public List<String> violations(Scene scene) {
        return PillarPair.allOf(scene.pillars()).stream()
                .filter(pair -> hides(pair.front(), pair.back()))
                .map(this::describe)
                .toList();
    }

    private boolean hides(Pillar front, Pillar back) {
        return topSamples(back).stream().anyMatch(front::covers);
    }

    private List<ScreenPoint> topSamples(Pillar pillar) {
        ScreenPoint center = pillar.topCenter();
        List<ScreenPoint> samples = new ArrayList<>(List.of(center));
        for (int index = 0; index < RIM_SAMPLES; index++) {
            double angle = 2 * Math.PI * index / RIM_SAMPLES;
            samples.add(center.moved(pillar.radius() * Math.cos(angle), pillar.depthRadius() * Math.sin(angle)));
        }
        return samples;
    }

    private String describe(PillarPair pair) {
        String message = "Top of pillar '%s' is hidden by pillar '%s'.";
        return message.formatted(pair.back().elementId(), pair.front().elementId());
    }
}
