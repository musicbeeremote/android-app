package com.kelsos.mbrc.configuration;

import javax.inject.Inject;
import com.kelsos.mbrc.commands.CancelNotificationCommand;
import com.kelsos.mbrc.commands.ConnectionStatusChangedCommand;
import com.kelsos.mbrc.commands.HandleHandshake;
import com.kelsos.mbrc.commands.InitiateConnectionCommand;
import com.kelsos.mbrc.commands.KeyVolumeDownCommand;
import com.kelsos.mbrc.commands.KeyVolumeUpCommand;
import com.kelsos.mbrc.commands.NotifyPluginOutOfDateCommand;
import com.kelsos.mbrc.commands.ProcessUserAction;
import com.kelsos.mbrc.commands.ProtocolPingHandle;
import com.kelsos.mbrc.commands.ProtocolPongHandle;
import com.kelsos.mbrc.commands.ProtocolRequest;
import com.kelsos.mbrc.commands.ReduceVolumeOnRingCommand;
import com.kelsos.mbrc.commands.RestartConnectionCommand;
import com.kelsos.mbrc.commands.SocketDataAvailableCommand;
import com.kelsos.mbrc.commands.StartDiscoveryCommand;
import com.kelsos.mbrc.commands.TerminateConnectionCommand;
import com.kelsos.mbrc.commands.VersionCheckCommand;
import com.kelsos.mbrc.commands.model.UpdateCover;
import com.kelsos.mbrc.commands.model.UpdateLastFm;
import com.kelsos.mbrc.commands.model.UpdateLfmRating;
import com.kelsos.mbrc.commands.model.UpdateLyrics;
import com.kelsos.mbrc.commands.model.UpdateMute;
import com.kelsos.mbrc.commands.model.UpdateNowPlayingList;
import com.kelsos.mbrc.commands.model.UpdateNowPlayingTrack;
import com.kelsos.mbrc.commands.model.UpdatePlayState;
import com.kelsos.mbrc.commands.model.UpdatePlayerStatus;
import com.kelsos.mbrc.commands.model.UpdatePlaylistList;
import com.kelsos.mbrc.commands.model.UpdatePluginVersionCommand;
import com.kelsos.mbrc.commands.model.UpdateRating;
import com.kelsos.mbrc.commands.model.UpdateRepeat;
import com.kelsos.mbrc.commands.model.UpdateShuffle;
import com.kelsos.mbrc.commands.model.UpdateVolume;
import com.kelsos.mbrc.commands.visual.NotifyNotAllowedCommand;
import com.kelsos.mbrc.commands.visual.UpdateNowPlayingTrackMoved;
import com.kelsos.mbrc.commands.visual.UpdateNowPlayingTrackRemoval;
import com.kelsos.mbrc.commands.visual.UpdatePlaybackPositionCommand;
import com.kelsos.mbrc.commands.visual.VisualUpdateHandshakeComplete;
import com.kelsos.mbrc.constants.Protocol;
import com.kelsos.mbrc.constants.ProtocolEventType;
import com.kelsos.mbrc.constants.SocketEventType;
import com.kelsos.mbrc.constants.UserInputEventType;
import com.kelsos.mbrc.controller.RemoteController;

public class CommandRegistration {
  @Inject public static void register(RemoteController controller) {
    controller.register(ProtocolEventType.ReduceVolume, ReduceVolumeOnRingCommand.class);
    controller.register(ProtocolEventType.HandshakeComplete, VisualUpdateHandshakeComplete.class);
    controller.register(ProtocolEventType.InformClientNotAllowed, NotifyNotAllowedCommand.class);
    controller.register(ProtocolEventType.InformClientPluginOutOfDate,
        NotifyPluginOutOfDateCommand.class);
    controller.register(ProtocolEventType.InitiateProtocolRequest, ProtocolRequest.class);
    controller.register(ProtocolEventType.PluginVersionCheck, VersionCheckCommand.class);
    controller.register(ProtocolEventType.UserAction, ProcessUserAction.class);
    controller.register(Protocol.NowPlayingTrack, UpdateNowPlayingTrack.class);
    controller.register(Protocol.NowPlayingCover, UpdateCover.class);
    controller.register(Protocol.NowPlayingRating, UpdateRating.class);
    controller.register(Protocol.PlayerStatus, UpdatePlayerStatus.class);
    controller.register(Protocol.PlayerState, UpdatePlayState.class);
    controller.register(Protocol.PlayerRepeat, UpdateRepeat.class);
    controller.register(Protocol.PlayerVolume, UpdateVolume.class);
    controller.register(Protocol.PlayerMute, UpdateMute.class);
    controller.register(Protocol.PlayerShuffle, UpdateShuffle.class);
    controller.register(Protocol.PlayerScrobble, UpdateLastFm.class);
    controller.register(Protocol.NowPlayingLyrics, UpdateLyrics.class);
    controller.register(Protocol.NowPlayingList, UpdateNowPlayingList.class);
    controller.register(Protocol.NowPlayingLfmRating, UpdateLfmRating.class);
    controller.register(Protocol.NowPlayingListRemove, UpdateNowPlayingTrackRemoval.class);
    controller.register(Protocol.NowPlayingListMove, UpdateNowPlayingTrackMoved.class);
    controller.register(Protocol.NowPlayingPosition, UpdatePlaybackPositionCommand.class);
    controller.register(Protocol.PluginVersion, UpdatePluginVersionCommand.class);
    controller.register(Protocol.PING, ProtocolPingHandle.class);
    controller.register(Protocol.PONG, ProtocolPongHandle.class);
    controller.register(Protocol.PlaylistList, UpdatePlaylistList.class);

    controller.register(UserInputEventType.SettingsChanged, RestartConnectionCommand.class);
    controller.register(UserInputEventType.CancelNotification, CancelNotificationCommand.class);
    controller.register(UserInputEventType.StartConnection, InitiateConnectionCommand.class);
    controller.register(UserInputEventType.TerminateConnection, TerminateConnectionCommand.class);
    controller.register(UserInputEventType.ResetConnection, RestartConnectionCommand.class);
    controller.register(UserInputEventType.StartDiscovery, StartDiscoveryCommand.class);
    controller.register(UserInputEventType.KeyVolumeUp, KeyVolumeUpCommand.class);
    controller.register(UserInputEventType.KeyVolumeDown, KeyVolumeDownCommand.class);
    controller.register(SocketEventType.SocketDataAvailable, SocketDataAvailableCommand.class);
    controller.register(SocketEventType.SocketStatusChanged, ConnectionStatusChangedCommand.class);
    controller.register(SocketEventType.SocketHandshakeUpdate, HandleHandshake.class);
  }

