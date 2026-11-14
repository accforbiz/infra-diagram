package org.ruscayn.infradiagram.layout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.ruscayn.infradiagram.testing.TestInputs.cluster;
import static org.ruscayn.infradiagram.testing.TestInputs.element;
import static org.ruscayn.infradiagram.testing.TestInputs.infrastructure;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ruscayn.infradiagram.model.Infrastructure;
import org.ruscayn.infradiagram.scene.Pillar;
import org.ruscayn.infradiagram.scene.Scene;
import org.ruscayn.infradiagram.settings.DrawingSettings;
import org.ruscayn.infradiagram.testing.FixedWidthText;
import org.ruscayn.infradiagram.testing.TestInputs;

class SceneLayoutTest {

    private static final double TOLERANCE = 1e-6;
    private static final String SEVEN_WITH_CORE = cluster(element("a"), element("b"),
            element("hub", "\"core\":true,\"scale\":\"large\",\"label\":\"HUB\""),
            element("c"), element("d"), element("e"), element("f"));

    private final SceneLayout layout = new SceneLayout(DrawingSettings.standard(), new FixedWidthText());

    @Test
    @DisplayName("요소마다 기둥 하나, 연결마다 점선 하나, 글자가 있는 요소마다 글자 하나를 만든다")
    void makesOneMarkPerInputItem() {
        String json = "{\"clusters\":[" + SEVEN_WITH_CORE + "," + cluster(element("x")) + "],"
                + "\"links\":[{\"between\":[\"a\",\"x\"]},{\"between\":[\"b\",\"c\"]}]}";

        Scene scene = layout.arrange(TestInputs.read(json));

        assertEquals(8, scene.pillars().size());
        assertEquals(2, scene.links().size());
        assertEquals(1, scene.labels().size());
    }

    @Test
    @DisplayName("중심 요소는 입력의 셋째에 적혀 있어도 무리의 가운데 자리에 놓인다")
    void putsCoreElementAtCentre() {
        Scene scene = layout.arrange(TestInputs.read(infrastructure(SEVEN_WITH_CORE)));

        assertAtCentreOfBases(pillar(scene, "hub"), scene.pillars());
    }

    @Test
    @DisplayName("중심 요소가 없으면 입력에 먼저 적힌 요소가 가운데 자리에 놓인다")
    void putsFirstElementAtCentreWithoutCore() {
        String sevenWithoutCore = cluster(element("first"), element("b"), element("c"),
                element("d"), element("e"), element("f"), element("g"));

        Scene scene = layout.arrange(TestInputs.read(infrastructure(sevenWithoutCore)));

        assertAtCentreOfBases(pillar(scene, "first"), scene.pillars());
    }

    @Test
    @DisplayName("같은 입력을 두 번 그리면 모든 좌표가 같은 그림이 나온다")
    void drawsSameInputTheSameWay() {
        Infrastructure infrastructure = TestInputs.read(infrastructure(SEVEN_WITH_CORE, cluster(element("x"))));

        assertEquals(layout.arrange(infrastructure), layout.arrange(infrastructure));
    }

    private Pillar pillar(Scene scene, String id) {
        return scene.pillars().stream().filter(pillar -> pillar.elementId().equals(id)).findFirst().orElseThrow();
    }

    /**
     * 첫 고리를 다 채운 일곱 자리는 가운데 자리를 중심으로 대칭이므로,
     * 가운데 자리에 놓인 기둥의 바닥은 모든 바닥의 평균 위치와 같다.
     */
    private void assertAtCentreOfBases(Pillar pillar, List<Pillar> all) {
        double meanX = all.stream().mapToDouble(each -> each.base().x()).average().orElseThrow();
        double meanY = all.stream().mapToDouble(each -> each.base().y()).average().orElseThrow();
        assertEquals(meanX, pillar.base().x(), TOLERANCE);
        assertEquals(meanY, pillar.base().y(), TOLERANCE);
    }
}
