package org.ruscayn.infradiagram.input;

import java.util.ArrayList;
import java.util.List;
import org.ruscayn.infradiagram.model.Infrastructure;

public final class InputRules implements InputRule {

    private final List<InputRule> rules;

    private InputRules(List<InputRule> rules) {
        this.rules = List.copyOf(rules);
    }

    public static InputRules standard(InputLimits limits) {
        return new InputRules(List.of(
                new UniqueIdRule(),
                new SingleCoreRule(),
                new LinkEndsRule(),
                new LimitsRule(limits)));
    }

    public InputRules and(InputRule extra) {
        List<InputRule> extended = new ArrayList<>(rules);
        extended.add(extra);
        return new InputRules(extended);
    }

    @Override
    public void check(Infrastructure infrastructure) {
        rules.forEach(rule -> rule.check(infrastructure));
    }
}
