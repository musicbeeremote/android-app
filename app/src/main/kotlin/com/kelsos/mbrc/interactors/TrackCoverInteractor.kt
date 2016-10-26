package com.kelsos.mbrc.interactors

import android.graphics.Bitmap

import rx.Observable

interface TrackCoverInteractor {
  fun load(reload: Boolean = false): Observable<Bitmap?>
}