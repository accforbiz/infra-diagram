package org.ruscayn.infradiagram.layout;

import java.util.List;
import java.util.stream.IntStream;
import org.ruscayn.infradiagram.model.Cluster;
import org.ruscayn.infradiagram.model.Element;
import org.ruscayn.infradiagram.scene.ClusterStyle;
import org.ruscayn.infradiagram.scene.Pillar;

/**
 * 한 무리의 요소를 기둥으로 바꿔 고리 모양으로 놓고, 글자를 바깥 둘레에 붙인다.
 *
 * 중심 요소가 가운데 자리에 오고, 나머지는 입력에 적힌 순서대로 가운데부터 바깥으로 놓인다.
 * 먼저 자리 사이의 거리가 1인 것으로 놓아 보고, 조건을 지키는 거리를 구한 뒤 그만큼 벌린다.
 */
final class ClusterLayout {

    private final RingSlots slots = new RingSlots();
    private final ClusterPitch pitch;
    private final PillarFactory pillarFactory;
    private final LabelPlacer labelPlacer;

    ClusterLayout(ClusterPitch pitch, PillarFactory pillarFactory, LabelPlacer labelPlacer) {
        this.pitch = pitch;
        this.pillarFactory = pillarFactory;
        this.labelPlacer = labelPlacer;
    }

    ClusterBlock arrange(Cluster cluster, int clusterIndex, ClusterStyle style) {
        List<Element> ordered = cluster.coreFirst();
        List<Standing> unitPillars = unitPillars(ordered);
        double distance = pitch.of(unitPillars);
        List<Pillar> pillars = IntStream.range(0, ordered.size())
                .mapToObj(index -> pillarFactory.create(
                        ordered.get(index), spread(unitPillars.get(index), distance), clusterIndex, style))
                .toList();
        return new ClusterBlock(pillars, labelPlacer.place(ordered, pillars));
    }

    private List<Standing> unitPillars(List<Element> ordered) {
        List<GroundPoint> spots = slots.positions(ordered.size());
        return IntStream.range(0, ordered.size())
                .mapToObj(index -> pillarFactory.standing(ordered.get(index), spots.get(index)))
                .toList();
    }

    private Standing spread(Standing unitPillar, double distance) {
        return unitPillar.at(unitPillar.spot().scaled(distance));
    }
}
