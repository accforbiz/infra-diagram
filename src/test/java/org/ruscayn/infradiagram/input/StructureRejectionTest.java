package org.ruscayn.infradiagram.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class StructureRejectionTest {

    @Test
    @DisplayName("JSON 문법이 틀리면 틀린 위치의 줄 번호와 함께 거부한다")
    void rejectsBrokenJson() {
        String message = InputTesting.rejectionOf("{ \"clusters\": [ }");

        assertTrue(message.startsWith("Invalid JSON at line 1"), message);
    }

    @Test
    @DisplayName("오타로 보이는 모르는 항목은 무시하지 않고, 항목의 경로와 쓸 수 있는 항목을 알리며 거부한다")
    void rejectsUnknownField() {
        String message = InputTesting.rejectionOf(InputTesting.oneCluster("{\"id\":\"a\",\"sclae\":\"large\"}"));

        String expected = "Unknown field 'sclae' at clusters[0].elements[0].sclae. "
                + "Allowed fields: core, id, label, scale.";
        assertEquals(expected, message);
    }

    @Test
    @DisplayName("글자 자리에 숫자를 적으면 글자로 바꿔 읽지 않고, 와야 할 값의 종류를 알리며 거부한다")
    void rejectsNumberWhereTextIsExpected() {
        String message = InputTesting.rejectionOf(InputTesting.oneCluster("{\"id\":5}"));

        assertTrue(message.startsWith("Wrong kind of value at line 1"), message);
        assertTrue(message.endsWith("(clusters[0].elements[0].id): expected text in double quotes."), message);
    }

    @Test
    @DisplayName("내용이 없는 입력은 비어 있다고 알리며 거부한다")
    void rejectsEmptyInput() {
        assertEquals("The input is empty.", InputTesting.rejectionOf("  "));
    }

    @Test
    @DisplayName("한 요소에 같은 항목을 두 번 적으면 뒤의 값을 쓰지 않고 거부한다")
    void rejectsRepeatedField() {
        String message = InputTesting.rejectionOf(InputTesting.oneCluster("{\"id\":\"a\",\"id\":\"b\"}"));

        assertTrue(message.startsWith("Invalid JSON"), message);
    }

    @ParameterizedTest
    @ValueSource(strings = {"null", "[]", "{}", "{\"clusters\":null}", "{\"clusters\":[]}"})
    @DisplayName("무리가 하나도 없는 입력은 거부한다")
    void rejectsInputWithoutClusters(String json) {
        String message = InputTesting.rejectionOf(json);

        assertTrue(message.contains("cluster") || message.contains("an object in curly braces"), message);
    }

    @ParameterizedTest
    @ValueSource(strings = {"{\"clusters\":[{}]}", "{\"clusters\":[{\"elements\":[]}]}", "{\"clusters\":[null]}"})
    @DisplayName("요소가 없는 빈 무리는 몇 번째 무리인지와 함께 거부한다")
    void rejectsEmptyCluster(String json) {
        String message = InputTesting.rejectionOf(json);

        assertTrue(message.startsWith("Cluster #1 has no elements"), message);
    }
}
