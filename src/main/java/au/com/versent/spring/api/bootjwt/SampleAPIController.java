package au.com.versent.spring.api.bootjwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@PreAuthorize("hasRole('Admin') and #oauth2.hasScope('Customer.info')")
public class SampleAPIController {

	private final Logger logger = LoggerFactory.getLogger(SampleAPIController.class);

	public SampleAPIController() {
	}

	@ApiOperation(value = "me", nickname = "me")
	@RequestMapping(value = "/me", method = RequestMethod.GET, produces = "application/json")
	 @ApiImplicitParams({
		    @ApiImplicitParam(name = "auth"),
		  })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = UserDetails.class),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	public UserDetails index(Authentication auth) {
		OAuth2Request clientAuthentication = ((OAuth2Authentication) auth).getOAuth2Request();
		return new UserDetails(1, auth.getName(), auth.getAuthorities(), clientAuthentication.getScope());
	}
}