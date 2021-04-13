package mx.org.infonavit.tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Propiedades {
	Properties prop;
	FileInputStream file ;
	String ruta ="";
	
	public Propiedades()  {
		prop= new Properties();
		ruta = getClass().getResource("config.properties").getPath();
	}
	
	public void writePropertie(String campo,String valor) {
		try {
			file= new FileInputStream(ruta);
			prop.load(file);
			prop.setProperty(campo, valor);
			prop.store(new FileWriter(ruta),"");
			file.close();
		}catch(FileNotFoundException fne) {
			
		}catch(IOException ioe) {
			
		}
	}
	public String readPropertie(String campo) {
		String res="";
		try {
			file= new FileInputStream(ruta);
			prop.load(file);
			res =prop.getProperty(campo);
			file.close();
		}catch(FileNotFoundException fne) {
			
		}catch(IOException ioe) {
			
		}
		return res;
	}
}
