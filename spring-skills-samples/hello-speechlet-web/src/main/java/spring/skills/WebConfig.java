package spring.skills;

import java.util.List;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.amazon.speech.Sdk;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.authentication.SpeechletRequestSignatureVerifier;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(new HandlerMethodArgumentResolver() {

      @Override
      public boolean supportsParameter(MethodParameter param) {
        return param.getParameterType().isAssignableFrom(SpeechletRequestEnvelope.class);
      }

      @Override
      public Object resolveArgument(MethodParameter param, 
                                    ModelAndViewContainer mavc, 
                                    NativeWebRequest request,
                                    WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest httpServletRequest = 
                request.getNativeRequest(HttpServletRequest.class);
        ServletInputStream inputStream = httpServletRequest.getInputStream();
        byte[] bytes = IOUtils.toByteArray(inputStream);

        SpeechletRequestSignatureVerifier.checkRequestSignature(bytes,
            request.getHeader(Sdk.SIGNATURE_REQUEST_HEADER),
            request.getHeader(Sdk.SIGNATURE_CERTIFICATE_CHAIN_URL_REQUEST_HEADER));

        return SpeechletRequestEnvelope.fromJson(bytes);
      }
    });
  }
}
