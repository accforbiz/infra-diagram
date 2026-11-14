package org.ruscayn.infradiagram.quality;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.ruscayn.infradiagram.quality.Scenes.label;
import static org.ruscayn.infradiagram.quality.Scenes.pillar;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ruscayn.infradiagram.scene.Scene;

class QualityRulesTest {

    @Test
    @DisplayName("조건 1: 바닥이 겹친 두 기둥을 찾아낸다")
    void findsOverlappingFootprints() {
        Scene scene = Scenes.of(List.of(pillar("a", 0, 500, 500, 50), pillar("b", 0, 530, 500, 50)), List.of());

        assertEquals(List.of("Pillars 'a' and 'b' overlap on the ground."), new FootprintsApart().violations(scene));
    }

    @Test
    @DisplayName("조건 2: 높은 앞 기둥에 윗면이 가려진 뒤 기둥을 찾아낸다")
    void findsHiddenTop() {
        Scene scene = Scenes.of(List.of(pillar("back", 0, 500, 500, 40), pillar("front", 0, 500, 525, 120)), List.of());

        assertEquals(List.of("Top of pillar 'back' is hidden by pillar 'front'."), new TopsVisible().violations(scene));
    }

    @Test
    @DisplayName("조건 2: 높이가 같고 바닥이 떨어져 있으면 몸통이 가려져도 윗면은 보이는 것으로 판정한다")
    void acceptsHiddenBodyWithVisibleTop() {
        Scene scene = Scenes.of(List.of(pillar("back", 0, 500, 500, 80), pillar("front", 0, 500, 525, 80)), List.of());

        assertEquals(List.of(), new TopsVisible().violations(scene));
    }

    @Test
    @DisplayName("조건 3: 무리 사이가 무리 안의 틈보다 좁으면 찾아낸다")
    void findsClustersCloserThanNeighbours() {
        List<String> violations = new ClustersApart().violations(Scenes.of(List.of(
                pillar("a1", 0, 100, 500, 50), pillar("a2", 0, 200, 500, 50), pillar("b1", 1, 250, 500, 50)), List.of()));

        assertEquals(List.of("Clusters are too close: 10.0 px between clusters, 60.0 px inside a cluster."), violations);
    }

    @Test
    @DisplayName("조건 4: 기둥 위에 놓인 글자와 서로 겹친 글자를 찾아낸다")
    void findsLabelOverPillarAndOverLabel() {
        Scene scene = Scenes.of(List.of(pillar("a", 0, 500, 500, 50)),
                List.of(label("a", 490, 460, 20), label("b", 900, 100, 20), label("c", 950, 110, 20)));

        List<String> expected = List.of("Label of 'a' overlaps pillar 'a'.", "Labels of 'b' and 'c' overlap.");
        assertEquals(expected, new LabelsClear().violations(scene));
    }

    @Test
    @DisplayName("조건 5: 여백을 넘어간 기둥과 글자를 찾아낸다")
    void findsMarksOutsideMargin() {
        Scene scene = Scenes.of(List.of(pillar("a", 0, 70, 500, 50)), List.of(label("b", 1800, 500, 20)));

        List<String> expected = List.of("Pillar 'a' reaches outside the picture margin.",
                "Label of 'b' reaches outside the picture margin.");
        assertEquals(expected, new InsideCanvas(60).violations(scene));
    }

    @Test
    @DisplayName("조건 6: 기둥이 최소 크기와 같으면 받아들이고, 그보다 작으면 가장 작은 기둥의 크기를 알린다")
    void findsPillarBelowMinimumSize() {
        Scene scene = Scenes.of(List.of(pillar("a", 0, 500, 500, 50)), List.of());

        assertEquals(List.of(), new PillarMinimumSize(Scenes.RADIUS).violations(scene));
        assertEquals(List.of("The smallest pillar would be drawn with a radius of 20.0 px. The minimum is 20.1 px."),
                new PillarMinimumSize(Scenes.RADIUS + 0.1).violations(scene));
    }

    @Test
    @DisplayName("조건 7: 글자가 최소 크기와 같으면 받아들이고, 그보다 작으면 글자가 여럿이어도 한 줄로 알린다")
    void findsTextBelowMinimumSize() {
        Scene scene = Scenes.of(List.of(), List.of(label("b", 900, 100, 15.9), label("c", 900, 300, 15.9)));

        assertEquals(List.of(), new TextMinimumSize(15.9).violations(scene));
        assertEquals(List.of("Labels would be drawn 15.9 px high. The minimum is 16.0 px."),
                new TextMinimumSize(16).violations(scene));
    }
}
