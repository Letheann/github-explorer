package com.desafio.githubexplorer.presentation

import org.junit.Assert.assertEquals
import org.junit.Test

class SortOptionTest {

    @Test
    fun stars_hasCorrectValues() {
        assertEquals("Stars", SortOption.STARS.displayName)
        assertEquals("stars", SortOption.STARS.apiValue)
    }

    @Test
    fun forks_hasCorrectValues() {
        assertEquals("Forks", SortOption.FORKS.displayName)
        assertEquals("forks", SortOption.FORKS.apiValue)
    }

    @Test
    fun updated_hasCorrectValues() {
        assertEquals("Last Updated", SortOption.UPDATED.displayName)
        assertEquals("updated", SortOption.UPDATED.apiValue)
    }

    @Test
    fun allOptionsAvailable() {
        assertEquals(3, SortOption.entries.size)
    }
}
