package org.ruscayn.infradiagram.input;

import java.util.List;
import java.util.stream.IntStream;
import org.ruscayn.infradiagram.input.RawInfrastructure.RawCluster;
import org.ruscayn.infradiagram.input.RawInfrastructure.RawElement;
import org.ruscayn.infradiagram.input.RawInfrastructure.RawLink;
import org.ruscayn.infradiagram.model.Cluster;
import org.ruscayn.infradiagram.model.Element;
import org.ruscayn.infradiagram.model.Infrastructure;
import org.ruscayn.infradiagram.model.Link;

final class InfrastructureAssembler {

    private final ElementAssembler elementAssembler = new ElementAssembler();

    Infrastructure assemble(RawInfrastructure raw) {
        if (raw == null) {
            throw new InvalidInputException("Input must be a JSON object with a 'clusters' field.");
        }
        return new Infrastructure(clusters(raw.clusters()), links(raw.links()));
    }

    private List<Cluster> clusters(List<RawCluster> raws) {
        if (raws == null || raws.isEmpty()) {
            throw new InvalidInputException("Input must contain at least one cluster.");
        }
        return IntStream.range(0, raws.size())
                .mapToObj(index -> cluster(raws.get(index), index + 1))
                .toList();
    }

    private Cluster cluster(RawCluster raw, int clusterNumber) {
        if (raw == null || raw.elements() == null || raw.elements().isEmpty()) {
            String message = "Cluster #%d has no elements. Every cluster needs at least one element.";
            throw new InvalidInputException(message.formatted(clusterNumber));
        }
        return new Cluster(elements(raw.elements(), clusterNumber));
    }

    private List<Element> elements(List<RawElement> raws, int clusterNumber) {
        return IntStream.range(0, raws.size())
                .mapToObj(index -> element(raws.get(index), index + 1, clusterNumber))
                .toList();
    }

    private Element element(RawElement raw, int elementNumber, int clusterNumber) {
        String position = "Element #%d of cluster #%d".formatted(elementNumber, clusterNumber);
        return elementAssembler.assemble(raw, position);
    }

    private List<Link> links(List<RawLink> raws) {
        if (raws == null) {
            return List.of();
        }
        return IntStream.range(0, raws.size())
                .mapToObj(index -> link(raws.get(index), index + 1))
                .toList();
    }

    private Link link(RawLink raw, int linkNumber) {
        if (raw == null || !namesTwoElements(raw.between())) {
            String message = "Link #%d must name exactly two element ids in 'between'.";
            throw new InvalidInputException(message.formatted(linkNumber));
        }
        return new Link(raw.between().get(0), raw.between().get(1));
    }

    private boolean namesTwoElements(List<String> between) {
        return between != null && between.size() == 2 && between.stream().noneMatch(id -> id == null);
    }
}
