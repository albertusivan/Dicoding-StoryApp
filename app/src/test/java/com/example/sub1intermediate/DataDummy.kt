package com.example.sub1intermediate

import com.example.sub1intermediate.data.model.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val quote = ListStoryItem(
                "https://avatars.githubusercontent.com/u/71594935?s=400&u=2d803648ba08fc16959262dc8b2c1802ca501284&v=4",
                "2023-06-04",
                "Albertus Ivan Martino",
                "Testing",
                "107.6305251",
                "story-2WDh_axScG5U6zDl",
                "-6.978778"
            )
            items.add(quote)
        }
        return items
    }
}