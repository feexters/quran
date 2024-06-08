package com.example.quran

data class Quran(val pages: Array<Page>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Quran

        return pages.contentEquals(other.pages)
    }

    override fun hashCode(): Int {
        return pages.contentHashCode()
    }
}
