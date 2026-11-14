package org.ruscayn.infradiagram.layout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ruscayn.infradiagram.model.Element;
import org.ruscayn.infradiagram.model.Scale;
import org.ruscayn.infradiagram.scene.Box;
import org.ruscayn.infradiagram.scene.ClusterStyle;
import org.ruscayn.infradiagram.scene.LabelMark;
import org.ruscayn.infradiagram.scene.Pillar;
import org.ruscayn.infradiagram.scene.PillarShape;
import org.ruscayn.infradiagram.scene.ScreenPoint;
import org.ruscayn.infradiagram.settings.LabelSettings;
import org.ruscayn.infradiagram.testing.FixedWidthText;

/**
 * 기둥은 반지름 1, 세로 반지름 0.5, 높이 2로 만든다. 글자 "AB"의 폭은 가짜 폭 재기 도구로 1이다.
 * 설정은 글자 크기 1, 줄 높이 1.5, 화살표가 시작되기 전의 빈자리 1, 무리와의 틈 0.5다.
 */
class LabelPlacerTest {

    private static final double TOLERANCE = 1e-9;
    private static final ClusterStyle STYLE = new ClusterStyle(PillarShape.CYLINDER, 0);

    private final LabelPlacer placer = new LabelPlacer(
            new LabelBuilder(new LabelSettings(1.0, 1.5, 1.0, 0.2), new FixedWidthText(), 0.5), 1.5);

    @Test
    @DisplayName("가운데보다 왼쪽에 있는 기둥의 글자는 무리의 왼쪽 바깥에, 손으로 구한 자리에 놓인다")
    void placesLabelOfLeftPillarOnTheLeft() {
        List<LabelMark> labels = placer.place(elements(2), List.of(pillar(-3, 0), pillar(3, 0)));

        // 무리의 왼쪽 끝은 -4다. 틈 0.5와 빈자리 1을 띄우면 -5.5, 작은 틈 0.4를 더 띄우면 -5.9이고,
        // 폭 1인 글자의 왼쪽 끝은 -6.9다. 글자의 세로 가운데는 기둥의 윗면 높이인 -2다.
        assertSameBox(new Box(-6.9, -2.5, 1.0, 1.0), labels.get(0).textBox());
    }

    @Test
    @DisplayName("가운데와 그 오른쪽에 있는 기둥의 글자는 무리의 오른쪽 바깥에 놓인다")
    void placesLabelOfCentreAndRightPillarsOnTheRight() {
        List<LabelMark> labels = placer.place(elements(2), List.of(pillar(0, 0), pillar(3, 4)));

        // 무리의 오른쪽 끝은 4다. 4 + 0.5 + 1 + 0.4 = 5.9에서 글자가 시작한다.
        assertSameBox(new Box(5.9, -2.5, 1.0, 1.0), labels.get(0).textBox());
        assertSameBox(new Box(5.9, 1.5, 1.0, 1.0), labels.get(1).textBox());
    }

    @Test
    @DisplayName("같은 쪽의 글자가 한 줄 높이보다 가까우면 아래 글자를 한 줄 아래로 내린다")
    void pushesCloseLabelDownByOneLine() {
        List<LabelMark> labels = placer.place(elements(2), List.of(pillar(1, 0), pillar(3, 0.2)));

        // 윗면 높이는 -2와 -1.8이다. 둘째 글자는 -2 + 1.5 = -0.5로 내려간다.
        assertEquals(-2.0, labels.get(0).textBox().centerY(), TOLERANCE);
        assertEquals(-0.5, labels.get(1).textBox().centerY(), TOLERANCE);
    }

    @Test
    @DisplayName("화살표는 글자 곁에서 출발해 기둥의 윗면 가운데에서 끝난다")
    void pointsArrowAtTopOfPillar() {
        Pillar pillar = pillar(3, 0);

        LabelMark label = placer.place(elements(1), List.of(pillar)).get(0);

        assertEquals(pillar.topCenter(), label.arrowHead());
        assertEquals(5.5, label.arrowTail().x(), TOLERANCE);
    }

    private List<Element> elements(int count) {
        return java.util.stream.IntStream.range(0, count)
                .mapToObj(index -> new Element("e" + index, Scale.MEDIUM, false, Optional.of("AB")))
                .toList();
    }

    private Pillar pillar(double x, double y) {
        return new Pillar("p", 0, STYLE, new ScreenPoint(x, y), 1, 0.5, 2, true);
    }

    private void assertSameBox(Box expected, Box actual) {
        assertEquals(expected.left(), actual.left(), TOLERANCE);
        assertEquals(expected.top(), actual.top(), TOLERANCE);
        assertEquals(expected.width(), actual.width(), TOLERANCE);
        assertEquals(expected.height(), actual.height(), TOLERANCE);
    }
}
