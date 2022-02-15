package bunkerchain.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.crypto.hash.SimpleHash;

public class MyRealmCredentialsMatcher implements CredentialsMatcher {

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		// TODO Auto-generated method stub
		
		UsernamePasswordToken myPasswordToken = (UsernamePasswordToken)token;
//      //指定加密算法
//      hashedCredentialsMatcher.setHashAlgorithmName("MD5");
//      //指定加密次数
//      hashedCredentialsMatcher.setHashIterations(3);
		String pwdString =  String.copyValueOf(myPasswordToken.getPassword());
		String nameString = myPasswordToken.getUsername();
		
	 	String credentialString = info.getCredentials().toString();
	 	String hashpwdString =  new SimpleHash("MD5", pwdString, nameString, 3).toBase64();

	 	
//	 	SimpleHash hashPwdString = new SimpleHash("MD5", pwdString, userName, 3);
//		String hexpwdString = hashPwdString.toBase64();
		Boolean resultBoolean = credentialString.equals(hashpwdString);
	 	return resultBoolean;
//		return JwtUtil.verify(jwtToken.getCredentials().toString());
	}

}
