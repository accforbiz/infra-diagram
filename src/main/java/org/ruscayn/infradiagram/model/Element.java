package org.ruscayn.infradiagram.model;

import java.util.Optional;

public record Element(String id, Scale scale, boolean core, Optional<String> label) {

    public boolean hasLabel() {
        return label.isPresent();
    }
}
