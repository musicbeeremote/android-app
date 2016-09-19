package com.kelsos.mbrc.constants

object Protocol {

  @SuppressWarnings("unused")
  val Error = "error"
  val Player = "player"
  val ProtocolTag = "protocol"
  val ProtocolVersion = "2.2"
  val PluginVersion = "pluginversion"
  val ClientNotAllowed = "notallowed"

  val PlayerStatus = "playerstatus"
  val PlayerRepeat = "playerrepeat"
  val PlayerScrobble = "scrobbler"
  val PlayerShuffle = "playershuffle"
  val PlayerMute = "playermute"
  val PlayerPlayPause = "playerplaypause"
  val PlayerPrevious = "playerprevious"
  val PlayerNext = "playernext"
  val PlayerStop = "playerstop"
  val PlayerState = "playerstate"
  val PlayerVolume = "playervolume"
  val PlayerAutoDj = "playerautodj"

  val NowPlayingTrack = "nowplayingtrack"
  val NowPlayingCover = "nowplayingcover"
  val NowPlayingPosition = "nowplayingposition"
  val NowPlayingLyrics = "nowplayinglyrics"
  val NowPlayingRating = "nowplayingrating"
  val NowPlayingLfmRating = "nowplayinglfmrating"
  val NowPlayingList = "nowplayinglist"
  @SuppressWarnings("unused")
  val NowPlayingListChanged = "nowplayinglistchanged"
  val NowPlayingListPlay = "nowplayinglistplay"
  val NowPlayingListRemove = "nowplayinglistremove"
  val NowPlayingListMove = "nowplayinglistmove"
  val NowPlayingListSearch = "nowplayinglistsearch"

  val LibraryQueueGenre = "libraryqueuegenre"
  val LibraryQueueArtist = "libraryqueueartist"
  val LibraryQueueAlbum = "libraryqueuealbum"
  val LibraryQueueTrack = "libraryqueuetrack"

  val ALL = "All"
  val DISCOVERY = "discovery"

  val PING = "ping"
  val PONG = "pong"
  val INIT = "init"

  val PlayerPlay = "playerplay"
  val PlayerPause = "playerpause"

  val PlaylistList = "playlistlist"
  val PlaylistPlay = "playlistplay"
  val NoBroadcast = "nobroadcast"

  val LibraryBrowseGenres = "browsegenres"
  val LibraryBrowseArtists = "browseartists"
  val LibraryBrowseAlbums = "browsealbums"
  val LibraryBrowseTracks = "browsetracks"
  val ONE = "one"

  var ProtocolVersionNumber = 2.2f
}
