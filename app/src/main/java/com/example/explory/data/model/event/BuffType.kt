package com.example.explory.data.model.event

import com.example.explory.R

enum class BuffType(val animationResource: Int, val title: String) {
    COINS(R.raw.coins, "Увеличение монет"),
    EXPERIENCE(R.raw.exp, "Увеличение опыта");

    companion object {
        fun getAnimationResource(buffType: BuffType): Int {
            return buffType.animationResource
        }

        fun getTitle(buffType: BuffType): String {
            return buffType.title
        }
    }
}