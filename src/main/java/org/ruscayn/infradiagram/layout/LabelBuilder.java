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
 *   무리의 오른쪽 끝 | 틈 | 화살표 | 작은 틈 | 글자 | 작은 틈 | 색 점
 *
 * 화살표는 글자 쪽 끝에서 출발해 기둥의 윗면 가운데를 향한다. 화살표의 길이가 정해져 있고
 * 출발점이 무리의 끝에서 "틈 + 화살표 길이"만큼 떨어져 있으므로, 화살표는 기둥을 덮지 않는다.
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
        double tailX = side.edgeOf(area) + side.direction() * (labelGap + settings.arrowLength());
        ScreenPoint tail = new ScreenPoint(tailX, lineCenter);
        Box textBox = textBox(target.text(), side, tailX, lineCenter);
        Pillar pillar = target.pillar();
        ScreenPoint head = tail.toward(pillar.topCenter(), settings.arrowLength());
        return new LabelMark(pillar.elementId(), target.text(), pillar.style().colorIndex(),
                textBox, tail, head, dot(textBox, side, lineCenter), settings.dotRadius());
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
