package org.ruscayn.infradiagram.scene;

/**
 * 기둥에 붙이는 글자 하나와 그 곁의 화살표, 색 점이다.
 *
 * textBox의 높이가 글자 크기다. 화살표는 글자 곁의 arrowTail에서 기둥 윗면 가운데인 arrowHead까지 그린다.
 * dot은 그 무리의 색으로 칠해서 글자가 어느 무리의 것인지 알려 주는 점이다.
 */
public record LabelMark(
        String elementId,
        String text,
        ClusterColor color,
        Box textBox,
        ScreenPoint arrowTail,
        ScreenPoint arrowHead,
        ScreenPoint dot,
        double dotRadius) {

    public double fontSize() {
        return textBox.height();
    }

    public Box captionBox() {
        Box dotBox = new Box(dot.x() - dotRadius, dot.y() - dotRadius, 2 * dotRadius, 2 * dotRadius);
        return textBox.union(dotBox);
    }

    public Box bounds() {
        return captionBox().union(Box.between(arrowTail, arrowHead));
    }

    public LabelMark placed(Placement placement) {
        return new LabelMark(elementId, text, color, placement.apply(textBox),
                placement.apply(arrowTail), placement.apply(arrowHead),
                placement.apply(dot), placement.apply(dotRadius));
    }
}
