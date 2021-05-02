package com.example.expensecontrol.models

class Expense {
    var color: String? = null
    var title: String? = null
    var iconId: Int? = null

    constructor()
    constructor(color: String?, title: String?, iconId: Int?) {
        this.color = color
        this.title = title
        this.iconId = iconId
    }

}