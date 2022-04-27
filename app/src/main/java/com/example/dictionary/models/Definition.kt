package com.example.dictionary.models

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class Definition {
    @SerializedName("definition")
    @Expose
    var definition: String? = null

}