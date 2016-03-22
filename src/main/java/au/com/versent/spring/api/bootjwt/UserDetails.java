package au.com.versent.spring.api.bootjwt;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import io.swagger.annotations.ApiModelProperty;

public class UserDetails {

	private final long id;
	private final String principal;
	private final Collection<? extends GrantedAuthority> authorities;
	private final Set<String> scopes;
	private final String token;

	public UserDetails(long id, String principal, Collection<? extends GrantedAuthority> authorities,
			Set<String> scopes, String token) {
		this.id = id;
		this.principal = principal;
		this.authorities = authorities;
		this.scopes = scopes;
		this.token = token;
	}

	@ApiModelProperty(notes = "The id of the user", required = true)
	public long getId() {
		return id;
	}

	@ApiModelProperty(notes = "The principal name of the user", required = true)
	public String getPrincipal() {
		return principal;
	}

	@ApiModelProperty(notes = "The granted authoritied of the user", required = true)
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@ApiModelProperty(notes = "The scopes that the user has consented to", required = true)
	public Set<String> getScopes() {
		return scopes;
	}

	@ApiModelProperty(notes = "The token that was presented", required = true)
	public String getToken() {
		return token;
	}

}
