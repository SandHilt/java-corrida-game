
import java.awt.Rectangle;
import java.rmi.Remote;
import java.rmi.RemoteException;

interface IPlayer extends Remote {

	public boolean haveLife() throws RemoteException;

	public void moveRight() throws RemoteException;

	public void moveLeft() throws RemoteException;

	public Rectangle getBoundPlayer() throws RemoteException;

}
