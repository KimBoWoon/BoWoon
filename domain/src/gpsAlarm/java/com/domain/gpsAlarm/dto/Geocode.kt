package com.domain.gpsAlarm.dto

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Geocode(
    val addresses: List<Addresses>? = null,
    val errorMessage: String? = null,
    val meta: Meta? = null,
    val status: String? = null // OK
) : Parcelable

@Keep
@Parcelize
data class Addresses(
    val addressElements: List<AddressElement>? = null,
    val distance: Double? = null, // 20.925857741585514
    val englishAddress: String? = null, // 6, Buljeong-ro, Bundang-gu, Seongnam-si, Gyeonggi-do, Republic of Korea
    val jibunAddress: String? = null, // 경기도 성남시 분당구 정자동 178-1 그린팩토리
    val roadAddress: String? = null, // 경기도 성남시 분당구 불정로 6 그린팩토리
    val x: Double? = null, // 127.10522081658463
    val y: Double? = null // 37.35951219616309
) : Parcelable

@Keep
@Parcelize
data class AddressElement(
    val code: String? = null,
    val longName: String? = null, // 13561
    val shortName: String? = null,
    val types: List<String>? = null
) : Parcelable

@Keep
@Parcelize
data class Meta(
    val count: Int? = null, // 1
    val page: Int? = null, // 1
    val totalCount: Int? = null // 1
) : Parcelable