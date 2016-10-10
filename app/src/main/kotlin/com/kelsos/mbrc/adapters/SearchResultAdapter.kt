package com.kelsos.mbrc.adapters

import android.app.Activity
import android.support.annotation.MenuRes
import android.support.annotation.Nullable
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.kelsos.mbrc.R
import com.kelsos.mbrc.annotations.Search.SECTION_ALBUM
import com.kelsos.mbrc.annotations.Search.SECTION_ARTIST
import com.kelsos.mbrc.annotations.Search.SECTION_GENRE
import com.kelsos.mbrc.annotations.Search.SECTION_TRACK
import com.kelsos.mbrc.annotations.Search.Section
import com.kelsos.mbrc.data.library.*
import com.raizlabs.android.dbflow.list.FlowQueryList
import com.raizlabs.android.dbflow.sql.language.SQLite
import javax.inject.Inject

class SearchResultAdapter
@Inject
constructor(context: Activity) : SectionedRecyclerViewAdapter<SearchResultAdapter.SearchViewHolder>() {

  private val inflater: LayoutInflater

  private var genreList: FlowQueryList<Genre>? = null
  private var artistList: FlowQueryList<Artist>? = null
  private var albumList: FlowQueryList<Album>? = null
  private var trackList: FlowQueryList<Track>? = null

  private var onSearchItemSelectedListener: OnSearchItemSelected? = null

  init {
    inflater = LayoutInflater.from(context)
  }

  override val sectionCount: Int
    get() = 4

  override fun getItemCount(@Section section: Int): Int {

    if (section == SECTION_GENRE) {
      return if (genreList != null) genreList!!.size else 0
    } else if (section == SECTION_ARTIST) {
      return if (artistList != null) artistList!!.size else 0
    } else if (section == SECTION_ALBUM) {
      return if (albumList != null) albumList!!.size else 0
    } else if (section == SECTION_TRACK) {
      return if (trackList != null) trackList!!.size else 0
    }

    return 0
  }

  fun setQuery(query: String) {
    val like = "%$query%"
    genreList = SQLite.select().from<Genre>(Genre::class.java).where(Genre_Table.genre.like(like)).flowQueryList()
    artistList = SQLite.select().from<Artist>(Artist::class.java).where(Artist_Table.artist.like(like)).flowQueryList()
    albumList = SQLite.select().from<Album>(Album::class.java).where(Album_Table.album.like(like)).flowQueryList()
    trackList = SQLite.select().from<Track>(Track::class.java).where(Track_Table.title.like(like)).flowQueryList()
    notifyDataSetChanged()
  }

  override fun onBindHeaderViewHolder(holder: SearchViewHolder, @Section section: Int) {
    when (section) {
      SECTION_ALBUM -> holder.lineOne.setText(R.string.label_albums)
      SECTION_ARTIST -> holder.lineOne.setText(R.string.label_artists)
      SECTION_GENRE -> holder.lineOne.setText(R.string.label_genres)
      SECTION_TRACK -> holder.lineOne.setText(R.string.label_tracks)
      else -> {
      }
    }
  }

  override fun onBindViewHolder(holder: SearchViewHolder,
                                @Section section: Int,
                                relativePosition: Int,
                                absolutePosition: Int) {
    when (section) {
      SECTION_ALBUM -> {
        val album = albumList!![relativePosition]
        holder.lineOne.text = album.album
        holder.lineTwo?.text = album.artist
        holder.uiItemContextIndicator?.setOnClickListener { onContextClick(holder, album) }
        holder.itemView.setOnClickListener {
          onSearchItemSelectedListener?.albumSelected(album)
        }
      }
      SECTION_ARTIST -> {
        val artist = artistList!![relativePosition]
        holder.lineOne.text = artist.artist
        holder.uiItemContextIndicator?.setOnClickListener { onContextClick(holder, artist) }
        holder.itemView.setOnClickListener {
          onSearchItemSelectedListener?.artistSelected(artist)
        }
      }
      SECTION_GENRE -> {
        val genre = genreList!![relativePosition]
        holder.lineOne.text = genre.genre
        holder.uiItemContextIndicator?.setOnClickListener { onContextClick(holder, genre) }
        holder.itemView.setOnClickListener {
          onSearchItemSelectedListener?.genreSelected(genre)
        }
      }
      SECTION_TRACK -> {
        val track = trackList!![relativePosition]
        holder.lineOne.text = track.title
        holder.lineTwo?.text = track.artist
        holder.uiItemContextIndicator?.setOnClickListener { onContextClick(holder, track) }
        holder.itemView.setOnClickListener {
          onSearchItemSelectedListener?.trackSelected(track)
        }
      }
      else -> {
      }
    }
  }

  private fun onContextClick(holder: SearchViewHolder, track: Track) {
    showPopup(R.menu.popup_track, holder.uiItemContextIndicator!!, { item ->
      onSearchItemSelectedListener?.trackSelected(item, track)
      true
    })
  }

  private fun onContextClick(holder: SearchViewHolder, genre: Genre) {
    showPopup(R.menu.popup_genre, holder.uiItemContextIndicator!!, { item ->
      onSearchItemSelectedListener?.genreSelected(item, genre)
      true
    })
  }

  private fun onContextClick(holder: SearchViewHolder, album: Album) {
    showPopup(R.menu.popup_album, holder.uiItemContextIndicator!!, { item ->
      onSearchItemSelectedListener?.albumSelected(item, album)
      true
    })
  }

  private fun onContextClick(holder: SearchViewHolder, artist: Artist) {
    showPopup(R.menu.popup_artist, holder.uiItemContextIndicator!!, { item ->
      onSearchItemSelectedListener?.artistSelected(item, artist)
      true
    })
  }

  override fun getItemViewType(@Section section: Int, relativePosition: Int, absolutePosition: Int): Int {
    if (section == SECTION_GENRE || section == SECTION_ARTIST) {
      return VIEW_TYPE_SINGLE
    } else if (section == SECTION_ALBUM || section == SECTION_TRACK) {
      return VIEW_TYPE_DUAL
    }

    return super.getItemViewType(section, relativePosition, absolutePosition)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
    val layout: Int
    when (viewType) {
      VIEW_TYPE_HEADER -> layout = R.layout.list_section_header
      VIEW_TYPE_DUAL -> layout = R.layout.ui_list_dual
      VIEW_TYPE_SINGLE -> layout = R.layout.listitem_single
      else -> layout = R.layout.listitem_single
    }
    val view = inflater.inflate(layout, parent, false)
    return SearchViewHolder(view)
  }

  private fun showPopup(@MenuRes menu: Int, view: View, listener: (MenuItem) -> Boolean) {
    val popupMenu = PopupMenu(view.context, view)
    popupMenu.inflate(menu)
    popupMenu.setOnMenuItemClickListener({ listener.invoke(it) })
    popupMenu.show()
  }

  fun setOnSearchItemSelectedListener(onSearchItemSelectedListener: OnSearchItemSelected) {
    this.onSearchItemSelectedListener = onSearchItemSelectedListener
  }

  interface OnSearchItemSelected {
    fun albumSelected(item: MenuItem, album: Album)

    fun albumSelected(album: Album)

    fun artistSelected(item: MenuItem, artist: Artist)

    fun artistSelected(artist: Artist)

    fun genreSelected(item: MenuItem, genre: Genre)

    fun genreSelected(genre: Genre)

    fun trackSelected(item: MenuItem, track: Track)

    fun trackSelected(track: Track)
  }

  class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    @BindView(R.id.line_one) lateinit var lineOne: TextView
    @BindView(R.id.line_two) @Nullable @JvmField var lineTwo: TextView? = null
    @BindView(R.id.ui_item_context_indicator) @Nullable @JvmField var uiItemContextIndicator: LinearLayout? = null

    init {
      ButterKnife.bind(this, itemView)
    }
  }

  companion object {

    private val VIEW_TYPE_DUAL = 1
    private val VIEW_TYPE_SINGLE = 2
  }
}

