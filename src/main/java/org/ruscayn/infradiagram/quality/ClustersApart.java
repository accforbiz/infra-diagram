package org.ruscayn.infradiagram.quality;

import java.util.List;
import java.util.Locale;
import org.ruscayn.infradiagram.scene.ClusterSpacing;
import org.ruscayn.infradiagram.scene.Scene;

/**
 * 품질 조건 3. 서로 다른 무리는 무리 안의 기둥 사이보다 넓게 떨어진다.
 *
 * 무리 사이의 틈이 무리 안의 틈보다 넓어야, 어느 기둥이 어느 무리에 속하는지 위치만으로 읽힌다.
 */
final class ClustersApart implements QualityRule {

    @Override
    public List<String> violations(Scene scene) {
        ClusterSpacing spacing = ClusterSpacing.of(scene.pillars());
        if (spacing.between() > spacing.within()) {
            return List.of();
        }
        String message = "Clusters are too close: %.1f px between clusters, %.1f px inside a cluster.";
        return List.of(String.format(Locale.ROOT, message, spacing.between(), spacing.within()));
    }
}
