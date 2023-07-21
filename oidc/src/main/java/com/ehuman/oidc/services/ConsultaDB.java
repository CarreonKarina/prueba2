package com.ehuman.oidc.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.ehuman.oidc.dto.EmpleadoDto;
import com.ehuman.oidc.dto.HuCatPropiedadesDto;


@Service
public class ConsultaDB {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	

	
	private static final Logger LOG = LoggerFactory.getLogger(ConsultaDB.class);
	
	
	//consulta por numero de empleado y clave de compania internos
	/*
	 * public EmpleadoDto getEmpleado (Long numEmpleado, Long numeroCompania) {
	 * String sqlNumEmplClaveComp =
	 * "SELECT he.NUM_CIA, he.NUM_EMP, he.APELL_PAT,he.APELL_MAT,he.NOMBRE, hc.NUMERO_COMPANIA, hc.CLAVE_COMPANIA \r\n"
	 * +
	 * "FROM HUMAN.HU_EMPLS he, HUMAN.HU_COMPANIA hc WHERE he.NUM_CIA = hc.NUMERO_COMPANIA\r\n"
	 * + "AND he.STATUS = 'A' AND NUMERO_COMPANIA < 5000 AND he.NUM_EMP ="
	 * +" '"+numEmpleado+"' "+ "AND hc.NUMERO_COMPANIA="+" '"+numeroCompania +"' ";
	 * //List<EmpleadoDto>empleadoDtoNuevo = new ArrayList<>(); EmpleadoDto emp =
	 * null; List<EmpleadoDto> empleadoDtoNuevo =
	 * jdbcTemplate.query(sqlNumEmplClaveComp,BeanPropertyRowMapper.newInstance(
	 * EmpleadoDto.class)); LOG.info("En ConsultaDB: getEmpleado  obtiene"+
	 * empleadoDtoNuevo.toString()); if(!empleadoDtoNuevo.isEmpty()) { emp =
	 * empleadoDtoNuevo.get(0);
	 * 
	 * }else { emp = new EmpleadoDto();
	 * 
	 * } return emp;//trae datos completos(nombre y apellidos) }
	 */
	
	//consulta en empleados externos
	/*
	 * public EmpleadoDto getEmpletadoExterno(Long numEmpleado, Long
	 * numeroCompania){
	 * 
	 * String numEmplComp = this.completaNumEmp(numEmpleado);
	 * 
	 * String sqlEmpleadosExternos
	 * ="SELECT substr(EXT.USER_SKEY,1,(length(EXT.USER_SKEY)-10)) AS NUM_CIA,\r\n"
	 * +
	 * "substr(EXT.USER_SKEY, length(substr(EXT.USER_SKEY,1,(length(EXT.USER_SKEY)-10)))+1 , (length(EXT.USER_SKEY))) AS NUM_EMP,\r\n"
	 * +
	 * "substr(EXT.USER_SKEY,1,(length(EXT.USER_SKEY)-10)) AS NUMERO_COMPANIA, \r\n"
	 * +
	 * "(SELECT CLAVE_COMPANIA FROM HUMAN.HU_COMPANIA WHERE NUMERO_COMPANIA = substr(EXT.USER_SKEY,1,(length(EXT.USER_SKEY)-10))) AS CLAVE_COMPANIA, \r\n"
	 * + "APELL_PAT, APELL_MAT, NOMBRE " + "FROM HUMAN.HU_USUARIO_EXTERNO EXT \r\n"
	 * + "WHERE  STATUS = 'A' \r\n" +
	 * "AND substr(EXT.USER_SKEY,1,(length(EXT.USER_SKEY)-10)) ="+
	 * " '"+numeroCompania+"'\r\n" +
	 * "AND substr(EXT.USER_SKEY, length(substr(EXT.USER_SKEY,1,(length(EXT.USER_SKEY)-10)))+1 , (length(EXT.USER_SKEY))) ="
	 * + " '" +numEmplComp+ "' "; EmpleadoDto empExterno = null; List<EmpleadoDto>
	 * empleadoExterno =
	 * jdbcTemplate.query(sqlEmpleadosExternos,BeanPropertyRowMapper.newInstance(
	 * EmpleadoDto.class)); LOG.info("En ConsultaDB: getEmpletadoExterno  obtiene"+
	 * empleadoExterno.toString()); if(!empleadoExterno.isEmpty()) { empExterno=
	 * empleadoExterno.get(0); }else { empExterno = new EmpleadoDto(); }
	 * 
	 * return empExterno; }
	 */
	
