package com.kelsos.mbrc.ui.navigation.library.tracks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kelsos.mbrc.R
import com.kelsos.mbrc.content.library.tracks.Track
import com.kelsos.mbrc.databinding.FragmentBrowseBinding
import com.kelsos.mbrc.ui.navigation.library.PopupActionHandler
import com.kelsos.mbrc.ui.navigation.library.tracks.TrackEntryAdapter.MenuItemSelectedListener
import toothpick.Toothpick
import javax.inject.Inject

class BrowseTrackFragment : Fragment(), BrowseTrackView, MenuItemSelectedListener {

  @Inject
  lateinit var adapter: TrackEntryAdapter

  @Inject
  lateinit var actionHandler: PopupActionHandler

  @Inject
  lateinit var presenter: BrowseTrackPresenter

  private var _binding: FragmentBrowseBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentBrowseBinding.inflate(inflater)
    return binding.root
  }

  override fun search(term: String) {
    binding.libraryBrowserSync.isGone = term.isNotEmpty()
  }

  override fun queue(success: Boolean, tracks: Int) {
    val message = if (success) {
      getString(R.string.queue_result__success, tracks)
    } else {
      getString(R.string.queue_result__failure)
    }
    Snackbar.make(requireView(), R.string.queue_result__success, Snackbar.LENGTH_SHORT)
      .setText(message)
      .show()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    val scope = Toothpick.openScopes(requireActivity().application, this)
    scope.installModules(BrowseTrackModule())
    super.onCreate(savedInstanceState)
    Toothpick.inject(this, scope)
    adapter.setCoverMode(true)
    presenter.attach(this)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    presenter.detach()
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.libraryBrowserTextTitle.setText(R.string.tracks_list_empty)
    binding.libraryBrowserSync.setOnClickListener {
      presenter.sync()
    }
    binding.libraryBrowserContent.adapter = adapter
    binding.libraryBrowserContent.layoutManager = LinearLayoutManager(requireContext())
    binding.libraryBrowserContent.setHasFixedSize(true)
    adapter.setMenuItemSelectedListener(this)
    presenter.load()
  }

  override suspend fun update(tracks: PagingData<Track>) {
    adapter.submitData(tracks)
    binding.libraryBrowserEmptyGroup.isGone = adapter.itemCount != 0
  }

  override fun onMenuItemSelected(@IdRes itemId: Int, track: Track) {
    presenter.queue(track, actionHandler.trackSelected(itemId))
  }

  override fun onItemClicked(track: Track) {
    presenter.queue(track)
  }

  override fun hideLoading() {
    binding.libraryBrowserLoadingBar.isGone = true
  }
}
