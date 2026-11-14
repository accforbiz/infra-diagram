package org.ruscayn.infradiagram.input;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class LinkRejectionTest {

    private static final String TWO_ELEMENTS = "{\"clusters\":[{\"elements\":[{\"id\":\"a\"},{\"id\":\"b\"}]}],\"links\":[%s]}";

    @ParameterizedTest
    @ValueSource(strings = {"{}", "{\"between\":[\"a\"]}", "{\"between\":[\"a\",\"b\",\"a\"]}", "{\"between\":[\"a\",null]}"})
    @DisplayName("요소 둘을 잇지 않는 연결은 몇 번째 연결인지와 함께 거부한다")
    void rejectsLinkThatDoesNotNameTwoElements(String link) {
        String message = InputTesting.rejectionOf(TWO_ELEMENTS.formatted(link));

        assertTrue(message.startsWith("Link #1 must name exactly two element ids"), message);
    }

    @Test
    @DisplayName("없는 요소를 가리키는 연결은 빼고 그리지 않고 그 id와 함께 거부한다")
    void rejectsLinkToUnknownElement() {
        String message = InputTesting.rejectionOf(TWO_ELEMENTS.formatted("{\"between\":[\"a\",\"z\"]}"));

        assertTrue(message.startsWith("Link #1 refers to unknown element 'z'"), message);
    }

    @Test
    @DisplayName("요소를 자기 자신과 잇는 연결은 거부한다")
    void rejectsLinkFromElementToItself() {
        String message = InputTesting.rejectionOf(TWO_ELEMENTS.formatted("{\"between\":[\"a\",\"a\"]}"));

        assertTrue(message.startsWith("Link #1 joins element 'a' to itself"), message);
    }

    @Test
    @DisplayName("연결에는 방향이 없으므로 순서만 뒤집어 다시 적은 연결도 중복으로 거부한다")
    void rejectsSameLinkWrittenInReverse() {
        String links = "{\"between\":[\"a\",\"b\"]},{\"between\":[\"b\",\"a\"]}";

        String message = InputTesting.rejectionOf(TWO_ELEMENTS.formatted(links));

        assertTrue(message.startsWith("Link #2 repeats an earlier link"), message);
    }
}
