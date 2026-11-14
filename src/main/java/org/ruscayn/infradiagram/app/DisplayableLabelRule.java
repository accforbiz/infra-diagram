package org.ruscayn.infradiagram.app;

import java.util.function.Predicate;
import org.ruscayn.infradiagram.input.InputRule;
import org.ruscayn.infradiagram.input.InvalidInputException;
import org.ruscayn.infradiagram.model.Element;
import org.ruscayn.infradiagram.model.Infrastructure;

/**
 * 글꼴이 그릴 수 없는 글자가 든 글자를 거부한다.
 *
 * 그릴 수 없는 글자는 빈 네모로 그려지고, 줄바꿈 같은 제어 문자는 한 줄로 놓는 자리 계산을 깨뜨린다.
 * 그런 그림을 조용히 내놓는 대신 입력을 거부한다.
 */
final class DisplayableLabelRule implements InputRule {

    private final Predicate<String> displayable;

    DisplayableLabelRule(Predicate<String> displayable) {
        this.displayable = displayable;
    }

    @Override
    public void check(Infrastructure infrastructure) {
        infrastructure.elements().stream().filter(Element::hasLabel).forEach(this::check);
    }

    private void check(Element element) {
        String label = element.label().orElseThrow();
        boolean hasControlCharacter = label.chars().anyMatch(Character::isISOControl);
        if (hasControlCharacter || !displayable.test(label)) {
            String message = "Label of element '%s' contains a character that cannot be drawn. "
                    + "Use letters, digits, and common symbols on a single line.";
            throw new InvalidInputException(message.formatted(element.id()));
        }
    }
}
