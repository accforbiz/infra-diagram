package org.ruscayn.infradiagram.layout;

import org.ruscayn.infradiagram.scene.Box;
import org.ruscayn.infradiagram.scene.LabelMark;
import org.ruscayn.infradiagram.scene.Pillar;
import org.ruscayn.infradiagram.scene.ScreenPoint;
import org.ruscayn.infradiagram.settings.LabelSettings;

/**
 * 글자 하나의 모양을 만든다. 무리의 바깥쪽으로 화살표, 글자, 색 점이 차례로 놓인다.
 *
 * 오른쪽에 놓을 때의 가로 배치는 다음과 같다. 왼쪽에 놓을 때는 좌우를 뒤집는다.
 *
 *   무리의 오른쪽 끝 | 틈 | 화살표가 시작되기 전의 빈자리 | 작은 틈 | 글자 | 작은 틈 | 색 점
 *
 * 화살표는 글자 쪽 끝에서 출발해 기둥의 윗면 가운데까지 간다. 한 무리에 글자가 여럿 붙어도
 * 어느 글자가 어느 기둥을 가리키는지 정확히 알 수 있게 하려는 것이다.
 * 그래서 화살표는 다른 기둥 위를 지나갈 수 있고, 글자를 마지막에 그려 화살표가 가려지지 않게 한다.
 */
final class LabelBuilder {

    private static final double SMALL_GAP_PER_FONT_SIZE = 0.4;

    private final LabelSettings settings;
    private final TextMeasurer measurer;
    private final double labelGap;

    LabelBuilder(LabelSettings settings, TextMeasurer measurer, double labelGap) {
        this.settings = settings;
        this.measurer = measurer;
        this.labelGap = labelGap;
    }

    LabelMark build(LabelTarget target, double lineCenter, Box area) {
        LabelSide side = target.side();
        double tailX = side.edgeOf(area) + side.direction() * (labelGap + settings.arrowRoom());
        ScreenPoint tail = new ScreenPoint(tailX, lineCenter);
        Box textBox = textBox(target.text(), side, tailX, lineCenter);
        Pillar pillar = target.pillar();
        return new LabelMark(pillar.elementId(), target.text(), pillar.style().color(),
                textBox, tail, pillar.topCenter(), dot(textBox, side, lineCenter), settings.dotRadius());
    }

    private Box textBox(String text, LabelSide side, double tailX, double lineCenter) {
        double fontSize = settings.fontSize();
        double width = measurer.width(text, fontSize);
        double nearEdge = tailX + side.direction() * smallGap();
        double left = side == LabelSide.RIGHT ? nearEdge : nearEdge - width;
        return new Box(left, lineCenter - fontSize / 2, width, fontSize);
    }

    private ScreenPoint dot(Box textBox, LabelSide side, double lineCenter) {
        double offset = smallGap() + settings.dotRadius();
        return new ScreenPoint(side.outerEdgeOf(textBox) + side.direction() * offset, lineCenter);
    }

    private double smallGap() {
        return settings.fontSize() * SMALL_GAP_PER_FONT_SIZE;
    }
}
