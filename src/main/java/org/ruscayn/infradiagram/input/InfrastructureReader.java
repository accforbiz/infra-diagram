package org.ruscayn.infradiagram.input;

import java.nio.file.Path;
import org.ruscayn.infradiagram.model.Infrastructure;

public interface InfrastructureReader {

    Infrastructure read(Path file);
}
