// Copyright (c) 2018 Silicon Labs. All rights reserved.

package com.silabs.pti.adapter;

import java.io.IOException;

/**
 * Abstraction of a backchannel connection to an adapter.
 *
 * This object manages multiple connections to the different ports
 * that the adapter supports.
 *
 * @author Timotej
 * Created on Mar 20, 2018
 */
public interface IBackchannel {
  /**
   * Returns current portmapper.
   *
   *
   * @param
   * @returns IBackchannelPortMapper
   */
  public IBackchannelPortMapper portMapper();

  /**
   * Returns current connection enabler.
   *
   *
   * @param
   * @returns IConnectionEnabler
   */
  public IConnectionEnabler connectionEnabler();

  /**
   * Close the backchannel, closing all underlying connections.
   */
  public void close();

  /**
   * Returns a connection.
   * @param port
   * @return
   */
  public IConnection getConnection(final AdapterPort port);

  /**
   * Returns the Connection object for the admin port. Useful for interacting
   * directly with the Connection.
   */
  default public IConnection admin() {
    return getConnection(AdapterPort.ADMIN);
  }

  /**
   * Returns debug connection if one exists, null otherwise.
   *
   * @returns IDebugConnection
   */
  public IDebugConnection debugConnection();

  /**
   * Connects socket to the specified port: serial 0, serial 1, admin and debug
   * port. Does not try to reset the board on failure.
   *
   * @return true if and only if connection was successful.
   */
  default public void connect(final AdapterPort port) throws IOException {
    IConnection connection = getConnection(port);
    if (connection == null) {
      throw new IOException("No connection declared for port: " + port.name());
    }
    connection.connect();
  }

  /**
   * Returns true if the socket for the specified port is connected.
   */
  default public boolean isConnected(final AdapterPort port) {
    if (getConnection(port) == null)
      return false;
    else
      return getConnection(port).isConnected();
  }

  /**
   * Returns the serial 0 connection. Shortcut to getConnectio(SERIAL0)
   * @return
   */
  default public IConnection serial0() {
    return getConnection(AdapterPort.SERIAL0);
  }

  /**
   * Returns the serial 1 connection. Shortcut to getConnectio(SERIAL1)
   * @return
   */
  default public IConnection serial1() {
    return getConnection(AdapterPort.SERIAL1);
  }

  /**
   * Starts the debug session.
   *
   * @param debugListener
   * @return
   */
  public boolean enableDebugChannelCapture(IDebugMessageListener debugMessageListener,
                                           IConnectionProblemListener problemListener);

  /**
   * Ends the debug session.
   *
   * @return
   */
  public boolean disableDebugChannelCapture();


}