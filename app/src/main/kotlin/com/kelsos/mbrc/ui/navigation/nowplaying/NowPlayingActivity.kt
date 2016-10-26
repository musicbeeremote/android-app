package com.kelsos.mbrc.ui.navigation.nowplaying

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.view.MenuItemCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import butterknife.BindView
import butterknife.ButterKnife
import com.kelsos.mbrc.R
import com.kelsos.mbrc.adapters.NowPlayingAdapter
import com.kelsos.mbrc.adapters.SimpleItemTouchHelperCallback
import com.kelsos.mbrc.domain.QueueTrack
import com.kelsos.mbrc.events.ui.TrackInfoChangeEvent
import com.kelsos.mbrc.events.ui.TrackMoved
import com.kelsos.mbrc.events.ui.TrackRemoval
import com.kelsos.mbrc.ui.navigation.nowplaying.NowPlayingPresenter
import com.kelsos.mbrc.ui.activities.BaseActivity
import com.kelsos.mbrc.ui.navigation.nowplaying.NowPlayingView
import toothpick.Toothpick

class NowPlayingActivity : BaseActivity(), SearchView.OnQueryTextListener, NowPlayingAdapter.OnUserActionListener, NowPlayingView {

  @BindView(R.id.now_playing_recycler) lateinit var recyclerView: RecyclerView
  @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
  @BindView(R.id.drawer_layout) lateinit var drawer: DrawerLayout
  @BindView(R.id.navigation_view) lateinit var navigationView: NavigationView
  lateinit var adapter: NowPlayingAdapter
  lateinit var presenter: NowPlayingPresenter
  private var layoutManager: LinearLayoutManager? = null
  private var mSearchView: SearchView? = null
  private var mSearchItem: MenuItem? = null

  override fun onDestroy() {
    super.onDestroy()
    Toothpick.closeScope(this)
  }

  override fun onStart() {
    super.onStart()
  }

  fun handlePlayingTrackChange(event: TrackInfoChangeEvent) {
    if (adapter.javaClass != NowPlayingAdapter::class.java) {
      return
    }
    val info = event.trackInfo
    val track = QueueTrack()

    track.artist = info.artist
    track.title = info.title
    adapter.setPlayingTrack(track)
  }

  override fun onQueryTextSubmit(query: String): Boolean {
    mSearchView!!.isIconified = true
    MenuItemCompat.collapseActionView(mSearchItem)
    return false
  }

  override fun onQueryTextChange(newText: String): Boolean {
    return true
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_now_playing, menu)
    mSearchItem = menu.findItem(R.id.now_playing_search_item)
    mSearchView = MenuItemCompat.getActionView(mSearchItem) as SearchView
    mSearchView!!.queryHint = getString(R.string.now_playing_search_hint)
    mSearchView!!.setIconifiedByDefault(true)
    mSearchView!!.setOnQueryTextListener(this)
    return true
  }

  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val scope = Toothpick.openScopes(application, this)
    Toothpick.inject(this, scope)
    setContentView(R.layout.activity_now_playing)
    ButterKnife.bind(this)
    initialize(toolbar,drawer,navigationView)
    setCurrentSelection(R.id.drawer_menu_now_playing)
    presenter.bind(this)

    layoutManager = LinearLayoutManager(baseContext)
    recyclerView.layoutManager = layoutManager
    adapter.setOnUserActionListener(this)
    recyclerView.adapter = adapter
    val callback = SimpleItemTouchHelperCallback(adapter)
    val touchHelper = ItemTouchHelper(callback)
    touchHelper.attachToRecyclerView(recyclerView)

    presenter.loadData()
  }

  fun handleTrackMoved(event: TrackMoved) {
    // In case the action failed revert the change
    if (!event.isSuccess) {
      adapter.restorePositions(event.from, event.to)
    }
  }

  fun handleTrackRemoval(event: TrackRemoval) {
    // In case the action failed revert the change
    if (!event.isSuccess) {
      // TODO: 8/18/15 fix caching of track until successful removal
      //adapter.insert(track, event.getIndex());
    }
  }

  override fun onItemRemoved(position: Int) {
    presenter.removeItem(position)
  }

  override fun onItemMoved(from: Int, to: Int) {
    presenter.moveItem(from, to)
  }

  override fun updatePlayingTrack(track: QueueTrack) {
    adapter.setPlayingTrack(track)
  }

  override fun updateAdapter(data: List<QueueTrack>) {
    adapter.updateData(data)
  }

  override fun onItemClicked(position: Int, track: QueueTrack) {
    presenter.playTrack(track)
  }

  override fun onBackPressed() {
    ActivityCompat.finishAfterTransition(this)
  }
}