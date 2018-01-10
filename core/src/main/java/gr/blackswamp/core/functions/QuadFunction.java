package gr.blackswamp.core.functions;

public interface QuadFunction<P1, P2, P3, P4, R> {
    R call(P1 param1, P2 param2, P3 param3, P4 param4);
}
