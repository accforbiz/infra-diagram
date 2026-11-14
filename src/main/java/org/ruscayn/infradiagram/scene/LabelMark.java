package org.ruscayn.infradiagram.scene;

/**
 * 기둥에 붙이는 글자 하나와 그 곁의 화살표, 색 점이다.
 *
 * textBox의 높이가 글자 크기다. 화살표는 arrowTail에서 arrowHead로 그리며 기둥의 윗면 쪽을 가리킨다.
 * dot은 기둥 윗면과 같은 색으로 칠해서 글자와 기둥을 짝짓는 점이다.
 */
public record LabelMark(
        String elementId,
        String text,
        int colorIndex,
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
        return new LabelMark(elementId, text, colorIndex, placement.apply(textBox),
                placement.apply(arrowTail), placement.apply(arrowHead),
                placement.apply(dot), placement.apply(dotRadius));
    }
}
