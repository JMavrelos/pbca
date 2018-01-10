package gr.blackswamp.core.functions;

public interface BiFunction<P1, P2, R> {
    R call(P1 param1, P2 param2);
}
