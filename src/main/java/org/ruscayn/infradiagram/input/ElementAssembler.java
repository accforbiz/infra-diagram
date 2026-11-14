package org.ruscayn.infradiagram.input;

import java.util.Optional;
import org.ruscayn.infradiagram.input.RawInfrastructure.RawElement;
import org.ruscayn.infradiagram.model.Element;
import org.ruscayn.infradiagram.model.Scale;

final class ElementAssembler {

    Element assemble(RawElement raw, String position) {
        String id = id(raw, position);
        boolean core = Boolean.TRUE.equals(raw.core());
        return new Element(id, scale(raw.scale(), id), core, label(raw.label(), id));
    }

    private String id(RawElement raw, String position) {
        if (raw == null || raw.id() == null || raw.id().isBlank()) {
            throw new InvalidInputException("%s has no id. Every element needs an id.".formatted(position));
        }
        return raw.id();
    }

    private Scale scale(String text, String id) {
        return switch (text) {
            case null -> Scale.MEDIUM;
            case "small" -> Scale.SMALL;
            case "medium" -> Scale.MEDIUM;
            case "large" -> Scale.LARGE;
            default -> throw unknownScale(text, id);
        };
    }

    private InvalidInputException unknownScale(String text, String id) {
        String message = "Element '%s' has unknown scale '%s'. Use small, medium, or large.";
        return new InvalidInputException(message.formatted(id, text));
    }

    private Optional<String> label(String text, String id) {
        if (text == null) {
            return Optional.empty();
        }
        if (text.isBlank()) {
            String message = "Element '%s' has an empty label. Remove the label or give it text.";
            throw new InvalidInputException(message.formatted(id));
        }
        return Optional.of(text);
    }
}
