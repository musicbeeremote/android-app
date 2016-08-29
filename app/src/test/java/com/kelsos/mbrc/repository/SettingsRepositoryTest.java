package com.kelsos.mbrc.repository;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;

import com.kelsos.mbrc.BuildConfig;
import com.kelsos.mbrc.SettingsRepository;
import com.kelsos.mbrc.SettingsRepositoryImpl;
import com.kelsos.mbrc.data.ConnectionSettings;
import com.kelsos.mbrc.rules.DBFlowTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import toothpick.Scope;
import toothpick.Toothpick;
import toothpick.config.Module;
import toothpick.testing.ToothPickRule;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class SettingsRepositoryTest {
  public ToothPickRule toothPickRule = new ToothPickRule(this);
  @Rule
  public TestRule chain = RuleChain.outerRule(toothPickRule).around(DBFlowTestRule.create());

  @Before
  public void setUp() throws Exception {

  }

  @Test
  public void addNewSettings() {
    SettingsRepository repository = getRepository();
    ConnectionSettings settings = createSettings("192.167.90.10");
    repository.save(settings);

    assertThat(repository.getDefault()).isEqualTo(settings);
    assertThat(settings.getId()).isEqualTo(1);
  }

  @Test
  public void addMultipleNewSettings() {
    SettingsRepository repository = getRepository();
    ConnectionSettings settings = createSettings("192.167.90.10");
    ConnectionSettings settings1 = createSettings("192.167.90.11");
    ConnectionSettings settings2 = createSettings("192.167.90.12");
    ConnectionSettings settings3 = createSettings("192.167.90.12");
    repository.save(settings);
    repository.save(settings1);
    repository.save(settings2);
    repository.save(settings3);

    assertThat(repository.getDefault()).isEqualTo(settings);
    assertThat(settings.getId()).isEqualTo(1);
    assertThat(repository.count()).isEqualTo(3);
  }

  private SettingsRepository getRepository() {
    Scope scope = Toothpick.openScope(RuntimeEnvironment.application);
    scope.installModules(new TestModule());
    return scope.getInstance(SettingsRepository.class);
  }

  @Test
  public void addMultipleNewSettingsRemoveOne() {
    SettingsRepository repository = getRepository();
    ConnectionSettings settings = createSettings("192.167.90.10");
    ConnectionSettings settings1 = createSettings("192.167.90.11");
    ConnectionSettings settings2 = createSettings("192.167.90.12");
    ConnectionSettings settings3 = createSettings("192.167.90.13");

    repository.save(settings);
    repository.save(settings1);
    repository.save(settings2);
    repository.save(settings3);

    assertThat(repository.getDefault()).isEqualTo(settings);
    assertThat(settings.getId()).isEqualTo(1);
    assertThat(repository.count()).isEqualTo(4);

    repository.delete(settings2);

    List<ConnectionSettings> settingsList = new ArrayList<>();
    settingsList.add(settings);
    settingsList.add(settings1);
    settingsList.add(settings3);

    assertThat(repository.count()).isEqualTo(3);
    assertThat(repository.getAll()).containsAllIn(settingsList);
  }

  @Test
  public void changeDefault() {
    SettingsRepository repository = getRepository();

    ConnectionSettings settings = createSettings("192.167.90.10");
    ConnectionSettings settings1 = createSettings("192.167.90.11");

    repository.save(settings);
    repository.save(settings1);

    assertThat(repository.getDefault()).isEqualTo(settings);

    repository.setDefault(settings1);

    assertThat(repository.getDefault()).isEqualTo(settings1);
  }

  @NonNull
  private ConnectionSettings createSettings(String address) {
    ConnectionSettings settings = new ConnectionSettings();
    settings.setName("Desktop PC");
    settings.setAddress(address);
    settings.setPort(3000);
    return settings;
  }

  private class TestModule extends Module {
    @SuppressLint("CommitPrefEdits")
    TestModule() {
      bind(SharedPreferences.class).toProviderInstance(() -> {
        long[] defaultId = {-1};
        SharedPreferences preferences = Mockito.mock(SharedPreferences.class);
        SharedPreferences.Editor editor = Mockito.mock(SharedPreferences.Editor.class);
        when(preferences.edit()).thenReturn(editor);
        when(preferences.getLong(anyString(), anyLong())).thenAnswer(invocation -> defaultId[0]);
        when(editor.putLong(anyString(), anyLong())).then(invocation -> {
          Object o = invocation.getArguments()[1];
          defaultId[0] = Long.parseLong(o.toString());
          return editor;
        });
        return preferences;
      });
      bind(SettingsRepository.class).to(SettingsRepositoryImpl.class);
      bind(Resources.class).toProviderInstance(() -> {
        Resources resources = Mockito.mock(Resources.class);
        when(resources.getString(anyInt())).thenReturn("preferences_key");
        return resources;
      });
    }
  }

  @After
  public void tearDown() throws Exception {
    Toothpick.reset();
  }
}
