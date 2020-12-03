package com.example.ytchannels

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.ytchannels.handlers.ChannelHandler
import com.example.ytchannels.models.Channel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var editTextTitle : EditText
    lateinit var editTextLink : EditText
    lateinit var editTextRank : EditText
    lateinit var editTextReason : EditText
    lateinit var addChannelButton : Button
    lateinit var channelHandler: ChannelHandler
    lateinit var channels: ArrayList<Channel>
    lateinit var channelsListView: ListView
    lateinit var channelEdited: Channel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextTitle = findViewById(R.id.editTextTitle)
        editTextLink = findViewById(R.id.editTextLink)
        editTextRank = findViewById(R.id.editTextRank)
        editTextReason = findViewById(R.id.editTextReason)
        addChannelButton = findViewById(R.id.addChannelButton)
        channelHandler = ChannelHandler()
        channels = ArrayList()
        channelsListView = findViewById(R.id.channelsListView)


        addChannelButton.setOnClickListener{
            val title = editTextTitle.text.toString()
            val link = editTextLink.text.toString()
            val rank = editTextRank.text.toString().toInt()
            val reason = editTextReason.text.toString()

            if(addChannelButton.text.toString() == "Add Channel") {
                val channel = Channel(title = title , link = link , rank = rank , reason = reason)
                if (channelHandler.create(channel)) {
                    Toast.makeText(applicationContext , "Youtube Channel Added." , Toast.LENGTH_SHORT).show()
                    clearFields()
                }
            }
            else if (addChannelButton.text.toString() == "Update Channel"){
                val channel = Channel(id = channelEdited.id, title = title, link = link, rank = rank, reason = reason)
                if(channelHandler.update(channel)){
                    Toast.makeText(applicationContext, "Youtube Channel Updated.", Toast.LENGTH_SHORT).show()
                    clearFields()
                }
            }
        }
        registerForContextMenu(channelsListView)
    }

    override fun onCreateContextMenu(menu: ContextMenu? , v: View? , menuInfo: ContextMenu.ContextMenuInfo?) {
        val inflater = menuInflater
        inflater.inflate(R.menu.channels_options, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        return when(item.itemId){

            R.id.edit_channel ->{
                channelEdited = channels[info.position]
                editTextTitle.setText(channelEdited.title)
                editTextLink.setText(channelEdited.link)
                editTextRank.setText(channelEdited.rank.toString())
                editTextReason.setText(channelEdited.reason)
                addChannelButton.text = "Update Channel"
                true
            }
            R.id.delete_channel -> {
                if(channelHandler.delete(channels[info.position])){
                    Toast.makeText(applicationContext, "Youtube Channel Deleted.", Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onContextItemSelected(item)
        }

    }

    fun clearFields (){
        editTextTitle.text.clear()
        editTextLink.text.clear()
        editTextRank.text.clear()
        editTextReason.text.clear()
        addChannelButton.text = "Add Channel"
    }
    override fun onStart(){
        super.onStart()
        channelHandler.channelReference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                channels.clear()
                snapshot.children.forEach{
                    val channel = it.getValue(Channel::class.java)
                    channels.add(channel!!)
                }
                channels.sort()
                val adapter = ArrayAdapter<Channel>(applicationContext,android.R.layout.simple_list_item_1, channels)
                channelsListView.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }
}