package com.example.dictionary.models

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import com.example.dictionary.models.Definition

class Meaning {
    @SerializedName("definitions")
    @Expose
    var definitions: List<Definition>? = null

}