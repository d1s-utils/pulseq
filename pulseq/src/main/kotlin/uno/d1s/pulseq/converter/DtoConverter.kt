package uno.d1s.pulseq.converter

interface DtoConverter<E, D> {

    fun convertToDto(domain: E): D

    fun convertToDomain(dto: D): E

    fun convertToDtoList(domains: List<E>): List<D>

    fun convertToDomainList(dtoList: List<D>): List<E>
}