package org.ruscayn.infradiagram.layout;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.ruscayn.infradiagram.model.Link;
import org.ruscayn.infradiagram.scene.LinkLine;
import org.ruscayn.infradiagram.scene.Pillar;

final class LinkLines {

    private final Map<String, Pillar> pillarsById;

    LinkLines(List<Pillar> pillars) {
        this.pillarsById = pillars.stream().collect(Collectors.toMap(Pillar::elementId, Function.identity()));
    }

    List<LinkLine> between(List<Link> links) {
        return links.stream().map(this::line).toList();
    }

    private LinkLine line(Link link) {
        Pillar first = pillarsById.get(link.firstId());
        Pillar second = pillarsById.get(link.secondId());
        return new LinkLine(first.base(), second.base());
    }
}
