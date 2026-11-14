package org.ruscayn.infradiagram.app;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ruscayn.infradiagram.input.InvalidInputException;
import org.ruscayn.infradiagram.model.Cluster;
import org.ruscayn.infradiagram.model.Element;
import org.ruscayn.infradiagram.model.Infrastructure;
import org.ruscayn.infradiagram.model.Scale;

class DisplayableLabelRuleTest {

    private final DisplayableLabelRule rule = new DisplayableLabelRule(label -> !label.contains("?"));

    @Test
    @DisplayName("글꼴이 그릴 수 없는 글자가 든 글자는 빈 네모로 그리지 않고 그 요소의 id와 함께 거부한다")
    void rejectsLabelTheFontCannotDraw() {
        InvalidInputException error = assertThrows(InvalidInputException.class, () -> rule.check(labeled("A?")));

        assertTrue(error.getMessage().startsWith("Label of element 'a' contains a character that cannot be drawn."));
    }

    @Test
    @DisplayName("줄바꿈이 든 글자는 글꼴이 그릴 수 있다고 해도 거부한다")
    void rejectsLabelWithLineBreak() {
        assertThrows(InvalidInputException.class, () -> rule.check(labeled("A\nB")));
    }

    @Test
    @DisplayName("그릴 수 있는 글자와, 글자가 없는 요소는 받아들인다")
    void acceptsDrawableLabelAndMissingLabel() {
        assertDoesNotThrow(() -> rule.check(labeled("데이터 레이크 A-1")));
        assertDoesNotThrow(() -> rule.check(infrastructure(Optional.empty())));
    }

    private Infrastructure labeled(String label) {
        return infrastructure(Optional.of(label));
    }

    private Infrastructure infrastructure(Optional<String> label) {
        Element element = new Element("a", Scale.MEDIUM, false, label);
        return new Infrastructure(List.of(new Cluster(List.of(element))), List.of());
    }
}
