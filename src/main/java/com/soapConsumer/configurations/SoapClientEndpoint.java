package com.soapConsumer.configurations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.soapConsumer.client.GetCourseInfoResponse;
import com.soapConsumer.client.ObjectFactory;
import com.soapConsumer.client.SoapRequest;
import com.soapConsumer.client.SoapResponse;
import com.soapConsumer.courseBeans.DeleteCourseInfoRequest;
import com.soapConsumer.courseBeans.GetCourseInfoRequest;
import com.soapConsumer.service.ClientService;

@Endpoint
public class SoapClientEndpoint {
	
	@Autowired
	private ClientService service;
	
//	private Map<String, Object> soapActions = new HashMap<String, Object>();
//	
//	public SoapClientEndpoint() {
//		soapActions.put("getById", new GetCourseInfoRequest());
//	}
//	
	@SuppressWarnings("unchecked")
	@PayloadRoot(namespace="http://www.soapConsumer.com/client",
			localPart="soapRequest")
	@ResponsePayload
	public SoapResponse processRequest(@RequestPayload SoapRequest request) {
		SoapResponse response = new SoapResponse();
		if(request.getSoapAction().equals("getAll")) {
			return null;
		}
		else{
			if(request.getSoapAction().equals("getById")) {
				GetCourseInfoRequest clientRequest = new GetCourseInfoRequest();
				clientRequest.setId(request.getId());
//				JAXBElement<GetCourseInfoResponse> jaxBresponse = new JAXBElement(
//						new QName(GetCourseInfoResponse.class.getSimpleName()), 
//						GetCourseInfoResponse.class, service.handleRequest(clientRequest));
				
				ObjectFactory factory = new ObjectFactory();
				ModelMapper mapper = new ModelMapper();
				Object o = mapper.map(service.handleRequest(clientRequest), Object.class);
				List<JAXBElement<Object>> jaxbList = new ArrayList<JAXBElement<Object>>();
				jaxbList.add(factory.createSoapResponseGetCourseInfoResponse(o));
				
				response.setGetCourseInfoResponseOrGetAllCourseInfoResponseOrDeleteCourseInfoResponse(
						jaxbList);
			}
			else {
				DeleteCourseInfoRequest clientRequest = new DeleteCourseInfoRequest();
				clientRequest.setId(request.getId());
			}
		}
		return response;
	}
}
