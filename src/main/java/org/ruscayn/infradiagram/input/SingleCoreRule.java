package org.ruscayn.infradiagram.input;

import java.util.List;
import java.util.stream.IntStream;
import org.ruscayn.infradiagram.model.Cluster;
import org.ruscayn.infradiagram.model.Element;
import org.ruscayn.infradiagram.model.Infrastructure;

final class SingleCoreRule implements InputRule {

    @Override
    public void check(Infrastructure infrastructure) {
        List<Cluster> clusters = infrastructure.clusters();
        IntStream.range(0, clusters.size()).forEach(index -> check(clusters.get(index), index + 1));
    }

    private void check(Cluster cluster, int clusterNumber) {
        List<String> coreIds = cluster.coreElements().stream().map(Element::id).toList();
        if (coreIds.size() > 1) {
            String message = "Cluster #%d has more than one core element (%s). A cluster can have at most one.";
            throw new InvalidInputException(message.formatted(clusterNumber, String.join(", ", coreIds)));
        }
    }
}
