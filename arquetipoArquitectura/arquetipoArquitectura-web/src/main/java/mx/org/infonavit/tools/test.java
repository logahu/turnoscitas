package mx.org.infonavit.tools;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Propiedades po = new Propiedades();
		//po.writePropertie("usuario", "password");
		System.out.println(po.readPropertie("autenticaURL"));
	}

}
