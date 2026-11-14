package org.ruscayn.infradiagram.layout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ruscayn.infradiagram.scene.Box;
import org.ruscayn.infradiagram.settings.CanvasSettings;

class RowPackerTest {

    private static final CanvasSettings SIXTEEN_BY_NINE = new CanvasSettings(1600, 900, 0, 60);
    private static final List<Box> SIX_SQUARES = Collections.nCopies(6, new Box(0, 0, 10, 10));

    @Test
    @DisplayName("가로세로 10인 덩어리 여섯 개는 16대 9 그림에 가장 크게 들어가는 두 줄로 놓인다")
    void choosesRowCountThatFitsLargest() {
        List<Box> placed = new RowPacker(0, 0, 0, SIXTEEN_BY_NINE).pack(SIX_SQUARES);

        assertEquals(List.of(0.0, 10.0, 20.0, 0.0, 10.0, 20.0), placed.stream().map(Box::left).toList());
        assertEquals(List.of(0.0, 0.0, 0.0, 10.0, 10.0, 10.0), placed.stream().map(Box::top).toList());
    }

    /**
     * 틀렸던 곳을 재현한다. 첫 줄에 큰 것 셋(너비 30), 둘째 줄에 큰 것 셋과 작은 것 셋(너비 39)을 놓으면
     * 39 × 20이 되어 배율이 1600 / 39 = 41.0이다. 전체 너비 69를 2나 3으로 나눈 한도로는 이 배치가
     * 나오지 않아 세 줄(배율 37.5)을 고르던 결함이 있었다. 첫 줄은 (39 - 30) / 2 = 4.5만큼 가운데로 옮겨진다.
     */
    @Test
    @DisplayName("큰 덩어리 여섯 개 뒤에 작은 덩어리 세 개가 오면, 작은 것들을 둘째 줄 끝에 붙인 두 줄 배치를 찾아낸다")
    void findsRowBreakThatEvenSplitsMiss() {
        List<Box> blocks = new ArrayList<>(Collections.nCopies(6, new Box(0, 0, 10, 10)));
        blocks.addAll(Collections.nCopies(3, new Box(0, 0, 3, 4)));

        List<Box> placed = new RowPacker(0, 0, 0, SIXTEEN_BY_NINE).pack(blocks);

        assertEquals(List.of(4.5, 14.5, 24.5, 0.0, 10.0, 20.0, 30.0, 33.0, 36.0), placed.stream().map(Box::left).toList());
        assertEquals(List.of(0.0, 0.0, 0.0, 10.0, 10.0, 10.0, 13.0, 13.0, 13.0), placed.stream().map(Box::top).toList());
    }

    @Test
    @DisplayName("둘째 줄은 정해 둔 거리만큼 오른쪽으로 밀려 첫째 줄과 엇갈린다")
    void staggersSecondRow() {
        List<Box> placed = new RowPacker(0, 0, 5, SIXTEEN_BY_NINE).pack(SIX_SQUARES);

        assertEquals(List.of(0.0, 10.0, 20.0, 5.0, 15.0, 25.0), placed.stream().map(Box::left).toList());
    }

    @Test
    @DisplayName("덩어리 사이에는 가로 틈 2를, 줄 사이에는 세로 틈 3을 둔다")
    void keepsGapBetweenBlocksAndRows() {
        List<Box> placed = new RowPacker(2, 3, 0, SIXTEEN_BY_NINE).pack(SIX_SQUARES);

        assertEquals(List.of(0.0, 12.0, 24.0, 0.0, 12.0, 24.0), placed.stream().map(Box::left).toList());
        assertEquals(List.of(0.0, 0.0, 0.0, 13.0, 13.0, 13.0), placed.stream().map(Box::top).toList());
    }
}
