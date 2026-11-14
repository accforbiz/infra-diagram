package org.ruscayn.infradiagram.layout;

import java.util.ArrayList;
import java.util.List;
import org.ruscayn.infradiagram.scene.Box;

/**
 * 무리 덩어리를 입력 순서대로 줄에 나눠 담는다. 한 줄의 너비가 한도를 넘으면 다음 줄로 넘긴다.
 *
 * 덩어리 하나가 한도보다 넓으면 그 덩어리 혼자 한 줄을 차지한다.
 * 예: 너비가 4, 4, 4인 덩어리를 틈 1, 한도 10으로 나누면 [4, 4]와 [4] 두 줄이 된다.
 * 셋을 한 줄에 놓으면 4 + 1 + 4 + 1 + 4 = 14로 한도를 넘기 때문이다.
 */
final class RowSplitter {

    /** 한도와 너비가 같을 때, 소수 계산의 아주 작은 오차 때문에 줄이 넘어가지 않게 하는 여유다. */
    private static final double ROUNDING = 1e-9;

    private final double gap;

    RowSplitter(double gap) {
        this.gap = gap;
    }

    List<List<Box>> split(List<Box> blocks, double widthLimit) {
        List<List<Box>> rows = new ArrayList<>();
        for (Box block : blocks) {
            if (rows.isEmpty() || rowWidth(rows.getLast()) + gap + block.width() > widthLimit + ROUNDING) {
                rows.add(new ArrayList<>());
            }
            rows.getLast().add(block);
        }
        return rows;
    }

    double rowWidth(List<Box> row) {
        double widths = row.stream().mapToDouble(Box::width).sum();
        return widths + gap * (row.size() - 1);
    }
}
