package common;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

@WebFilter("/*")
public class EncodingFilter implements Filter {
	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {	

		arg0.setCharacterEncoding("utf-8");
		arg1.setContentType("text/html; charset=utf-8");
		arg1.setCharacterEncoding("utf-8");		
		System.out.println("[" + ((HttpServletRequest)arg0).getHeader("referer") + "] -> " + "[doUrl:" +  ((HttpServletRequest)arg0).getRequestURI() + "]");		
		arg2.doFilter(arg0, arg1);
		
	}
}
