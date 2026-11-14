package org.ruscayn.infradiagram.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.ruscayn.infradiagram.layout.SceneLayout;
import org.ruscayn.infradiagram.quality.QualityInspector;
import org.ruscayn.infradiagram.render.DiagramFont;
import org.ruscayn.infradiagram.settings.DrawingSettings;

/**
 * 안내문과 설명서에 적은 "항상 그려지는 범위"를 실제 글꼴과 표준 설정으로 그려서 확인한다.
 *
 * 무리를 놓는 순서와 무리의 크기는 빠짐없이 훑는다. 글자는 입력 상한까지 붙인다.
 * 그려 보는 입력은 모두 27,000여 개다.
 */
class GuaranteedRangeTest {

    private static final int SHOWN = 5;

    private final GuaranteedRange range = GuaranteedRange.standard();
    private final DrawingSettings settings = DrawingSettings.standard();
    private final SceneLayout layout = new SceneLayout(settings, new DiagramFont());
    private final QualityInspector inspector = QualityInspector.standard(settings.canvas(), settings.quality());

    @ParameterizedTest(name = "무리 {0}개")
    @MethodSource("clusterCounts")
    @DisplayName("항상 그려진다고 알린 범위의 입력은 무리의 순서와 크기가 어떻든 품질 조건 일곱 가지를 지킨다")
    void everyInputInsideGuaranteedRangeIsDrawn(int clusterCount) {
        Stream<GuaranteedInput> inputs = new GuaranteedInputs(range, settings.limits()).withClusters(clusterCount);

        assertEquals(List.of(), inputs.flatMap(this::failures).limit(SHOWN).toList());
    }

    static IntStream clusterCounts() {
        return IntStream.rangeClosed(1, GuaranteedRange.standard().maxClusters());
    }

    private Stream<String> failures(GuaranteedInput input) {
        List<String> violations = inspector.inspect(layout.arrange(input.infrastructure()));
        return violations.stream().map(violation -> input + ": " + violation);
    }
}
