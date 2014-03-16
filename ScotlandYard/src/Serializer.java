import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class Serializer {

	private GameState state;
	private File file;
	
	public Serializer(GameState Istate, File Ifile) throws IOException
	{ 
		state = Istate;
		file = Ifile;
		
	}
	
	public void serializeDataOut()throws IOException{
	    FileOutputStream fos = new FileOutputStream(file);
	    ObjectOutputStream oos = new ObjectOutputStream(fos);
	    oos.writeObject(state);
	    oos.close();
	}

	
	public GameState serializeDataIn()throws IOException{
		   FileInputStream fin = new FileInputStream(file);
		   ObjectInputStream ois = new ObjectInputStream(fin);
		   GameState state = null;
		try {
			state = (GameState) ois.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   ois.close();
		   return state;
		}
	
	
}
