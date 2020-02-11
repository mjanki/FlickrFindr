package org.br.repository

import org.junit.Assert
import org.junit.Test
import org.br.database.models.ErrorNetworkDatabaseEntity
import org.br.network.models.ErrorNetworkEntity
import org.br.repository.mappers.ErrorNetworkRepoDatabaseMapper
import org.br.repository.mappers.ErrorNetworkRepoNetworkMapper
import org.br.repository.models.ErrorNetworkRepoEntity
import org.br.util.enums.ErrorNetworkTypes

class ErrorNetworkRepoMapperTest {
    // Mock Entities
    private var testErrorNetworkRepoEntity = ErrorNetworkRepoEntity(
            id = 51L,
            type = ErrorNetworkTypes.HTTP,
            action = "TESTING"
    )

    private var testErrorNetworkEntity = ErrorNetworkEntity(
            type = ErrorNetworkTypes.HTTP,
            action = "TESTING"
    )

    private var testErrorNetworkDatabaseEntity = ErrorNetworkDatabaseEntity(
            id = 51L,
            type = ErrorNetworkTypes.HTTP,
            action = "TESTING"
    )

    // ErrorNetworkRepoNetworkMapper Tests
    @Test
    fun errorNetworkRepoNetworkMapperDownstream_shouldMapCorrectly() {
        val mapper = ErrorNetworkRepoNetworkMapper()
        val mappedValue = mapper.downstream(testErrorNetworkRepoEntity)

        Assert.assertEquals(testErrorNetworkRepoEntity.type, mappedValue.type)
        Assert.assertEquals(testErrorNetworkRepoEntity.action, mappedValue.action)
    }

    @Test
    fun errorNetworkRepoNetworkMapperUpstream_shouldMapCorrectly() {
        val mapper = ErrorNetworkRepoNetworkMapper()
        val mappedValue = mapper.upstream(testErrorNetworkEntity)

        Assert.assertEquals(testErrorNetworkEntity.type, mappedValue.type)
        Assert.assertEquals(testErrorNetworkEntity.action, mappedValue.action)
    }

    // ErrorNetworkRepoDatabaseMapper Tests
    @Test
    fun errorNetworkRepoDatabaseMapperDownstream_shouldMapCorrectly() {
        val mapper = ErrorNetworkRepoDatabaseMapper()
        val mappedValue = mapper.downstream(testErrorNetworkRepoEntity)

        Assert.assertEquals(testErrorNetworkRepoEntity.id, mappedValue.id)
        Assert.assertEquals(testErrorNetworkRepoEntity.type, mappedValue.type)
        Assert.assertEquals(testErrorNetworkRepoEntity.action, mappedValue.action)
    }

    @Test
    fun errorNetworkRepoDatabaseMapperUpstream_shouldMapCorrectly() {
        val mapper = ErrorNetworkRepoDatabaseMapper()
        val mappedValue = mapper.upstream(testErrorNetworkDatabaseEntity)

        Assert.assertEquals(testErrorNetworkDatabaseEntity.type, mappedValue.type)
        Assert.assertEquals(testErrorNetworkDatabaseEntity.action, mappedValue.action)
    }
}