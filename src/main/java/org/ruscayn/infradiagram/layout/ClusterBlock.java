package org.ruscayn.infradiagram.layout;

import java.util.List;
import java.util.stream.Stream;
import org.ruscayn.infradiagram.scene.Box;
import org.ruscayn.infradiagram.scene.LabelMark;
import org.ruscayn.infradiagram.scene.Pillar;
import org.ruscayn.infradiagram.scene.Placement;

/**
 * 한 무리의 기둥과 글자를 묶은 덩어리다. 무리끼리 놓을 때 이 덩어리를 통째로 옮긴다.
 */
record ClusterBlock(List<Pillar> pillars, List<LabelMark> labels) {

    Box bounds() {
        Stream<Box> pillarBoxes = pillars.stream().map(Pillar::bounds);
        Stream<Box> labelBoxes = labels.stream().map(LabelMark::bounds);
        return Box.around(Stream.concat(pillarBoxes, labelBoxes).toList());
    }

    ClusterBlock movedTo(Box spot) {
        Box current = bounds();
        Placement shift = Placement.shift(spot.left() - current.left(), spot.top() - current.top());
        return new ClusterBlock(
                pillars.stream().map(pillar -> pillar.placed(shift)).toList(),
                labels.stream().map(label -> label.placed(shift)).toList());
    }
}
