package com.hafidrf.dominictoretto.feature.friendsList

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hafidrf.dominictoretto.R
import com.hafidrf.dominictoretto.feature.ItemClickListener
import com.hafidrf.dominictoretto.feature.appToolbarTitle
import com.hafidrf.dominictoretto.feature.toolsList.ToolsListActivity.Companion.IS_DATA_UPDATED
import com.hafidrf.dominictoretto.model.Friend
import com.hafidrf.dominictoretto.model.Tool
import com.hafidrf.dominictoretto.util.showToast
import com.hafidrf.dominictoretto.util.visibleIfTrue
import com.hafidrf.dominictoretto.viewModel.ToolsTrackingViewModel
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.android.synthetic.main.activity_tools.*
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.tools_dialog.view.*

class FriendsListActivity : AppCompatActivity() {

    private lateinit var toolsTrackingViewModel: ToolsTrackingViewModel
    private var friendsList: List<Friend> = listOf()
    private var friendsListAdapter = FriendsListAdapter()
    private var isDataUpdated = false
    lateinit var mAddFab: ExtendedFloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tools)
        mAddFab = findViewById(R.id.extended_fab);

        setToolBar(getString(R.string.friends))
        toolsTrackingViewModel = ViewModelProviders.of(this).get(ToolsTrackingViewModel::class.java)

        getFriendsList()
        setFriendsListAdapter()
        mAddFab.hide()
    }

    private fun setToolBar(title: String) {
        toolbar.appToolbarTitle(title)
        toolbarNavIcon.visibleIfTrue(true)
        toolbarNavIcon.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getFriendsList() {
        friendsList = toolsTrackingViewModel.getFriendsData(this)
    }

    private fun setFriendsListAdapter() {
        friendsListAdapter.setItems(friendsList)
        friendsListAdapter.setFriendClickListener(friendItemClickListener)
        recycler_view.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_view.adapter = friendsListAdapter
    }

    private val friendItemClickListener = object : ItemClickListener {
        override fun onItemClick(position: Int) {
            val selectedFriend = friendsListAdapter.getItem(position)
            val toolsList = toolsTrackingViewModel.getBorrowedToolsList(selectedFriend.id)
            if (toolsList != null && toolsList.size > 0)
                showToolsDialog(toolsList, selectedFriend)
            else showToast(
                applicationContext,
                getString(R.string.not_borrowed_tool, selectedFriend.name)
            )
        }
    }

    private fun showToolsDialog(toolsList: ArrayList<Tool>, selectedFriend: Friend) {
        val view = layoutInflater.inflate(R.layout.tools_dialog, null)
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(view)
        val dialog = alertDialogBuilder.create()
        view.loan_tool_text_view.text = getString(R.string.tools_loaned, selectedFriend.name)
        view.ok_button.text = getString(R.string.close)
        var isItemSelected = false
        val friendToolsListAdapter = FriendToolsListAdapter()
        friendToolsListAdapter.setItems(toolsList)
        view.friends_recycler_view.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        if (toolsList.size > 5) {
            val params = view.friends_recycler_view.layoutParams
            params.height = 800
            view.friends_recycler_view.layoutParams = params
        }
        view.friends_recycler_view.adapter = friendToolsListAdapter
        var selectedTool: Tool? = null
        friendToolsListAdapter.setToolClickListener(object : ItemClickListener {
            override fun onItemClick(position: Int) {
                for (tool in toolsList) {
                    tool.is_selected = false
                }
                selectedTool = friendToolsListAdapter.getItem(position)
                selectedTool?.let {
                    it.is_selected = true
                    isItemSelected = true
                    view.loan_tool_text_view.text = getString(R.string.mark_return, it.name)
                    view.ok_button.text = getString(R.string.ok)
                    friendToolsListAdapter.notifyDataSetChanged()
                }
            }
        })
        view.ok_button.setOnClickListener {
            if (isItemSelected && selectedTool != null) {
                toolsTrackingViewModel.updateFriendsData(selectedFriend, selectedTool!!)
                showToast(
                    this,
                    getString(
                        R.string.return_successfully,
                        selectedTool!!.name,
                        selectedFriend.name
                    )
                )
                refreshList()
                dialog.dismiss()
            } else
                dialog.dismiss()
        }
        dialog.show()
    }

    private fun refreshList() {
        isDataUpdated = true
        getFriendsList()
        friendsListAdapter.setItems(friendsList)
    }

    override fun onBackPressed() {
        if (isDataUpdated) {
            val data = Intent()
            data.putExtra(IS_DATA_UPDATED, true)
            setResult(Activity.RESULT_OK, data)
        }
        super.onBackPressed()
    }
}