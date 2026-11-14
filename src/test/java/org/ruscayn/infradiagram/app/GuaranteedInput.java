package org.ruscayn.infradiagram.app;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.ruscayn.infradiagram.model.Cluster;
import org.ruscayn.infradiagram.model.Element;
import org.ruscayn.infradiagram.model.Infrastructure;
import org.ruscayn.infradiagram.model.Scale;

/**
 * 항상 그려지는 범위 안의 입력 하나. 무리마다 요소가 몇 개인지를 입력 순서대로 받는다.
 *
 * 탐색에서 가장 불리했던 모양으로 만든다. 무리의 가운데 요소만 규모를 바꾸고 나머지는 모두 크게 하며,
 * 글자는 무리의 맨 바깥에 놓이는 마지막 요소들에 붙인다.
 * 예: sizes가 [4, 1]이고 labelsPerCluster가 3이면 첫 무리는 둘째부터 넷째 요소에, 둘째 무리는 하나뿐인 요소에 글자가 붙는다.
 */
record GuaranteedInput(List<Integer> sizes, Scale centerScale, int labelsPerCluster, String label) {

    Infrastructure infrastructure() {
        List<Cluster> clusters = IntStream.range(0, sizes.size()).mapToObj(this::cluster).toList();
        return new Infrastructure(clusters, List.of());
    }

    private Cluster cluster(int index) {
        int size = sizes.get(index);
        return new Cluster(IntStream.range(0, size).mapToObj(position -> element(index, position, size)).toList());
    }

    private Element element(int cluster, int position, int size) {
        Scale scale = position == 0 && size > 1 ? centerScale : Scale.LARGE;
        boolean labeled = position >= size - labelsPerCluster;
        return new Element("c" + cluster + "e" + position, scale, false, labeled ? Optional.of(label) : Optional.empty());
    }
}
