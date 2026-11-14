package org.ruscayn.infradiagram.layout;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
