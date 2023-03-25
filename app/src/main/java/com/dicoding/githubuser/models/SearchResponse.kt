package com.dicoding.githubuser.models

import com.google.gson.annotations.SerializedName

data class SearchResponse(
	@field:SerializedName("total_count")
	val totalCount: Int = 0,

	@field:SerializedName("incomplete_results")
	val incompleteResults: Boolean? = null,

	@field:SerializedName("items")
	val items: ArrayList<SearchModel>? = null
)
