package com.kelsos.mbrc.ui.navigation.radio

import com.kelsos.mbrc.annotations.Queue.NOW
import com.kelsos.mbrc.mvp.BasePresenter
import com.kelsos.mbrc.repository.RadioRepository
import com.kelsos.mbrc.services.QueueService
import rx.Scheduler
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

@RadioActivity.Presenter
class RadioPresenterImpl
@Inject constructor(
    private val radioRepository: RadioRepository,
    private val queueService: QueueService,
    @Named("io") private val ioScheduler: Scheduler,
    @Named("main") private val mainScheduler: Scheduler
) :
    BasePresenter<RadioView>(),
    RadioPresenter {

  override fun load() {
    view?.showLoading()
    addSubcription(radioRepository.cacheIsEmpty().flatMap {
      if (it) {
        return@flatMap radioRepository.getAndSaveRemote()
      } else {
        return@flatMap radioRepository.getAllCursor()
      }
    }.subscribeOn(ioScheduler).observeOn(mainScheduler).subscribe({
      view?.hideLoading()
      view?.update(it)
    }, {
      view?.error(it)
      view?.hideLoading()
      Timber.v(it, "Failed")
    }))
  }

  override fun refresh() {
    view?.showLoading()
    addSubcription(radioRepository.getAndSaveRemote()
        .subscribeOn(ioScheduler)
        .observeOn(mainScheduler)
        .subscribe({
          view?.update(it)
          view?.hideLoading()
        }, {
          view?.error(it)
          view?.hideLoading()
        }))
  }

  override fun play(path: String) {
    queueService.queue(NOW, listOf(path))
        .subscribeOn(ioScheduler)
        .observeOn(mainScheduler)
        .subscribe({
          view?.radioPlaySuccessful()
        }, {
          view?.radioPlayFailed(it)
        })
  }
}
