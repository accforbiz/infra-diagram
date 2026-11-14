package org.ruscayn.infradiagram.quality;

import java.util.List;
import org.ruscayn.infradiagram.scene.Box;
import org.ruscayn.infradiagram.scene.ClusterStyle;
import org.ruscayn.infradiagram.scene.LabelMark;
import org.ruscayn.infradiagram.scene.Pillar;
import org.ruscayn.infradiagram.scene.PillarShape;
import org.ruscayn.infradiagram.scene.Scene;
import org.ruscayn.infradiagram.scene.ScreenPoint;

/**
 * 품질 조건을 일부러 어긴 그림을 손으로 만들기 위한 도구다.
 * 기둥은 가로 반지름 20, 세로 반지름 10(앞뒤로 절반 눌림)으로 만든다.
 */
final class Scenes {

    static final double RADIUS = 20;

    private static final ClusterStyle STYLE = new ClusterStyle(PillarShape.CYLINDER, 0);

    private Scenes() {
    }

    static Pillar pillar(String id, int cluster, double x, double y, double height) {
        return new Pillar(id, cluster, STYLE, new ScreenPoint(x, y), RADIUS, RADIUS / 2, height, false);
    }

    static LabelMark label(String id, double left, double top, double fontSize) {
        Box textBox = new Box(left, top, 100, fontSize);
        ScreenPoint beside = new ScreenPoint(left - 10, top + fontSize / 2);
        return new LabelMark(id, "LABEL", 0, textBox, beside, beside, new ScreenPoint(left + 110, top + fontSize / 2), 4);
    }

    static Scene of(List<Pillar> pillars, List<LabelMark> labels) {
        return new Scene(1920, 1080, pillars, List.of(), labels);
    }
}
