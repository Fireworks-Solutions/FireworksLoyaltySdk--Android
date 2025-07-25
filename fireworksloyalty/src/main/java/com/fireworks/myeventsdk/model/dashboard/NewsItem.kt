package com.fireworks.myeventsdk.model.dashboard

import com.google.gson.annotations.SerializedName

data class NewsItem(

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("featured_img")
	val featuredImage: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("title")
	val title: String? = null
)