  public static void unregister(RemoteController controller) {
    controller.unregister(ProtocolEventType.ReduceVolume, ReduceVolumeOnRingCommand.class);
    controller.unregister(ProtocolEventType.HandshakeComplete, VisualUpdateHandshakeComplete.class);
    controller.unregister(ProtocolEventType.InformClientNotAllowed, NotifyNotAllowedCommand.class);
    controller.unregister(ProtocolEventType.InformClientPluginOutOfDate,
        NotifyPluginOutOfDateCommand.class);
    controller.unregister(ProtocolEventType.InitiateProtocolRequest, ProtocolRequest.class);
    controller.unregister(ProtocolEventType.PluginVersionCheck, VersionCheckCommand.class);
    controller.unregister(ProtocolEventType.UserAction, ProcessUserAction.class);
    controller.unregister(Protocol.NowPlayingTrack, UpdateNowPlayingTrack.class);
    controller.unregister(Protocol.NowPlayingCover, UpdateCover.class);
    controller.unregister(Protocol.NowPlayingRating, UpdateRating.class);
    controller.unregister(Protocol.PlayerStatus, UpdatePlayerStatus.class);
    controller.unregister(Protocol.PlayerState, UpdatePlayState.class);
    controller.unregister(Protocol.PlayerRepeat, UpdateRepeat.class);
    controller.unregister(Protocol.PlayerVolume, UpdateVolume.class);
    controller.unregister(Protocol.PlayerMute, UpdateMute.class);
    controller.unregister(Protocol.PlayerShuffle, UpdateShuffle.class);
    controller.unregister(Protocol.PlayerScrobble, UpdateLastFm.class);
    controller.unregister(Protocol.NowPlayingLyrics, UpdateLyrics.class);
    controller.unregister(Protocol.NowPlayingList, UpdateNowPlayingList.class);
    controller.unregister(Protocol.NowPlayingLfmRating, UpdateLfmRating.class);
    controller.unregister(Protocol.NowPlayingListRemove, UpdateNowPlayingTrackRemoval.class);
    controller.unregister(Protocol.NowPlayingListMove, UpdateNowPlayingTrackMoved.class);
    controller.unregister(Protocol.NowPlayingPosition, UpdatePlaybackPositionCommand.class);
    controller.unregister(Protocol.PluginVersion, UpdatePluginVersionCommand.class);
    controller.unregister(Protocol.PING, ProtocolPingHandle.class);
    controller.unregister(Protocol.PONG, ProtocolPongHandle.class);


    controller.unregister(UserInputEventType.SettingsChanged, RestartConnectionCommand.class);
    controller.unregister(UserInputEventType.CancelNotification, CancelNotificationCommand.class);
    controller.unregister(UserInputEventType.StartConnection, InitiateConnectionCommand.class);
    controller.unregister(UserInputEventType.TerminateConnection, TerminateConnectionCommand.class);
    controller.unregister(UserInputEventType.ResetConnection, RestartConnectionCommand.class);
    controller.unregister(UserInputEventType.StartDiscovery, StartDiscoveryCommand.class);
    controller.unregister(UserInputEventType.KeyVolumeUp, KeyVolumeUpCommand.class);
    controller.unregister(UserInputEventType.KeyVolumeDown, KeyVolumeDownCommand.class);
    controller.unregister(SocketEventType.SocketDataAvailable, SocketDataAvailableCommand.class);
    controller.unregister(SocketEventType.SocketStatusChanged,
        ConnectionStatusChangedCommand.class);
    controller.unregister(SocketEventType.SocketHandshakeUpdate, HandleHandshake.class);
  }
}
