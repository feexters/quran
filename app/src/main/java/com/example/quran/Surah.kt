package com.example.quran

data class Surah(val ayahs: Array<Ayah>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Surah

        return ayahs.contentEquals(other.ayahs)
    }

    override fun hashCode(): Int {
        return ayahs.contentHashCode()
    }
}
