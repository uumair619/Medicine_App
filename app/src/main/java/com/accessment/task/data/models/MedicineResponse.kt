package com.accessment.task.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MedicineResponse (

    @SerializedName("name"        ) var name        : String?                = null,
    @SerializedName("medications" ) var medications : ArrayList<AssociatedDrug> = arrayListOf(),
    @SerializedName("labs"        ) var labs        : ArrayList<Labs>        = arrayListOf()

) : Parcelable

@Parcelize
data class Labs (

    @SerializedName("name"   ) var name   : String? = null,
    @SerializedName("image"  ) var image  : String? = null,
    @SerializedName("detail" ) var detail : String? = null

) : Parcelable

@Parcelize
data class AssociatedDrug (

    @SerializedName("name"     ) var name     : String? = null,
    @SerializedName("dose"     ) var dose     : String? = null,
    @SerializedName("strength" ) var strength : String? = null,
    @SerializedName("image"    ) var image    : String? = null,
    @SerializedName("detail"   ) var detail   : String? = null

) : Parcelable