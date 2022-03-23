package util;

/**
 * A record with 2 elements of different types
 * @param <A> type 1
 * @param <B> type 2
 */
public record Tuple2<A, B>(A a, B b){}