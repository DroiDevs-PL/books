package pl.droidevs.data.repository.reactive;

import java.util.Collection;

import io.reactivex.Flowable;

public interface FilterableRxCrudRepository<E, ID, F> extends RxCrudRepository<E, ID> {
    Flowable<Collection<E>> fetchBy(F filter);
}
