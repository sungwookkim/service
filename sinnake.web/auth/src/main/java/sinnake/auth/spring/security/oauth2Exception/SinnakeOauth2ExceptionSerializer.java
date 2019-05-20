package sinnake.auth.spring.security.oauth2Exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class SinnakeOauth2ExceptionSerializer extends StdSerializer<SinnakeOauth2Exception> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SinnakeOauth2ExceptionSerializer() {
		super(SinnakeOauth2Exception.class);
	}

	@Override
	public void serialize(SinnakeOauth2Exception value, JsonGenerator jsonGenerator, SerializerProvider provider)
			throws IOException {

        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("code", SinnakeOauth2ExceptionCode.eq(value.getOAuth2ErrorCode()).getCode());
        jsonGenerator.writeObjectField("errors", Arrays.asList(value.getOAuth2ErrorCode(), value.getMessage()));

        if (value.getAdditionalInformation() != null) {
            for (Map.Entry<String, String> entry : value.getAdditionalInformation().entrySet()) {
                String key = entry.getKey();
                String add = entry.getValue();
                jsonGenerator.writeStringField(key, add);
            }
        }
        
        jsonGenerator.writeEndObject();
    }
	
	enum SinnakeOauth2ExceptionCode {

		ERROR("error", -1)
		, DESCRIPTION("error_description", -2)
		, URI("error_uri", -3)
		, INVALID_REQUEST("invalid_request", -4)
		, INVALID_CLIENT("invalid_client", -5)
		, INVALID_GRANT("invalid_grant", -6)
		, UNAUTHORIZED_CLIENT("unauthorized_client", -7)
		, UNSUPPORTED_GRANT_TYPE("unsupported_grant_type", -8)
		, INVALID_SCOPE("invalid_scope", -9)
		, INSUFFICIENT_SCOPE("insufficient_scope", -10)
		, INVALID_TOKEN("invalid_token", -11)
		, REDIRECT_URI_MISMATCH("redirect_uri_mismatch", -12)
		, UNSUPPORTED_RESPONSE_TYPE("unsupported_response_type", -13)
		, ACCESS_DENIED("access_denied", -14)
		, DEFAULT("", -100);
		
		String key;
		int code;
		
		private SinnakeOauth2ExceptionCode(String key, int code) {
			this.key = key;
			this.code = code;
		}

		public String getKey() {
			return this.key;
		}
		
		public int getCodeInt() {
			return this.code;
		}
		
		public String getCode() {
			return String.valueOf(this.code);
		}
		
		public static SinnakeOauth2ExceptionCode eq(String key) {
			SinnakeOauth2ExceptionCode rtn = SinnakeOauth2ExceptionCode.DEFAULT;			
			SinnakeOauth2ExceptionCode[] sinnakeOauth2ExceptionCodes = SinnakeOauth2ExceptionCode.values();
			
			for(SinnakeOauth2ExceptionCode sinnakeOauth2ExceptionCode : sinnakeOauth2ExceptionCodes) {
				if(key.equals(sinnakeOauth2ExceptionCode.getKey())) {
					rtn = sinnakeOauth2ExceptionCode;
					break;
				}
			}
			
			return rtn;
		}
	}	
}
