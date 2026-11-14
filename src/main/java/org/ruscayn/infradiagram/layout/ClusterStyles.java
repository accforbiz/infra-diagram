package org.ruscayn.infradiagram.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.ruscayn.infradiagram.model.Cluster;
import org.ruscayn.infradiagram.scene.ClusterColor;
import org.ruscayn.infradiagram.scene.ClusterStyle;
import org.ruscayn.infradiagram.scene.PillarShape;

/**
 * 무리마다 모양과 색을 배정한다.
 *
 * 모양은 무리의 순서대로 삼각기둥, 육각기둥, 원기둥을 돌려 쓴다. 모양은 세 가지뿐이어서
 * 넷째 무리부터 다시 나오지만, 색이 겹치지 않으므로 무리를 헷갈리지 않는다.
 *
 * 색은 요소가 둘 이상인 무리에만 0번부터 차례로 준다. 요소가 하나뿐인 무리는 묶을 것이
 * 없으므로 색을 주지 않고, 색 번호도 쓰지 않는다. 색은 돌려 쓰지 않는다.
 * 예: 요소의 수가 [3, 1, 2, 1, 4]인 무리들은 색을 [0번, 없음, 1번, 없음, 2번]으로 받는다.
 */
final class ClusterStyles {

    List<ClusterStyle> assign(List<Cluster> clusters) {
        List<ClusterColor> colors = colors(clusters);
        PillarShape[] shapes = PillarShape.values();
        return IntStream.range(0, clusters.size())
                .mapToObj(index -> new ClusterStyle(shapes[index % shapes.length], colors.get(index)))
                .toList();
    }

    private List<ClusterColor> colors(List<Cluster> clusters) {
        List<ClusterColor> colors = new ArrayList<>();
        int nextNumber = 0;
        for (Cluster cluster : clusters) {
            boolean alone = cluster.hasSingleElement();
            colors.add(alone ? ClusterColor.neutral() : ClusterColor.numbered(nextNumber++));
        }
        return colors;
    }
}
