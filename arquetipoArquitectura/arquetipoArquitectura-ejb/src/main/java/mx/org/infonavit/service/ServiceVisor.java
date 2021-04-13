package mx.org.infonavit.service;


import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import mx.org.infonavit.entity.bitacora;

@Remote
public interface ServiceVisor {
	void save(bitacora bit);
	List<bitacora> reporte();
	int cantidadAccion(String busqueda);
	int cantidadAccionFechas(String busqueda,Date fechaInicial,Date fechaFin);
	List<Integer> cantidadAccionList();
}
