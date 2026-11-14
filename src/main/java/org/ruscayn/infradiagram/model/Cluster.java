package org.ruscayn.infradiagram.model;

import java.util.ArrayList;
import java.util.List;

public record Cluster(List<Element> elements) {

    public Cluster {
        elements = List.copyOf(elements);
    }

    public int size() {
        return elements.size();
    }

    public boolean hasSingleElement() {
        return elements.size() == 1;
    }

    public List<Element> coreElements() {
        return elements.stream().filter(Element::core).toList();
    }

    /**
     * 무리의 가운데부터 바깥으로 채울 순서를 돌려준다.
     *
     * 중심 요소가 있으면 맨 앞에 두고, 나머지는 입력에 적힌 순서를 그대로 지킨다.
     * 중심 요소가 없으면 입력에 적힌 순서 그대로다.
     * 예: 입력이 [a, b(중심), c]이면 [b, a, c]가 된다.
     */
    public List<Element> coreFirst() {
        List<Element> ordered = new ArrayList<>(coreElements());
        elements.stream().filter(element -> !element.core()).forEach(ordered::add);
        return List.copyOf(ordered);
    }
}
