package LAB_B.Database;
//Skeleton
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

public class Server extends Thread{
    @Override
    public void run() {
        super.run();
        try {
            // Crea l'oggetto DatabaseImpl (Skeleton)
            DatabaseImpl db = new DatabaseImpl();
            // Registra l'oggetto nell'RMI
            Registry registry = LocateRegistry.createRegistry(5432);
            registry.rebind("DatabaseImpl",db);

            System.out.println("db è pronto");

        } catch (RemoteException e) {
            System.err.println(e.getMessage());
        }


    }


}
