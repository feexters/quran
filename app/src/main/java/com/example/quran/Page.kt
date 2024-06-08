package com.example.quran

data class Page(val ayahs: Array<Ayah>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Page

        return ayahs.contentEquals(other.ayahs)
    }

    override fun hashCode(): Int {
        return ayahs.contentHashCode()
    }
}
