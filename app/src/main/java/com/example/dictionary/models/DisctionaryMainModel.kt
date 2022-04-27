package com.example.dictionary.models

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import com.example.dictionary.models.Meaning

class DisctionaryMainModel {
    @SerializedName("meanings")
    @Expose
    var meanings: List<Meaning>? = null
}