package com.hafidrf.dominictoretto.model

data class Friend(
    var id: String,
    var name: String,
    var borrowed_tools: ArrayList<Tool>,
    var is_selected: Boolean
)