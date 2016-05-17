package com.kelsos.mbrc.commands;

import com.google.inject.Inject;
import com.kelsos.mbrc.constants.Protocol;
import com.kelsos.mbrc.data.SocketMessage;
import com.kelsos.mbrc.interfaces.ICommand;
import com.kelsos.mbrc.interfaces.IEvent;
import com.kelsos.mbrc.services.SocketService;
import timber.log.Timber;

public class ProtocolPingHandle implements ICommand {
  private final SocketService service;

  @Inject public ProtocolPingHandle(SocketService service) {
    this.service = service;
  }

  @Override public void execute(IEvent e) {
    Timber.v("Ping received");
    service.sendData(new SocketMessage(Protocol.PONG, ""));
  }
}
