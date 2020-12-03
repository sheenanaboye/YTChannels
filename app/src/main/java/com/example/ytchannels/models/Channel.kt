package com.example.ytchannels.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Channel (var id: String? = "", var title: String? = "", var link: String? = "", var rank: Int = 0 , var reason: String? = "") : Comparable<Channel>{
    override fun toString(): String {
        return "$rank $title : $reason"
    }

    override fun compareTo(other: Channel): Int {
        return if(this.rank != other.rank){
            this.rank - other.rank
        }else{
            0
        }
    }

}
}