/*
 * BSD 3-Clause License, Copyright (c) 2021, Pulseq and contributors.
 */

package uno.d1s.pulseq.service

import uno.d1s.pulseq.domain.Holder
import uno.d1s.pulseq.dto.SimpleTimeSpan

interface HolderService {

    fun findById(id: String): Holder

    fun findAll(): List<Holder>

    fun findAllCreatedIn(timeSpan: SimpleTimeSpan): List<Holder>

    fun findAllCreatedIn(timeSpan: SimpleTimeSpan, ids: List<String>): List<Holder>

    fun remove(id: String): Holder

    fun remove(holder: Holder): Holder

    fun removeAll(): List<Holder>

    fun removeAll(ids: List<String>): List<Holder>

    fun removeAllByHolders(holders: List<Holder>): List<Holder>

    fun create(name: String): Holder

    fun createAll(names: List<String>): List<Holder>
}