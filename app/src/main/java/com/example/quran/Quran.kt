package com.example.quran

data class Quran(val surahs: Array<Surah>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Quran

        return surahs.contentEquals(other.surahs)
    }

    override fun hashCode(): Int {
        return surahs.contentHashCode()
    }
}
