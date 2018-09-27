package backend;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Server side implementation of the remote interface. Must extend UnicastRemoteObject, to allow the
 * JVM to create a remote proxy/stub.
 *
 */
public class RemoteGame extends UnicastRemoteObject implements IRemoteGame {

  protected RemoteGame() throws RemoteException {}

  @Override
  public String start() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String execute(int command, String Json) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String vote(boolean accept) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String pass() {
    // TODO Auto-generated method stub
    return null;
  }

}
