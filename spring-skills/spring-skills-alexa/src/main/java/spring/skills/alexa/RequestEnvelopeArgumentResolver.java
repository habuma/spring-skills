package spring.skills.alexa;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.services.Serializer;
import com.amazon.ask.servlet.ServletConstants;
import com.amazon.ask.servlet.verifiers.SkillRequestSignatureVerifier;
import com.amazon.ask.servlet.verifiers.SkillServletVerifier;
import com.amazon.ask.util.JacksonSerializer;

class RequestEnvelopeArgumentResolver implements HandlerMethodArgumentResolver {

	private SkillServletVerifier signatureVerifier;
	
	private Serializer serializer;

	public void setSignatureVerifier(SkillServletVerifier signatureVerifier) {
		this.signatureVerifier = signatureVerifier;
	}
	
	public void setSerializer(Serializer serializer) {
		this.serializer = serializer;
	}
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().isAssignableFrom(RequestEnvelope.class);
	}
	
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest httpRequest = (HttpServletRequest) webRequest.getNativeRequest();
        byte[] serializedRequestEnvelope = IOUtils.toByteArray(httpRequest.getInputStream());
        RequestEnvelope requestEnvelope = getSerializer().deserialize(IOUtils.toString(
                serializedRequestEnvelope, ServletConstants.CHARACTER_ENCODING), RequestEnvelope.class);
		getSignatureVerifier().verify(httpRequest, serializedRequestEnvelope, requestEnvelope);
		return requestEnvelope;
	}
	
	private SkillServletVerifier getSignatureVerifier() {
		if (signatureVerifier == null) {
			this.signatureVerifier = new SkillRequestSignatureVerifier();
		}
		return this.signatureVerifier;
	}
	
	private Serializer getSerializer() {
		if (serializer == null) {
			this.serializer = new JacksonSerializer();
		}
		return this.serializer;
	}
	
}
