package org.ruscayn.infradiagram.layout;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import org.ruscayn.infradiagram.scene.Box;
import org.ruscayn.infradiagram.settings.CanvasSettings;

/**
 * 무리 덩어리를 몇 줄로 놓을지 정한다. 그림에 가장 크게 들어가는 줄 나눔을 고른다.
 *
 * 줄 너비의 한도를 "전체 너비 / 1", "전체 너비 / 2", ... 로 바꿔 가며 줄을 나눠 보고,
 * 각 결과가 그림 안에 들어가도록 줄이거나 키울 때의 배율을 구한다. 배율이 가장 큰 것을 고른다.
 * 배율이 같으면 줄 수가 적은 쪽을 고른다.
 *
 * 예: 16대 9 그림에 가로 10, 세로 10인 덩어리 6개를 틈 없이 놓는다고 하자.
 * 한 줄이면 60 × 10이고 가로가 먼저 차서 배율은 16 / 60 = 0.27이다.
 * 두 줄이면 30 × 20이고 세로가 먼저 차서 배율은 9 / 20 = 0.45다.
 * 세 줄이면 20 × 30이고 배율은 9 / 30 = 0.3이다. 그래서 두 줄을 고른다.
 */
final class RowPacker {

    private final RowSplitter splitter;
    private final RowArranger arranger;
    private final CanvasSettings canvas;

    RowPacker(double columnGap, double rowGap, double stagger, CanvasSettings canvas) {
        this.splitter = new RowSplitter(columnGap);
        this.arranger = new RowArranger(splitter, columnGap, rowGap, stagger);
        this.canvas = canvas;
    }

    List<Box> pack(List<Box> blocks) {
        return candidates(blocks).stream().max(Comparator.comparingDouble(this::fitScale)).orElseThrow();
    }

    private List<List<Box>> candidates(List<Box> blocks) {
        double totalWidth = splitter.rowWidth(blocks);
        return IntStream.rangeClosed(1, blocks.size())
                .mapToObj(rows -> arranger.arrange(splitter.split(blocks, totalWidth / rows)))
                .toList();
    }

    private double fitScale(List<Box> placed) {
        Box bounds = Box.around(placed);
        return Math.min(canvas.innerWidth() / bounds.width(), canvas.innerHeight() / bounds.height());
    }
}
