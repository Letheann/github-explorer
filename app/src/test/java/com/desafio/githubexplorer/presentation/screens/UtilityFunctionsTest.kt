package com.desafio.githubexplorer.presentation.screens

import androidx.compose.ui.graphics.Color
import com.desafio.githubexplorer.presentation.screens.detail.formatDate
import com.desafio.githubexplorer.presentation.screens.repositories.formatCount
import com.desafio.githubexplorer.presentation.screens.repositories.getLanguageColor
import org.junit.Assert.assertEquals
import org.junit.Test

class UtilityFunctionsTest {

    @Test
    fun formatCount_underThousand() {
        assertEquals("0", formatCount(0))
        assertEquals("1", formatCount(1))
        assertEquals("999", formatCount(999))
        assertEquals("500", formatCount(500))
    }

    @Test
    fun formatCount_thousands() {
        assertEquals("1.0K", formatCount(1000))
        assertEquals("1.5K", formatCount(1500))
        assertEquals("10.0K", formatCount(10000))
        assertEquals("999.9K", formatCount(999900))
    }

    @Test
    fun formatCount_millions() {
        assertEquals("1.0M", formatCount(1000000))
        assertEquals("1.5M", formatCount(1500000))
        assertEquals("10.0M", formatCount(10000000))
    }

    @Test
    fun formatDate_validIsoDate() {
        assertEquals("Dec 01, 2024", formatDate("2024-12-01T00:00:00Z"))
    }

    @Test
    fun formatDate_differentMonths() {
        assertEquals("Jan 15, 2024", formatDate("2024-01-15T12:30:00Z"))
        assertEquals("Jun 20, 2023", formatDate("2023-06-20T00:00:00Z"))
    }

    @Test
    fun formatDate_invalidDate() {
        assertEquals("invalid-da", formatDate("invalid-date-string"))
    }

    @Test
    fun getLanguageColor_kotlin() {
        val expected = Color(0xFFA97BFF)
        assertEquals(expected, getLanguageColor("Kotlin"))
        assertEquals(expected, getLanguageColor("kotlin"))
        assertEquals(expected, getLanguageColor("KOTLIN"))
    }

    @Test
    fun getLanguageColor_java() {
        assertEquals(Color(0xFFB07219), getLanguageColor("Java"))
    }

    @Test
    fun getLanguageColor_python() {
        assertEquals(Color(0xFF3572A5), getLanguageColor("Python"))
    }

    @Test
    fun getLanguageColor_javascript() {
        assertEquals(Color(0xFFF1E05A), getLanguageColor("JavaScript"))
    }

    @Test
    fun getLanguageColor_typescript() {
        assertEquals(Color(0xFF2B7489), getLanguageColor("TypeScript"))
    }

    @Test
    fun getLanguageColor_rust() {
        assertEquals(Color(0xFFDEA584), getLanguageColor("Rust"))
    }

    @Test
    fun getLanguageColor_go() {
        assertEquals(Color(0xFF00ADD8), getLanguageColor("Go"))
    }

    @Test
    fun getLanguageColor_swift() {
        assertEquals(Color(0xFFFFAC45), getLanguageColor("Swift"))
    }

    @Test
    fun getLanguageColor_cpp() {
        assertEquals(Color(0xFFF34B7D), getLanguageColor("C++"))
    }

    @Test
    fun getLanguageColor_csharp() {
        assertEquals(Color(0xFF178600), getLanguageColor("C#"))
    }

    @Test
    fun getLanguageColor_ruby() {
        assertEquals(Color(0xFF701516), getLanguageColor("Ruby"))
    }

    @Test
    fun getLanguageColor_php() {
        assertEquals(Color(0xFF4F5D95), getLanguageColor("PHP"))
    }

    @Test
    fun getLanguageColor_unknown() {
        val defaultColor = Color(0xFF8B949E)
        assertEquals(defaultColor, getLanguageColor("Unknown"))
        assertEquals(defaultColor, getLanguageColor("Brainfuck"))
        assertEquals(defaultColor, getLanguageColor(""))
    }
}
