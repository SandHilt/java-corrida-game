
import java.rmi.*;
import java.util.ArrayList;

interface IJogo extends Remote {

	public Cenario getCenario() throws RemoteException;

	public ArrayList<Enemy> getEnemies() throws RemoteException;
}
