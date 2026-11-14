package org.ruscayn.infradiagram.scene;

import java.util.ArrayList;
import java.util.List;

public record PillarPair(Pillar first, Pillar second) {

    public static List<PillarPair> allOf(List<Pillar> pillars) {
        List<PillarPair> pairs = new ArrayList<>();
        for (int index = 0; index < pillars.size(); index++) {
            Pillar first = pillars.get(index);
            pillars.subList(index + 1, pillars.size()).forEach(second -> pairs.add(new PillarPair(first, second)));
        }
        return pairs;
    }

    public boolean inSameCluster() {
        return first.clusterIndex() == second.clusterIndex();
    }

    /**
     * 바닥 위에서 잰 두 기둥 사이의 틈이다. 음수이면 바닥이 겹친다.
     * 예: 가운데끼리 3 떨어져 있고 반지름이 각각 1이면 틈은 3 - 1 - 1 = 1이다.
     */
    public double groundGap() {
        return first.groundDistanceTo(second) - first.radius() - second.radius();
    }

    public Pillar front() {
        return first.inFrontOf(second) ? first : second;
    }

    public Pillar back() {
        return first.inFrontOf(second) ? second : first;
    }
}
