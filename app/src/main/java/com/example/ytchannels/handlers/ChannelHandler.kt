package com.example.ytchannels.handlers

import com.example.ytchannels.models.Channel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ChannelHandler {
    var database : FirebaseDatabase
    var channelReference: DatabaseReference

    init {
        database = FirebaseDatabase.getInstance()
        channelReference = database.getReference("channels")
    }

    fun create (channel: Channel): Boolean{
        val id = channelReference.push().key
        channel.id = id

        channelReference.child(id!!).setValue(channel)
        return true
    }
    fun update (channel: Channel):Boolean{
        channelReference.child(channel.id!!).setValue(channel)
        return true
    }
    fun delete (channel: Channel):Boolean{
        channelReference.child(channel.id!!).removeValue()
        return true
    }
}