	//obtener url de tabla 
	public String getUrl() {
		//String urlRedirec = "";
		String parametro = "url_redirect_prod";
		String sqlconsultaUrl =   "SELECT VALOR FROM HUMAN.HU_CAT_PROPIEDADES WHERE PARAMETRO ="+ " '"+parametro+ "' ";
		List<HuCatPropiedadesDto>urlRedirec = jdbcTemplate.query(sqlconsultaUrl,BeanPropertyRowMapper.newInstance(HuCatPropiedadesDto.class));
		LOG.info(sqlconsultaUrl);
		LOG.info(urlRedirec.get(0).getValor());
		return urlRedirec.get(0).getValor();
	}
	
	
	
	
	
	
	//accion para numero de empleado con menos de 10 digitos solo en HU_USUARIO_EXTERNO
	public String completaNumEmp(Long numEmpleado) {
				String numEStr= String.valueOf(numEmpleado);
				int faltantes = 10-numEStr.length();
				String  numEmplComp = "" ;
				if(numEStr.length()<10) {
					for(int i = 0; i<faltantes; i++) {
						numEmplComp = numEmplComp +'0';
					}
					numEmplComp= numEmplComp+numEStr;
					
				}else {
					numEmplComp=numEStr;
				}
				LOG.info("En completaNumEmp, numEmplComp= " +numEmplComp);
				return numEmplComp;
	}
	
	//Obtener datos empleado interno  por email
	public EmpleadoDto getEmpleado (String correo){
		String sqlNumEmplClaveComp = "SELECT he.NUM_CIA, he.NUM_EMP,he.APELL_PAT,he.APELL_MAT,he.NOMBRE, hc.NUMERO_COMPANIA, hc.CLAVE_COMPANIA \r\n"
				+ "            FROM HUMAN.HU_EMPLS he, HUMAN.HU_COMPANIA hc \r\n"
				+ "            WHERE he.NUM_CIA = hc.NUMERO_COMPANIA AND he.STATUS = \r\n"
				+ "            'A' AND NUMERO_COMPANIA < 5000 AND LOWER (TRIM( he.EMAIL)) ="+" '"+ correo +"' ";
	
		EmpleadoDto emp =  null;
		List<EmpleadoDto> empleadoDtoNuevo = jdbcTemplate.query(sqlNumEmplClaveComp,BeanPropertyRowMapper.newInstance(EmpleadoDto.class));
		LOG.info("En ConsultaDB: getEmpleado correo  obtiene"+ empleadoDtoNuevo.toString());
		if(!empleadoDtoNuevo.isEmpty()) {
			emp =  empleadoDtoNuevo.get(0);
			
		}else {
			 emp = new EmpleadoDto();
			
	}
		return emp;//trae datos completos(nombre y apellidos)
	}
	
	
	//Obtener datos empleado interno  por email	
	public EmpleadoDto getEmpletadoExterno(String correo){
		
		
		
				String sqlEmpleadosExternos ="SELECT substr(EXT.USER_SKEY,1,(length(EXT.USER_SKEY)-10)) AS NUM_CIA,\r\n"
						+ "				substr(EXT.USER_SKEY, length(substr(EXT.USER_SKEY,1,(length(EXT.USER_SKEY)-10)))+1 , (length(EXT.USER_SKEY))) AS NUM_EMP,\r\n"
						+ "				substr(EXT.USER_SKEY,1,(length(EXT.USER_SKEY)-10)) AS NUMERO_COMPANIA, \r\n"
						+ "				(SELECT CLAVE_COMPANIA FROM HUMAN.HU_COMPANIA WHERE NUMERO_COMPANIA = substr(EXT.USER_SKEY,1,(length(EXT.USER_SKEY)-10))) AS CLAVE_COMPANIA \r\n"
						+ "              APELL_PAT, APELL_MAT, NOMBRE \r\n"	
						+ "				FROM HUMAN.HU_USUARIO_EXTERNO EXT \r\n"
						+ "				WHERE  STATUS = 'A' \r\n"
						+ "				AND substr(EXT.USER_SKEY,1,(length(EXT.USER_SKEY)-10)) < 5000 AND LOWER (TRIM(EMAIL)) ="+" '"+correo+"' "; 
				EmpleadoDto empExterno = null;
				List<EmpleadoDto> empleadoExterno =  jdbcTemplate.query(sqlEmpleadosExternos,BeanPropertyRowMapper.newInstance(EmpleadoDto.class));
				LOG.info("En ConsultaDB: getEmpletadoExterno  obtiene"+ empleadoExterno.toString());
				if(!empleadoExterno.isEmpty()) {
					empExterno= empleadoExterno.get(0);
				}else {
					empExterno =  new EmpleadoDto();
				}
				
				return empExterno;
	}
	
	

}
