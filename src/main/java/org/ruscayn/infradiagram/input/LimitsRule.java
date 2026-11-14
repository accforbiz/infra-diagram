package org.ruscayn.infradiagram.input;

import java.util.List;
import java.util.stream.IntStream;
import org.ruscayn.infradiagram.model.Cluster;
import org.ruscayn.infradiagram.model.Element;
import org.ruscayn.infradiagram.model.Infrastructure;

final class LimitsRule implements InputRule {

    private final InputLimits limits;

    LimitsRule(InputLimits limits) {
        this.limits = limits;
    }

    @Override
    public void check(Infrastructure infrastructure) {
        List<Cluster> clusters = infrastructure.clusters();
        if (clusters.size() > limits.maxClusters()) {
            String message = "Too many clusters: %d. The maximum is %d.";
            throw new InvalidInputException(message.formatted(clusters.size(), limits.maxClusters()));
        }
        IntStream.range(0, clusters.size()).forEach(index -> checkSize(clusters.get(index), index + 1));
        infrastructure.elements().forEach(this::checkLabelLength);
    }

    private void checkSize(Cluster cluster, int clusterNumber) {
        if (cluster.size() > limits.maxElementsPerCluster()) {
            String message = "Cluster #%d has too many elements: %d. The maximum is %d.";
            int maximum = limits.maxElementsPerCluster();
            throw new InvalidInputException(message.formatted(clusterNumber, cluster.size(), maximum));
        }
    }

    private void checkLabelLength(Element element) {
        int length = element.label().map(this::characterCount).orElse(0);
        if (length > limits.maxLabelLength()) {
            String message = "Label of element '%s' is too long: %d characters. The maximum is %d.";
            throw new InvalidInputException(message.formatted(element.id(), length, limits.maxLabelLength()));
        }
    }

    /**
     * 사람이 세는 글자 수를 돌려준다.
     *
     * 자바의 String.length()는 일부 글자를 2로 센다. 예: 수학 기호 "𝒜" 한 글자의 length()는 2다.
     * 상한은 사람이 세는 글자 수로 안내하므로 코드 포인트 수로 센다.
     */
    private int characterCount(String label) {
        return label.codePointCount(0, label.length());
    }
}
