package org.ruscayn.infradiagram.input;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.ruscayn.infradiagram.model.Element;
import org.ruscayn.infradiagram.model.Infrastructure;
import org.ruscayn.infradiagram.model.Link;

final class LinkEndsRule implements InputRule {

    @Override
    public void check(Infrastructure infrastructure) {
        Set<String> ids = infrastructure.elements().stream().map(Element::id).collect(Collectors.toSet());
        List<Link> links = infrastructure.links();
        for (int index = 0; index < links.size(); index++) {
            checkEnds(links.get(index), index + 1, ids);
            checkNotRepeated(links, index);
        }
    }

    private void checkEnds(Link link, int linkNumber, Set<String> ids) {
        checkKnown(link.firstId(), linkNumber, ids);
        checkKnown(link.secondId(), linkNumber, ids);
        if (link.joinsElementToItself()) {
            String message = "Link #%d joins element '%s' to itself.";
            throw new InvalidInputException(message.formatted(linkNumber, link.firstId()));
        }
    }

    private void checkKnown(String id, int linkNumber, Set<String> ids) {
        if (!ids.contains(id)) {
            String message = "Link #%d refers to unknown element '%s'.";
            throw new InvalidInputException(message.formatted(linkNumber, id));
        }
    }

    private void checkNotRepeated(List<Link> links, int index) {
        Link link = links.get(index);
        boolean repeated = links.subList(0, index).stream().anyMatch(link::joinsSameElementsAs);
        if (repeated) {
            String message = "Link #%d repeats an earlier link between '%s' and '%s'.";
            throw new InvalidInputException(message.formatted(index + 1, link.firstId(), link.secondId()));
        }
    }
}
