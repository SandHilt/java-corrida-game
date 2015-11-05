import java.awt.*;
import java.rmi.*;

interface IPlayer extends Remote{
	public void moveRight() throws RemoteException;
	public void moveLeft() throws RemoteException;
	public Point getPoint()	throws RemoteException;
}
