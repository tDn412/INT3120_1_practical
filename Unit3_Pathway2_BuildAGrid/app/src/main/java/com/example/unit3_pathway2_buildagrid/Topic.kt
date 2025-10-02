package com.example.unit3_pathway2_buildagrid

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Topic(
    @StringRes val stringResourceId: Int,
    val number: Int,
    @DrawableRes val imageResourceId: Int
)
