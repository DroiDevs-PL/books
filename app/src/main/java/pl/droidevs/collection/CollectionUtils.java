package pl.droidevs.collection;

import java.util.Collection;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public final class CollectionUtils {
    private CollectionUtils() {
    }

    public static <F, T> Collection<T> transform(Collection<F> fromCollection, Function<? super F, T> function) {
        return Observable.just(fromCollection)
                .flatMapIterable(list -> list)
                .map(function)
                .toList()
                .blockingGet();
    }

    public static <E> Collection<E> filter(Collection<E> unfiltered, Predicate<? super E> predicate) {
        return Observable.just(unfiltered)
                .flatMapIterable(list -> list)
                .filter(predicate)
                .toList()
                .blockingGet();
    }
}
