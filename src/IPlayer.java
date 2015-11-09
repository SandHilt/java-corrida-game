import java.awt.*;
import java.rmi.*;

interface IPlayer extends Remote{
	public boolean haveLife() throws RemoteException;
	public void moveRight() throws RemoteException;
	public void moveLeft() throws RemoteException;
	public Point getPoint()	throws RemoteException;
}
