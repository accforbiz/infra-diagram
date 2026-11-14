package org.ruscayn.infradiagram.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ClusterTest {

    @Test
    @DisplayName("중심 요소가 있으면 맨 앞에 두고 나머지는 입력에 적힌 순서를 지킨다")
    void putsCoreElementFirst() {
        Cluster cluster = new Cluster(List.of(element("a", false), element("b", true), element("c", false)));

        assertEquals(List.of("b", "a", "c"), ids(cluster.coreFirst()));
    }

    @Test
    @DisplayName("중심 요소가 없으면 입력에 적힌 순서 그대로다")
    void keepsInputOrderWithoutCoreElement() {
        Cluster cluster = new Cluster(List.of(element("a", false), element("b", false), element("c", false)));

        assertEquals(List.of("a", "b", "c"), ids(cluster.coreFirst()));
    }

    private Element element(String id, boolean core) {
        return new Element(id, Scale.MEDIUM, core, Optional.empty());
    }

    private List<String> ids(List<Element> elements) {
        return elements.stream().map(Element::id).toList();
    }
}
