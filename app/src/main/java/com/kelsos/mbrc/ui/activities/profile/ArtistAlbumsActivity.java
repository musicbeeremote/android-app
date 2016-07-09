package com.kelsos.mbrc.ui.activities.profile;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.inject.Inject;
import com.kelsos.mbrc.R;
import com.kelsos.mbrc.adapters.AlbumEntryAdapter;
import com.kelsos.mbrc.data.library.Album;
import com.kelsos.mbrc.helper.PopupActionHandler;
import com.kelsos.mbrc.ui.widgets.EmptyRecyclerView;
import roboguice.RoboGuice;

public class ArtistAlbumsActivity extends AppCompatActivity
    implements AlbumEntryAdapter.MenuItemSelectedListener {

  public static final String ARTIST_NAME = "artist_name";

  @BindView(R.id.album_recycler)
  EmptyRecyclerView recyclerView;

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.empty_view)
  LinearLayout emptyView;

  @Inject
  private AlbumEntryAdapter adapter;

  @Inject
  private PopupActionHandler actionHandler;

  private String artist;

  public ArtistAlbumsActivity() {
    // Required empty public constructor
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_artist_albums);
    RoboGuice.getInjector(this).injectMembers(this);
    ButterKnife.bind(this);

    final Bundle extras = getIntent().getExtras();
    if (extras != null) {
      artist = extras.getString(ARTIST_NAME);
    }

    setSupportActionBar(toolbar);
    final ActionBar actionBar = getSupportActionBar();

    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setDisplayShowHomeEnabled(true);
      actionBar.setTitle(artist);
    }

    adapter.setMenuItemSelectedListener(this);
    adapter.init(artist);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(adapter);
    recyclerView.setEmptyView(emptyView);
  }

  @Override public void onMenuItemSelected(MenuItem menuItem, Album album) {
    actionHandler.albumSelected(menuItem, album);
  }

  @Override public void onItemClicked(Album album) {
    actionHandler.albumSelected(album);
  }

}