package mx.org.infonavit.bean;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.List;


import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.primefaces.PrimeFaces;

import mx.org.infonavit.entity.bitacora;
import mx.org.infonavit.service.ServiceVisor;
import mx.org.infonavit.tools.Propiedades;
import org.apache.log4j.Logger;


@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String usuario;
	private Propiedades pro=new Propiedades();
	private final String IDCAT = "IDCATAPP";
	private boolean banderaAdmin = false;
	private String contrasena;
	private String carpeta;
	private String grupoUsuario = "cn=GS_ENTIDADES_EXTERNAS,ou=TI,ou=areasapoyo,O=INFONAVIT";
	private String Home = "principal.xhtml?faces-redirect=true";
	boolean loggedIn = false;
	@EJB
	private ServiceVisor transactionalService;
	List<bitacora> bit;
	static final Logger log = Logger.getLogger(LoginBean.class);
	public void init() {
		bit = transactionalService.reporte();
	}

	public String login() {
		
		try {
			if (validadorUsuario(grupoUsuario)) {
				/* URL para la carga con el Websocket */
				return Home;
			} 

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Usuario no encontrado"));
			return null;

		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			return null;
		}

	}

	public boolean validadorUsuario(String grupo) {
		try {
			String url = pro.readPropertie("autenticaURL") + "/AutenticaQa-web/api/autenticaService/loginDetalleBal";

			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

			con.setRequestMethod("POST");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			String urlParameters = "usuario=" + usuario + "&password=" + contrasena + "&ID_CAT_APP=" + IDCAT + "&grupo="
					+ grupo;
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			JSONParser parser = new JSONParser();
			JSONObject objJSON = (JSONObject) parser.parse(response.toString());

			if (objJSON.get("conexion") != null) {
				if (objJSON.get("conexion").equals("exitosa")
						&& objJSON.get("account_status").equals("Usuario no encontrado")) {

				}
				if (objJSON.get("conexion").equals("exitosa")
						&& objJSON.get("account_status").toString().contains("Datos Incorrectos")) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Password incorrecto"));
				}
				if (objJSON.get("conexion").equals("exitosa")
						&& objJSON.get("account_status").toString().contains("estatus: inactive")) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Usuario inactivo"));

				}

			} else if (objJSON.get("codigo_login") != null) {
				if (objJSON.get("codigo_login").equals("Exitoso") && objJSON.get("account_status").equals("active")) {
					// Se asigna el nombre de usuario del SDS para mostrarlo en la aplicación
					loggedIn = true;
					//ruta = "prometeo-web/carga.xhtml";
					FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", usuario);
					PrimeFaces.current().ajax().addCallbackParam("loggedIn", loggedIn);
					//PrimeFaces.current().ajax().addCallbackParam("ruta", ruta);
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "Acceso correcto", "Bienvenido: " + usuario));
					log.info( "Bienvenido: " + usuario);
					FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
					return true;
				}
			}

			return false;

		} catch (Exception e) {
			System.out.println("Mensaje validador: " + e.getMessage());
			System.out.println("Causa: " + e.getCause());
			return false;
		}

	}


	public String cerrarSesion() {
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();

			HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
			session.invalidate();
			System.out.println("Cerrando Sesión");
			PrimeFaces.current().ajax().addCallbackParam("loggedOut", true);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Logged out", "Cerrando la sesión"));
			return "/index.xhtml?faces-redirect=true";
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("" + e.getMessage());
			System.out.println("" + e.getCause());
		}
		return "/index.xhtml?faces-redirect=true";
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public String getCarpeta() {
		return carpeta;
	}

	public void setCarpeta(String carpeta) {
		this.carpeta = carpeta;
	}

	public boolean isBanderaAdmin() {
		return banderaAdmin;
	}

	public void setBanderaAdmin(boolean banderaAdmin) {
		this.banderaAdmin = banderaAdmin;
	}

	public String getIDCAT() {
		return IDCAT;
	}


	

	
}
