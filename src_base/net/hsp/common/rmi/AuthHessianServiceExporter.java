package net.hsp.common.rmi;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.remoting.caucho.HessianServiceExporter;

public class AuthHessianServiceExporter extends HessianServiceExporter {
	public static final String AUTH = "123456";

	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String auth = request.getHeader("auth"); 
		if (auth == null || !auth.equalsIgnoreCase(AUTH)) {
			System.out.println("无权限的Hessian调用");
			return;
		}
		super.handleRequest(request, response);
	}

}
