package com.ehuman.oidc.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ehuman.oidc.dto.EmpleadoDto;
import com.ehuman.oidc.dto.EmpleadoTokenDto;
import com.ehuman.oidc.services.ConsultaDB;
import com.ehuman.oidc.services.EmpleadoTokenService;




@CrossOrigin(origins = "*")
@RestController
public class EmpleadoController {

	@Autowired
	private ConsultaDB consulta;

	@Autowired
	private EmpleadoTokenService emplTokSer;

	private static final Logger LOG = LoggerFactory.getLogger(EmpleadoController.class);

	@GetMapping("/valida")
	//public String empleadoRegistrado(@RequestParam Long numEmp, @RequestParam Long numComp) {
	public String empleadoRegistrado(@RequestParam String email) {
		LOG.info("En EmpleadoController : Ingresa a empleadoRegistrado");
		//EmpleadoDto empEnc = recuperarRegistro(numEmp,numComp);//localiza datos de empleado en empleados intenos y esternos
		EmpleadoDto empEnc = recuperarRegistro(email);
		EmpleadoTokenDto empTkEnc =  new EmpleadoTokenDto();
		String url = consulta.getUrl();
		if(empEnc.getApell_mat()!= null && empEnc.getApell_pat()!=null && 
				empEnc.getNombre()!= null &&empEnc.getNum_cia() != null && 
				empEnc.getNum_emp()!= null) {

			String username = empEnc.getApell_pat()+" "+empEnc.getApell_mat()+" "+empEnc.getNombre();


			//buscar en tabla de tokens si existe registro
			empTkEnc= emplTokSer.getEmpleadoToken(empEnc.getNum_cia(), empEnc.getNum_emp());
			LOG.info("Empleado encontrado: " +empTkEnc);



			if(empTkEnc.getNumCia()!= null && empTkEnc.getNumEmp()!= null && empTkEnc.getClass()!= null &&empTkEnc.getToken()!= null) {

				LOG.info("aQUI " +empTkEnc.toString());
				 emplTokSer.updateEmpleadoToken(empTkEnc, username);
				//return empTkEnc.toString();



			}else {
				LOG.info("aQUI else " +empEnc.toString());

				empTkEnc = emplTokSer.addRegistroEmpleado(empEnc);
				//return empTkEnc.toString();

			}
			return url+"sso_token.xhtml?tokenUrl="+empTkEnc.getToken();
		
		}
		else {
			return url+"sso_token.xhtml?tokenUrl=sin datos";
		}

	}







		//obtiene registros de empleados internos o externos
		//public EmpleadoDto recuperarRegistro(Long numEmpleado, Long numeroCompania ) {
	   public EmpleadoDto recuperarRegistro(String email ) {
			LOG.info("Ingresa a recuperarRegistro");
			//String responseUrlRedirectWorkSocial = "";
			EmpleadoDto empleadoEncontrado =  null;	
			
			//EmpleadoDto empleadoDto= consulta.getEmpleado(numEmpleado, numeroCompania);
			EmpleadoDto empleadoDto= consulta.getEmpleado(email);
			if(empleadoDto.getApell_mat()!= null && empleadoDto.getApell_pat()!= null && empleadoDto.getNombre()!= null &&
					empleadoDto.getNum_cia()!= null && empleadoDto.getNum_emp()!= null && empleadoDto.getNumeroCompania()!= null) {

				//responseUrlRedirectWorkSocial = "Empleado  interno encontrado " + empleadoDto.get(0).getNum_cia()+" " + empleadoDto.get(0).getNum_emp()+" "+ empleadoDto.get(0).getApell_pat();
				empleadoEncontrado = empleadoDto;
			}else {

				//if(empleadoDto.isEmpty()) {

				//EmpleadoDto empleadoExterno =consulta.getEmpletadoExterno(numEmpleado, numeroCompania);
				EmpleadoDto empleadoExterno =consulta.getEmpletadoExterno(email);
				if(empleadoExterno.getApell_mat()!= null && empleadoExterno.getApell_pat()!= null && empleadoExterno.getNombre()!= null &&
						empleadoExterno.getNum_cia()!= null && empleadoExterno.getNum_emp()!= null && empleadoExterno.getNumeroCompania()!= null) {
					//responseUrlRedirectWorkSocial = "Sin correo electronico";
					empleadoEncontrado = empleadoExterno;
				}else if(empleadoDto.getNum_cia()== null || empleadoDto.getNum_emp()== null || 
						empleadoExterno.getNum_cia()!= null || empleadoExterno.getNum_emp()!= null) {
					empleadoEncontrado =  new EmpleadoDto();
				}
				

			}
			LOG.info("empleadoEncontrado: "+empleadoEncontrado);
			return empleadoEncontrado;
			

		}




	}
