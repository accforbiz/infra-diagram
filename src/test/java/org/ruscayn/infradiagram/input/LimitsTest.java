package org.ruscayn.infradiagram.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LimitsTest {

    private static final InputLimits LIMITS = new InputLimits(3, 2, 4, 5);

    @Test
    @DisplayName("무리의 수가 최솟값인 1개와 상한인 3개이면 받는다")
    void acceptsClusterCountAtBothEnds() {
        assertEquals(1, InputTesting.read(InputTesting.clustersOfOneElement(1), LIMITS).clusters().size());
        assertEquals(3, InputTesting.read(InputTesting.clustersOfOneElement(3), LIMITS).clusters().size());
    }

    @Test
    @DisplayName("무리의 수가 상한보다 하나 많으면 실제 수와 상한을 알리며 거부한다")
    void rejectsOneClusterOverLimit() {
        String message = InputTesting.rejectionOf(InputTesting.clustersOfOneElement(4), LIMITS);

        assertEquals("Too many clusters: 4. The maximum is 3.", message);
    }

    @Test
    @DisplayName("요소가 둘 이상인 무리가 상한인 2개이면 받고, 요소가 하나뿐인 무리는 그 수에 세지 않는다")
    void acceptsMultiElementClustersAtLimitWithoutCountingSingles() {
        String json = InputTesting.clusters(pair("a"), pair("b"), single("c"));

        assertEquals(3, InputTesting.read(json, LIMITS).clusters().size());
    }

    @Test
    @DisplayName("요소가 둘 이상인 무리가 상한보다 하나 많으면 색이 모자란다는 이유와 함께 거부한다")
    void rejectsOneMultiElementClusterOverLimit() {
        String message = InputTesting.rejectionOf(InputTesting.clusters(pair("a"), pair("b"), pair("c")), LIMITS);

        assertTrue(message.startsWith("Too many clusters with two or more elements: 3. The maximum is 2,"), message);
    }

    @Test
    @DisplayName("한 무리의 요소 수가 최솟값인 1개와 상한인 4개이면 받는다")
    void acceptsClusterSizeAtBothEnds() {
        assertEquals(1, InputTesting.read(InputTesting.oneCluster(InputTesting.elements(1)), LIMITS).elements().size());
        assertEquals(4, InputTesting.read(InputTesting.oneCluster(InputTesting.elements(4)), LIMITS).elements().size());
    }

    @Test
    @DisplayName("한 무리의 요소 수가 상한보다 하나 많으면 몇 번째 무리인지와 함께 거부한다")
    void rejectsOneElementOverLimit() {
        String message = InputTesting.rejectionOf(InputTesting.oneCluster(InputTesting.elements(5)), LIMITS);

        assertEquals("Cluster #1 has too many elements: 5. The maximum is 4.", message);
    }

    @Test
    @DisplayName("글자 길이가 최솟값인 한 글자와 상한인 다섯 글자이면 받는다")
    void acceptsLabelLengthAtBothEnds() {
        InputTesting.read(labeled("가"), LIMITS);
        InputTesting.read(labeled("데이터호수"), LIMITS);
    }

    @Test
    @DisplayName("글자 길이가 상한보다 한 글자 길면 그 요소의 id와 함께 거부한다")
    void rejectsLabelOneCharacterOverLimit() {
        String message = InputTesting.rejectionOf(labeled("데이터 호수"), LIMITS);

        assertTrue(message.startsWith("Label of element 'a' is too long: 6 characters"), message);
    }

    @Test
    @DisplayName("자바가 두 칸으로 세는 글자도 사람이 세는 대로 한 글자로 센다")
    void countsCharactersAsPeopleDo() {
        String fiveMathLetters = "𝒜".repeat(5);

        InputTesting.read(labeled(fiveMathLetters), LIMITS);
    }

    private String pair(String prefix) {
        return "{\"elements\":[{\"id\":\"" + prefix + "1\"},{\"id\":\"" + prefix + "2\"}]}";
    }

    private String single(String id) {
        return "{\"elements\":[{\"id\":\"" + id + "\"}]}";
    }

    private String labeled(String label) {
        return InputTesting.oneCluster("{\"id\":\"a\",\"label\":\"" + label + "\"}");
    }
}
