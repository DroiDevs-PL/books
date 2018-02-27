package pl.droidevs.data.repository.reactive;

import java.util.Collection;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import pl.droidevs.data.repository.Repository;

public interface RxCrudRepository<E, I> extends Repository<E, I> {
    Completable delete(E entity);

    Completable deleteAll(Collection<? extends E> entities);

    Flowable<Collection<E>> fetchAll();

    Maybe<E> fetchById(I id);

    Single<E> save(E entity);

    Single<Collection<E>> saveAll(Collection<E> entities);
}
