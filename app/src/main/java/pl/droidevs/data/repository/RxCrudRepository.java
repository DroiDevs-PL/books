package pl.droidevs.data.repository;

import java.util.Collection;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface RxCrudRepository<E, ID> extends Repository<E, ID> {
    Completable delete(E entity);

    Completable deleteAll(Collection<? extends E> entities);

    Flowable<Collection<E>> fetchAll();

    Maybe<E> fetchById(ID id);

    <S extends E> Single<S> save(S entity);

    <S extends E> Single<Iterable<S>> saveAll(Collection<S> entity);
}
