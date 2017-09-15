package com.flashvisions.red5.server.bodyshow;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.flashvisions.red5.server.tv.bodyshow.model.DataResponse;
import com.flashvisions.red5.server.tv.bodyshow.model.ErrorResponse;
import com.flashvisions.red5.server.tv.bodyshow.model.LiveStream;
import com.google.gson.Gson;

public class InformationSevlet extends HttpServlet {
	
	private Logger logger = LoggerFactory.getLogger(Application.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 7731281558788266706L;
	
	
	
	@Autowired
	private Application application;
	
	
	
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
	}	

	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String securityToken = null;
		List<LiveStream> streams = null;
		String requestToken = null;
		
		PrintWriter writer = resp.getWriter();
		
		try
		{
			securityToken = application.getAccessToken();
			streams = application.getLiveStreams();
			
			requestToken = req.getParameter("accessToken");
			logger.info("Passed token " +  requestToken);
			
			if(requestToken == null)
			throw new Exception("One or more parameter(s) missing from request");
			
			
			if(!securityToken.equalsIgnoreCase(requestToken))
			{
				writer.write(new Gson().toJson(new ErrorResponse(401, "Security token does not match!!")));
			}
			else
			{
				streams = application.getLiveStreams();
				writer.write(new Gson().toJson(new DataResponse(200, streams)));
			}			
		}
		catch(Exception e)
		{
			writer.write(new Gson().toJson(new ErrorResponse(400, e.getMessage())));
		}		
	}

}
