package bunkerchain.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

import bunkerchain.util.JwtUtil;

public class TokenCredentialsMatcher implements CredentialsMatcher {

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		// TODO Auto-generated method stub
		
		JwtToken jwtToken = (JwtToken)token;
		return JwtUtil.verify(jwtToken.getCredentials().toString());
	}

}
