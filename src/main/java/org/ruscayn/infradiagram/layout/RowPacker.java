package org.ruscayn.infradiagram.layout;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.ruscayn.infradiagram.scene.Box;
import org.ruscayn.infradiagram.settings.CanvasSettings;

/**
 * 무리 덩어리를 몇 줄로 놓을지 정한다. 그림에 가장 크게 들어가는 줄 나눔을 고른다.
 *
 * 줄 너비의 한도를 바꿔 가며 줄을 나눠 보고, 각 결과가 그림 안에 들어가도록 줄이거나 키울 때의
 * 배율을 구한다. 배율이 가장 큰 것을 고른다. 배율이 같으면 줄 수가 적은 쪽을 고른다.
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
        return rowWidthLimits(blocks).stream()
                .map(limit -> arranger.arrange(splitter.split(blocks, limit)))
                .max(Comparator.comparingDouble(this::fitScale))
                .orElseThrow();
    }

    /**
     * 살펴볼 줄 너비의 한도를 모두 구한다. 이웃한 덩어리 몇 개를 한 줄에 놓았을 때의 너비가 후보다.
     *
     * 어떤 줄 나눔이든 가장 넓은 줄은 이웃한 덩어리 몇 개로 이루어지므로, 이 후보만 살펴도
     * 가능한 가장 넓은 줄의 너비를 빠짐없이 살핀 것이 된다.
     * 처음에는 "전체 너비를 1, 2, 3, ...으로 나눈 값"만 살폈는데, 크기가 다른 덩어리가 섞이면
     * 좋은 줄 나눔을 놓쳤다. 예: 너비 10인 덩어리 여섯 개와 너비 3인 덩어리 세 개(전체 69)는
     * 한도가 39일 때 [10, 10, 10]과 [10, 10, 10, 3, 3, 3] 두 줄로 가장 잘 놓이는데,
     * 69 / 2 = 34.5나 69 / 3 = 23에서는 이 나눔이 나오지 않는다.
     *
     * 넓은 한도부터 차례로 돌려준다. 배율이 같을 때 줄 수가 적은 쪽이 먼저 선택되게 하기 위해서다.
     */
    private List<Double> rowWidthLimits(List<Box> blocks) {
        List<Double> limits = new ArrayList<>();
        for (int first = 0; first < blocks.size(); first++) {
            for (int last = first; last < blocks.size(); last++) {
                limits.add(splitter.rowWidth(blocks.subList(first, last + 1)));
            }
        }
        limits.sort(Comparator.reverseOrder());
        return limits;
    }

    private double fitScale(List<Box> placed) {
        Box bounds = Box.around(placed);
        return Math.min(canvas.innerWidth() / bounds.width(), canvas.innerHeight() / bounds.height());
    }
}
