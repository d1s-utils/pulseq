/*
 * BSD 3-Clause License, Copyright (c) 2021-2022, Pulseq and contributors.
 */

package uno.d1s.pulseq.converter

abstract class DtoConverterFacade<E, D> : DtoConverter<E, D> {

    override fun convertToDtoList(domains: List<E>): List<D> = domains.map {
        this.convertToDto(it)
    }

    override fun convertToDomainList(dtoList: List<D>): List<E> = dtoList.map {
        this.convertToDomain(it)
    }
}