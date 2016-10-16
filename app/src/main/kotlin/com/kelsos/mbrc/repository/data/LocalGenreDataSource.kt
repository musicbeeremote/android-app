package com.kelsos.mbrc.repository.data

import com.kelsos.mbrc.data.library.Genre
import com.raizlabs.android.dbflow.kotlinextensions.*
import com.raizlabs.android.dbflow.list.FlowCursorList
import com.raizlabs.android.dbflow.structure.database.transaction.FastStoreModelTransaction
import rx.Emitter
import rx.Observable
import javax.inject.Inject

class LocalGenreDataSource
@Inject constructor() : LocalDataSource<Genre> {
  override fun deleteAll() {
    delete(Genre::class).execute()
  }

  override fun saveAll(list: List<Genre>) {
    val adapter = modelAdapter<Genre>()

    val transaction = FastStoreModelTransaction.insertBuilder(adapter)
        .addAll(list)
        .build()

    database<Genre>().executeTransaction(transaction)
  }

  override fun loadAllCursor(): Observable<FlowCursorList<Genre>> {
    return Observable.fromEmitter({
      val modelQueriable = select from Genre::class
      val cursor = FlowCursorList.Builder(Genre::class.java).modelQueriable(modelQueriable).build()
      it.onNext(cursor)
      it.onCompleted()
    }, Emitter.BackpressureMode.LATEST)

  }
}
