package com.kiki.post8.model

data class Todo(
    var id: String? = null,
    var title: String = "",
    var description: String = "",
    var deadline: String = "",
    var completed: Boolean = false
)
