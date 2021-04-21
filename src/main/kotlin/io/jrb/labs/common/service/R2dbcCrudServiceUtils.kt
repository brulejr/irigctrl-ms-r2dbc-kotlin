package io.jrb.labs.common.service

import io.jrb.labs.common.model.Entity
import io.jrb.labs.common.repository.EntityRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class R2dbcCrudServiceUtils<E: Entity>(
    override val entityClass: Class<E>,
    override val repository: EntityRepository<E>,
    override val entityName: String = entityClass.simpleName
) : CrudServiceUtils<E> {

    override fun createEntity(entity: E): Mono<E> {
        return repository.save(entity)
            .onErrorResume(handleServiceError("Unexpected error when creating $entityName"))
    }

    override fun deleteEntity(name: String): Mono<Void> {
        return repository.findByName(name)
            .switchIfEmpty(Mono.error(ResourceNotFoundException(entityName, name)))
            .flatMap { repository.delete(it) }
            .onErrorResume(handleServiceError("Unexpected error when deleting $entityName"))
    }

    override fun findEntityByName(name: String): Mono<E> {
        return repository.findByName(name)
            .switchIfEmpty(Mono.error(ResourceNotFoundException(entityName, name)))
            .onErrorResume(handleServiceError("Unexpected error when finding $entityName"))
    }

    override fun listAllEntities(): Flux<E> {
        return repository.findAll()
            .onErrorResume(handleServiceError("Unexpected error when retrieving $entityName"))
    }

}
