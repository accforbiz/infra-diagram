package org.ruscayn.infradiagram.input;

import org.ruscayn.infradiagram.model.Infrastructure;

public interface InputRule {

    void check(Infrastructure infrastructure);
}
