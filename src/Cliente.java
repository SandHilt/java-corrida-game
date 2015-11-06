import java.rmi.*;

class Cliente {
	private IPlayer player;

	public Cliente(){
		try {
			player = (IPlayer) Naming.lookup("Player1");
		} catch(RemoteException e) {
			System.out.println("Nao consigo achar o jogador dentro do registro");
		} catch (Exception e) {
			System.out.println("Nao consigo achar o jogador dentro do registro");
		}
	}
}
