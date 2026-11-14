package org.ruscayn.infradiagram.scene;

import java.util.List;

/**
 * 무리 안의 틈과 무리 사이의 틈을 바닥 위에서 잰 값이다.
 *
 * - within(무리 안의 틈): 기둥마다 같은 무리에서 가장 가까운 기둥까지의 틈을 구하고, 그 가운데 가장 넓은 것.
 *   홀로 있는 기둥뿐이면 0이다.
 * - between(무리 사이의 틈): 서로 다른 무리에 속한 두 기둥 사이의 틈 가운데 가장 좁은 것.
 *   무리가 하나뿐이면 끝없이 넓은 것으로 본다.
 *
 * 예: 한 무리의 기둥들이 이웃과 0.3씩 떨어져 있고 다른 무리와는 가장 가까운 곳이 2.0 떨어져 있으면
 * within은 0.3, between은 2.0이다.
 */
public record ClusterSpacing(double within, double between) {

    public static ClusterSpacing of(List<Pillar> pillars) {
        return new ClusterSpacing(widestGapToNearestNeighbour(pillars), narrowestGapBetweenClusters(pillars));
    }

    private static double narrowestGapBetweenClusters(List<Pillar> pillars) {
        return PillarPair.allOf(pillars).stream()
                .filter(pair -> !pair.inSameCluster())
                .mapToDouble(PillarPair::groundGap)
                .min()
                .orElse(Double.POSITIVE_INFINITY);
    }

    private static double widestGapToNearestNeighbour(List<Pillar> pillars) {
        return pillars.stream()
                .mapToDouble(pillar -> gapToNearestNeighbour(pillar, pillars))
                .filter(Double::isFinite)
                .max()
                .orElse(0);
    }

    private static double gapToNearestNeighbour(Pillar pillar, List<Pillar> pillars) {
        return pillars.stream()
                .filter(other -> other != pillar && other.clusterIndex() == pillar.clusterIndex())
                .mapToDouble(other -> new PillarPair(pillar, other).groundGap())
                .min()
                .orElse(Double.POSITIVE_INFINITY);
    }
}
