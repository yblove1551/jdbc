package common;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

@WebListener
public class SCListener implements javax.servlet.ServletContextListener{
	@Override
	public void contextInitialized(ServletContextEvent sce) {		
		AppContext app = new AppContext(sce.getServletContext());
		try {
			app.beanScan();
			app.doAutowired();				
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}
	
}
