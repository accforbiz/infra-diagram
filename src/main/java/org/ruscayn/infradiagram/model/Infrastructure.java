package org.ruscayn.infradiagram.model;

import java.util.List;

public record Infrastructure(List<Cluster> clusters, List<Link> links) {

    public Infrastructure {
        clusters = List.copyOf(clusters);
        links = List.copyOf(links);
    }

    public List<Element> elements() {
        return clusters.stream().flatMap(cluster -> cluster.elements().stream()).toList();
    }
}
