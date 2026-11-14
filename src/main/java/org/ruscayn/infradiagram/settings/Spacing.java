package org.ruscayn.infradiagram.settings;

public record Spacing(
        double pillarGap,
        double minClusterGap,
        double clusterGapPerNeighbourGap,
        double labelGap,
        double rowStagger) {
}
