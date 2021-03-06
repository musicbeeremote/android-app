package com.kelsos.mbrc.features.library.presentation.screens

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.kelsos.mbrc.R
import com.kelsos.mbrc.common.Meta
import com.kelsos.mbrc.features.library.MenuItemSelectedListener
import com.kelsos.mbrc.features.library.PopupActionHandler
import com.kelsos.mbrc.features.library.data.Artist
import com.kelsos.mbrc.features.library.presentation.ArtistAdapter
import com.kelsos.mbrc.features.library.presentation.ArtistViewModel
import com.kelsos.mbrc.features.library.presentation.LibraryViewHolder
import com.kelsos.mbrc.features.queue.Queue.Default
import com.kelsos.mbrc.features.work.WorkHandler
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

typealias OnArtistPressed = (artist: Artist) -> Unit

class ArtistScreen(
  private val adapter: ArtistAdapter,
  private val workHandler: WorkHandler,
  private val viewModel: ArtistViewModel,
  private val actionHandler: PopupActionHandler
) : LibraryScreen,
  MenuItemSelectedListener<Artist> {
  private var viewHolder: LibraryViewHolder? = null
  private var onArtistPressedListener: OnArtistPressed? = null

  fun setOnArtistPressedListener(onArtistPressedListener: OnArtistPressed? = null) {
    this.onArtistPressedListener = onArtistPressedListener
  }

  override fun bind(viewHolder: LibraryViewHolder) {
    this.viewHolder = viewHolder
    viewHolder.setup(R.string.artists_list_empty, adapter)
    adapter.setMenuItemSelectedListener(this)
  }

  override fun observe(viewLifecycleOwner: LifecycleOwner) {
    val lifecycleScope = viewLifecycleOwner.lifecycleScope
    lifecycleScope.launch {
      adapter.loadStateFlow.drop(1).distinctUntilChangedBy { it.refresh }.collect {
        viewHolder?.refreshingComplete(adapter.itemCount == 0)
      }
    }
    lifecycleScope.launch {
      viewModel.artists.collect {
        adapter.submitData(it)
      }
    }
  }

  override fun onMenuItemSelected(itemId: Int, item: Artist) {
    val action = actionHandler.genreSelected(itemId)
    if (action == Default) {
      onItemClicked(item)
    } else {
      workHandler.queue(item.id, Meta.Artist, action)
    }
  }

  override fun onItemClicked(item: Artist) {
    onArtistPressedListener?.invoke(item)
  }
}
