package com.soapConsumer.service;

import org.apache.ws.security.WSConstants;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;

import com.soapConsumer.client.GetCourseInfoResponse;
import com.soapConsumer.courseBeans.CourseInfo;
import com.soapConsumer.courseBeans.GetCourseInfoRequest;

@Service
public class ClientService {

	@Autowired
	private Jaxb2Marshaller marshaller;
	
	private WebServiceTemplate template;
	
	public GetCourseInfoResponse handleRequest(GetCourseInfoRequest request) {
		GetCourseInfoResponse response = new GetCourseInfoResponse();
		template = new WebServiceTemplate(marshaller);
		template.setInterceptors(new ClientInterceptor[]{securityInterceptor()});
		request.setId(1);
		com.soapConsumer.courseBeans.GetCourseInfoResponse serviceResponse = 
				(com.soapConsumer.courseBeans.GetCourseInfoResponse) template.marshalSendAndReceive(
				"http://localhost:8080/ws", request);
		/*
		com.soapConsumer.client.CourseInfo cInfo = new com.soapConsumer.client.CourseInfo();
		cInfo.setDescription(courseInfo.getDescription());
		cInfo.setId(courseInfo.getId());
		cInfo.setName(courseInfo.getName());
		*/
		
//		response.setCourseInfo(cInfo);
//		response.setCourseInfo(serviceResponse.getCourseInfo());
		ModelMapper mapper = new ModelMapper();
		response = mapper.map(serviceResponse, GetCourseInfoResponse.class);
		
		return response;
	}
	
	@Bean(name="securityInterceptor") 
	public Wss4jSecurityInterceptor securityInterceptor() {
		Wss4jSecurityInterceptor securityInterceptor = new Wss4jSecurityInterceptor();
		
		securityInterceptor.setSecurementActions(WSHandlerConstants.USERNAME_TOKEN
				+" "+WSHandlerConstants.TIMESTAMP);
		securityInterceptor.setSecurementPasswordType(WSConstants.PW_TEXT);
		securityInterceptor.setSecurementUsername("abc");
		securityInterceptor.setSecurementPassword("qwerty");
		
		return securityInterceptor;
	 }
}
