package com.example.explory.common

fun formatNumber(value: Int): String {
    return when {
        value >= 1_000_000_000 -> "${value / 1_000_000_000}B"
        value >= 1_000_000 -> "${value / 1_000_000}M"
        value >= 1_000 -> "${value / 1_000}K"
        else -> value.toString()
    }
}