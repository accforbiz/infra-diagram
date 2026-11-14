package org.ruscayn.infradiagram.scene;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public record Scene(double width, double height, List<Pillar> pillars, List<LinkLine> links, List<LabelMark> labels) {

    public Scene {
        pillars = List.copyOf(pillars);
        links = List.copyOf(links);
        labels = List.copyOf(labels);
    }

    public Box contentBounds() {
        Stream<Box> pillarBoxes = pillars.stream().map(Pillar::bounds);
        Stream<Box> labelBoxes = labels.stream().map(LabelMark::bounds);
        return Box.around(Stream.concat(pillarBoxes, labelBoxes).toList());
    }

    /**
     * 그리는 순서대로, 곧 뒤쪽 기둥부터 앞쪽 기둥 순서로 돌려준다.
     *
     * 화면에서 바닥이 위쪽에 있을수록 뒤쪽 기둥이다. 뒤쪽부터 그리고 앞쪽을 덧그리면
     * 앞 기둥이 뒤 기둥의 몸통을 자연스럽게 가린다.
     */
    public List<Pillar> backToFront() {
        Comparator<Pillar> byDepth = Comparator.comparingDouble(pillar -> pillar.base().y());
        return pillars.stream().sorted(byDepth).toList();
    }

    public Scene placed(Placement placement, double canvasWidth, double canvasHeight) {
        return new Scene(canvasWidth, canvasHeight,
                pillars.stream().map(pillar -> pillar.placed(placement)).toList(),
                links.stream().map(link -> link.placed(placement)).toList(),
                labels.stream().map(label -> label.placed(placement)).toList());
    }
}
