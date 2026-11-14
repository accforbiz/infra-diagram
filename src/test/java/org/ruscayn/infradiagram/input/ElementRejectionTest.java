package org.ruscayn.infradiagram.input;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ElementRejectionTest {

    @ParameterizedTest
    @ValueSource(strings = {"{}", "{\"id\":null}", "{\"id\":\"  \"}", "null"})
    @DisplayName("id가 없거나 비어 있는 요소는 몇 번째 요소인지와 함께 거부한다")
    void rejectsElementWithoutId(String element) {
        String message = InputTesting.rejectionOf(InputTesting.oneCluster("{\"id\":\"a\"}," + element));

        assertTrue(message.startsWith("Element #2 of cluster #1 has no id"), message);
    }

    @Test
    @DisplayName("서로 다른 무리에 있어도 같은 id를 두 번 쓰면 거부한다")
    void rejectsRepeatedIdAcrossClusters() {
        String json = "{\"clusters\":[{\"elements\":[{\"id\":\"a\"}]},{\"elements\":[{\"id\":\"a\"}]}]}";

        String message = InputTesting.rejectionOf(json);

        assertTrue(message.startsWith("Element id 'a' is used more than once"), message);
    }

    @Test
    @DisplayName("소, 중, 대가 아닌 규모 값은 기본값으로 바꾸지 않고 거부한다")
    void rejectsUnknownScale() {
        String message = InputTesting.rejectionOf(InputTesting.oneCluster("{\"id\":\"a\",\"scale\":\"huge\"}"));

        assertTrue(message.startsWith("Element 'a' has unknown scale 'huge'"), message);
    }

    @Test
    @DisplayName("한 무리에 중심 요소를 둘 표시하면 두 요소의 id와 함께 거부한다")
    void rejectsTwoCoreElementsInOneCluster() {
        String elements = "{\"id\":\"a\",\"core\":true},{\"id\":\"b\"},{\"id\":\"c\",\"core\":true}";

        String message = InputTesting.rejectionOf(InputTesting.oneCluster(elements));

        assertTrue(message.startsWith("Cluster #1 has more than one core element (a, c)"), message);
    }

    @Test
    @DisplayName("서로 다른 무리에는 중심 요소가 하나씩 있어도 된다")
    void acceptsOneCoreElementPerCluster() {
        String first = "{\"elements\":[{\"id\":\"a\",\"core\":true}]}";
        String second = "{\"elements\":[{\"id\":\"b\",\"core\":true}]}";

        InputTesting.read("{\"clusters\":[" + first + "," + second + "]}");
    }

    @Test
    @DisplayName("빈 글자는 글자가 없는 것으로 넘기지 않고 거부한다")
    void rejectsBlankLabel() {
        String message = InputTesting.rejectionOf(InputTesting.oneCluster("{\"id\":\"a\",\"label\":\" \"}"));

        assertTrue(message.startsWith("Element 'a' has an empty label"), message);
    }
}
