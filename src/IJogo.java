
import java.awt.*;
import java.rmi.*;
import java.util.ArrayList;

interface IJogo extends Remote {

	public Cenario getCenario() throws RemoteException;

	public ArrayList<Enemy> getEnemies() throws RemoteException;

	public int getIDPlayer() throws RemoteException;

	public boolean isReady() throws RemoteException;

	public Road getRoad() throws RemoteException;

	public Color corTempo() throws RemoteException;

	public ArrayList<Crossover> getCrossovers() throws RemoteException;

}
