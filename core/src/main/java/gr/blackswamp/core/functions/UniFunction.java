package gr.blackswamp.core.functions;

import java.util.Objects;

public interface UniFunction<P, R> {
    R call(P param);
}
