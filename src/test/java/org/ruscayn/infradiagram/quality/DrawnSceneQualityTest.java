package org.ruscayn.infradiagram.quality;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.ruscayn.infradiagram.testing.TestInputs.cluster;
import static org.ruscayn.infradiagram.testing.TestInputs.element;
import static org.ruscayn.infradiagram.testing.TestInputs.infrastructure;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.ruscayn.infradiagram.layout.SceneLayout;
import org.ruscayn.infradiagram.scene.Scene;
import org.ruscayn.infradiagram.settings.DrawingSettings;
import org.ruscayn.infradiagram.testing.FixedWidthText;
import org.ruscayn.infradiagram.testing.TestInputs;

class DrawnSceneQualityTest {

    private static final String[] SCALES = {"small", "medium", "large"};

    private final DrawingSettings settings = DrawingSettings.standard();
    private final SceneLayout layout = new SceneLayout(settings, new FixedWidthText());
    private final QualityInspector inspector = QualityInspector.standard(settings.canvas(), settings.quality());

    @ParameterizedTest(name = "{0}")
    @MethodSource("inputs")
    @DisplayName("여러 구성을 그린 결과가 품질 조건 일곱 가지를 모두 지킨다")
    void drawnSceneMeetsEveryQualityRule(String description, String json) {
        Scene scene = layout.arrange(TestInputs.read(json));

        assertEquals(List.of(), inspector.inspect(scene), description);
    }

    static Stream<org.junit.jupiter.params.provider.Arguments> inputs() {
        return Stream.of(
                input("요소 하나", infrastructure(cluster(element("only")))),
                input("글자가 붙은 요소 하나", infrastructure(cluster(labeled("only", 0)))),
                input("크기가 섞인 일곱 요소 무리", infrastructure(mixedCluster("a", 7))),
                input("둘째 고리로 넘어간 여덟 요소 무리", infrastructure(mixedCluster("a", 8))),
                input("모든 요소에 글자가 붙은 무리", infrastructure(labeledCluster("a", 7))),
                input("큰 무리 셋과 홀로 있는 요소 셋", infrastructure(mixedCluster("a", 7), mixedCluster("b", 9),
                        mixedCluster("c", 6), cluster(labeled("x", 1)), cluster(labeled("y", 0)), cluster(labeled("z", 2)))));
    }

    private static org.junit.jupiter.params.provider.Arguments input(String description, String json) {
        return org.junit.jupiter.params.provider.Arguments.of(description, json);
    }

    private static String mixedCluster(String prefix, int size) {
        return cluster(IntStream.range(0, size)
                .mapToObj(index -> element(prefix + index, "\"scale\":\"" + SCALES[(index + 2) % 3] + "\""))
                .toArray(String[]::new));
    }

    private static String labeledCluster(String prefix, int size) {
        return cluster(IntStream.range(0, size).mapToObj(index -> labeled(prefix + index, index)).toArray(String[]::new));
    }

    private static String labeled(String id, int index) {
        return element(id, "\"scale\":\"" + SCALES[index % 3] + "\",\"label\":\"LABEL " + id + "\"");
    }
}
