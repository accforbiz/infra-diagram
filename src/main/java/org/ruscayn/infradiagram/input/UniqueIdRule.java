package org.ruscayn.infradiagram.input;

import java.util.HashSet;
import java.util.Set;
import org.ruscayn.infradiagram.model.Element;
import org.ruscayn.infradiagram.model.Infrastructure;

final class UniqueIdRule implements InputRule {

    @Override
    public void check(Infrastructure infrastructure) {
        Set<String> seen = new HashSet<>();
        for (Element element : infrastructure.elements()) {
            if (!seen.add(element.id())) {
                String message = "Element id '%s' is used more than once. Ids must be unique.";
                throw new InvalidInputException(message.formatted(element.id()));
            }
        }
    }
}
