package com.bowoon.gps_alarm.data

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
@Parcelize
data class Geocode(
    @SerialName("addresses")
    val addresses: List<Addresses>? = null,
    @SerialName("errorMessage")
    val errorMessage: String? = null,
    @SerialName("meta")
    val meta: Meta? = null,
    @SerialName("status")
    val status: String? = null // OK
) : Parcelable

@Keep
@Serializable
@Parcelize
data class Addresses(
    @SerialName("addressElements")
    val addressElements: List<AddressElement>? = null,
    @SerialName("distance")
    val distance: Double? = null, // 20.925857741585514
    @SerialName("englishAddress")
    val englishAddress: String? = null, // 6, Buljeong-ro, Bundang-gu, Seongnam-si, Gyeonggi-do, Republic of Korea
    @SerialName("jibunAddress")
    val jibunAddress: String? = null, // 경기도 성남시 분당구 정자동 178-1 그린팩토리
    @SerialName("roadAddress")
    val roadAddress: String? = null, // 경기도 성남시 분당구 불정로 6 그린팩토리
    @SerialName("x")
    val longitude: Double? = null, // 127.10522081658463
    @SerialName("y")
    val latitude: Double? = null // 37.35951219616309
) : Parcelable

@Keep
@Serializable
@Parcelize
data class AddressElement(
    @SerialName("code")
    val code: String? = null,
    @SerialName("longName")
    val longName: String? = null, // 13561
    @SerialName("shortName")
    val shortName: String? = null,
    @SerialName("types")
    val types: List<String>? = null
) : Parcelable

@Keep
@Serializable
@Parcelize
data class Meta(
    @SerialName("count")
    val count: Int? = null, // 1
    @SerialName("page")
    val page: Int? = null, // 1
    @SerialName("totalCount")
    val totalCount: Int? = null // 1
) : Parcelable