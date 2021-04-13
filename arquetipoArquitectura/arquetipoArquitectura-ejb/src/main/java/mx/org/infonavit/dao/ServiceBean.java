package mx.org.infonavit.dao;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import mx.org.infonavit.entity.bitacora;
import mx.org.infonavit.service.ServiceVisor;



@Stateless
public class ServiceBean implements ServiceVisor {
	
	@PersistenceContext(unitName = "arquetipoArquitectura")
	private EntityManager em;

	@Override
	public void save(bitacora bit) {
		try {
			em.persist(bit);
		}catch(Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
		}
		

	}

	@Override
	public List<bitacora> reporte() {
		// TODO Auto-generated method stub
		List<bitacora> listaBitacora =null;
		if(em.isOpen()) {
			System.out.println("conexion hecha");
		}
		try {
			TypedQuery<bitacora> bi = em.createNamedQuery("Allbitacora", bitacora.class);
			listaBitacora = bi.getResultList();
			 //listaBitacora = em.createNativeQuery("select * from bitacora ",bitacora.class).getResultList();
		}catch(Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
		}
		
		return listaBitacora;
	}

	@Override
	public int cantidadAccion(String busqueda) {
		return 0;
	}

	@Override
	public int cantidadAccionFechas(String busqueda, Date fechaInicial, Date fechaFin) {
		return 0;
	}

	@Override
	public List<Integer> cantidadAccionList() {
		return null;
	}



}
