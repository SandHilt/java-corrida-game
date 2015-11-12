
import java.awt.Graphics;
import java.awt.Point;
import java.rmi.Remote;
import java.rmi.RemoteException;

interface IPlayer extends Remote {

	public boolean haveLife() throws RemoteException;

	public byte getLife() throws RemoteException;

	public void moveRight(Road road) throws RemoteException;

	public void moveLeft(Road road) throws RemoteException;

	public void changeDirection(Player.Direction direction) throws RemoteException;

	public void setVel(int vel) throws RemoteException;

	public int getVel() throws RemoteException;

	public void setConnected(boolean connected) throws RemoteException;

	public boolean isConnected() throws RemoteException;

	public void render(Graphics g) throws RemoteException;

	public void winner(Graphics g, Point p, String s) throws RemoteException;

	public void gameOver(Graphics g, Point p) throws RemoteException;

}
