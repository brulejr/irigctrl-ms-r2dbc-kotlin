/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021 Jon Brule <brulejr@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.jrb.labs.common.service

import io.jrb.labs.common.model.Entity
import io.jrb.labs.common.repository.NamedEntityRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class R2dbcCrudServiceUtils<E: Entity>(
    override val entityClass: Class<E>,
    override val repository: NamedEntityRepository<E>,
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
