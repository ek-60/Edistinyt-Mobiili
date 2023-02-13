package com.example.androidapp.data.todo

import com.google.gson.annotations.SerializedName


data class Todos (

  @SerializedName("userId"    ) var userId    : Int?     = null,
  @SerializedName("id"        ) var id        : Int?     = null,
  @SerializedName("title"     ) var title     : String?  = null,
  @SerializedName("completed" ) var completed : Boolean? = null

)