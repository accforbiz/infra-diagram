package org.ruscayn.infradiagram.input;

import java.util.List;

record RawInfrastructure(List<RawCluster> clusters, List<RawLink> links) {

    record RawCluster(List<RawElement> elements) {
    }

    record RawElement(String id, String scale, Boolean core, String label) {
    }

    record RawLink(List<String> between) {
    }
}
