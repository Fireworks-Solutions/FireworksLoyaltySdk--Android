package com.fireworks.myeventsdk.model.wallet.multiple

import com.google.gson.annotations.SerializedName

data class WalletItem(

	@field:SerializedName("img")
	val img: String? = null,

	@field:SerializedName("purchase_end")
	val purchaseEnd: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("redeem_status")
	val redeemStatus: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("gift_status")
	val giftStatus: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("mall")
	val mall: Int? = null,

	@field:SerializedName("point")
	val point: String? = null,

	@field:SerializedName("status")
	val itemStatus: String? = ""
)