package com.kelsos.mbrc.features.library.presentation

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kelsos.mbrc.features.library.data.Album
import com.kelsos.mbrc.features.library.repositories.AlbumRepository
import com.kelsos.mbrc.ui.BaseViewModel
import com.kelsos.mbrc.ui.UiMessageBase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge

class AlbumViewModel(
  private val repository: AlbumRepository,
  searchModel: LibrarySearchModel
) : BaseViewModel<UiMessageBase>() {
  @OptIn(FlowPreview::class)
  val albums: Flow<PagingData<Album>> = searchModel.search.flatMapMerge { keyword ->
    if (keyword.isEmpty()) {
      repository.getAll()
    } else {
      repository.search(keyword)
    }
  }.cachedIn(viewModelScope)
}
