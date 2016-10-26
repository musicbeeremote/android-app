package com.kelsos.mbrc.ui.navigation.playlists.tracks

import com.kelsos.mbrc.annotations.Queue
import com.kelsos.mbrc.domain.PlaylistTrack
import com.kelsos.mbrc.extensions.task
import com.kelsos.mbrc.interactors.QueueInteractor
import com.kelsos.mbrc.interactors.playlists.PlaylistTrackInteractor
import timber.log.Timber
import javax.inject.Inject


class PlaylistTrackPresenterImpl
@Inject constructor(private val playlistTrackInteractor: PlaylistTrackInteractor,
                    private val queueInteractor: QueueInteractor): PlaylistTrackPresenter {
  private var view: PlaylistTrackView? = null

  override fun bind(view: PlaylistTrackView) {
    this.view = view
  }

  override fun load(longExtra: Long) {
    playlistTrackInteractor.execute(longExtra)
        .task()
        .subscribe({ view?.update(it) },
            {
              view?.showErrorWhileLoading()
              Timber.e(it, "")
            })
  }

  override fun queue(track: PlaylistTrack, @Queue.Action action: String) {
    if (track.path.isNullOrEmpty()) {
      return
    }

    queueInteractor.execute(action, track.path).subscribe({

    }) { Timber.e(it, "Queueing failed") }
  }
}