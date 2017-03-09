package net.hsp.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.Name;

import net.hsp.web.util.SqlListener;

import org.logicalcobwebs.proxool.ConnectionListenerIF;
import org.logicalcobwebs.proxool.ProxoolDataSource;
import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.ProxoolFacade;

public class ProxoolExtDataSource extends ProxoolDataSource { 
	public static Map<String,Integer> m = new Hashtable<String, Integer>(); 
	@Override
	public Connection getConnection() throws SQLException {
		if (m.get(this.getAlias())==null) {
			ConnectionListenerIF myConnectionListener = new SqlListener();
			try {
				String alias = this.getAlias();
				ProxoolFacade.addConnectionListener(alias, myConnectionListener);
				m.put(alias, 1);
			} catch (ProxoolException e) { 
				
			}
		}
		
		return super.getConnection();
	}

}
