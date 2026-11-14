package org.ruscayn.infradiagram.layout;

import java.util.ArrayList;
import java.util.List;
import org.ruscayn.infradiagram.scene.Box;

/**
 * 줄에 나눠 담은 덩어리에 자리를 준다.
 *
 * 줄은 위에서 아래로 쌓고, 각 줄은 가장 넓은 줄에 맞춰 가운데로 모은다.
 * 한 줄 안에서는 덩어리를 왼쪽부터 놓고, 높이가 다른 덩어리는 줄의 세로 가운데에 맞춘다.
 * 짝수 번째 줄(둘째, 넷째 줄)은 stagger만큼 오른쪽으로 밀어 줄마다 엇갈리게 한다.
 */
final class RowArranger {

    private final RowSplitter splitter;
    private final double gap;
    private final double stagger;

    RowArranger(RowSplitter splitter, double gap, double stagger) {
        this.splitter = splitter;
        this.gap = gap;
        this.stagger = stagger;
    }

    List<Box> arrange(List<List<Box>> rows) {
        double widest = rows.stream().mapToDouble(splitter::rowWidth).max().orElseThrow();
        List<Box> placed = new ArrayList<>();
        double top = 0;
        for (int index = 0; index < rows.size(); index++) {
            placed.addAll(placeRow(rows.get(index), startOf(rows.get(index), widest, index), top));
            top += rowHeight(rows.get(index)) + gap;
        }
        return placed;
    }

    private double startOf(List<Box> row, double widest, int rowIndex) {
        double centered = (widest - splitter.rowWidth(row)) / 2;
        return rowIndex % 2 == 0 ? centered : centered + stagger;
    }

    private List<Box> placeRow(List<Box> row, double start, double top) {
        double height = rowHeight(row);
        List<Box> placed = new ArrayList<>();
        double left = start;
        for (Box block : row) {
            placed.add(new Box(left, top + (height - block.height()) / 2, block.width(), block.height()));
            left += block.width() + gap;
        }
        return placed;
    }

    private double rowHeight(List<Box> row) {
        return row.stream().mapToDouble(Box::height).max().orElseThrow();
    }
}
