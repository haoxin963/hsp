package net.hsp.common.rmi;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import com.caucho.hessian.client.HessianProxyFactory;

public class AuthHessianProxyFactory extends HessianProxyFactory{

	@Override
	protected URLConnection openConnection(URL url) throws IOException {
		URLConnection conn = super.openConnection(url);
        conn.setRequestProperty("auth", "123456");
        return conn; 
	}

}
