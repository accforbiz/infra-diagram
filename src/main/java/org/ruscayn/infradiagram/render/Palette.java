package org.ruscayn.infradiagram.render;

import java.awt.Color;
import java.util.List;
import org.ruscayn.infradiagram.scene.ClusterColor;

/**
 * 그림에 쓰는 색이다.
 *
 * 무리의 색은 여섯 가지다. 무리마다 색이 겹치지 않아야 하므로, 서로 가장 잘 구별되는 색을 골랐다.
 * 색 차이를 계산하는 검증 도구로, 어느 두 무리든 이웃할 수 있다는 조건에서 확인했다.
 * 가장 비슷한 두 색의 차이가 보통 시각에서 17.8(쉽게 구별된다고 보는 기준 15),
 * 적록 색각에서 9.3(목표 8)이다. 색을 여덟 가지로 늘리면 이 차이가 12.7로 떨어져 기준에 못 미친다.
 * 그래서 색을 받는 무리의 수를 여섯으로 제한한다.
 *
 * 요소가 하나뿐인 무리는 색을 받지 않고 회색으로 그린다. 이 회색은 여섯 색과 17.3 이상 차이 난다.
 */
final class Palette {

    static final Color BACKGROUND = Color.WHITE;
    static final Color SURFACE = Color.WHITE;
    static final Color LINE = new Color(0x1E, 0x1E, 0x1E);
    static final Color LINK = new Color(0x9A, 0xA1, 0xA9);
    static final Color TEXT = new Color(0x15, 0x15, 0x15);

    private static final Color NEUTRAL = new Color(0xD2, 0xD2, 0xD2);
    private static final List<Color> CLUSTER_COLORS = List.of(
            new Color(0x3F, 0xBC, 0xF0), new Color(0xC4, 0x60, 0x16), new Color(0x9C, 0x63, 0xC2),
            new Color(0xA2, 0xBB, 0x40), new Color(0xF9, 0x82, 0xAA), new Color(0x38, 0x94, 0x69));

    Color of(ClusterColor color) {
        return color.isNeutral() ? NEUTRAL : CLUSTER_COLORS.get(color.number());
    }

    int clusterColorCount() {
        return CLUSTER_COLORS.size();
    }
}
