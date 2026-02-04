package com.zsasko.rawg.data.model

interface GenericPaginatedResponse<T> {
    val count: Int
    val next: String?
    val previous: String?
    val results: List<T>
}