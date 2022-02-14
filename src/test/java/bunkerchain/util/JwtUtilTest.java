package bunkerchain.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JwtUtilTest {
	
	
	@Test
	void getClaim() throws Exception {
		String accountString = "admin";
		// 获取当前最新时间戳
		String currentTimeMillis = String.valueOf(System.currentTimeMillis());
		String compareTokenString = JwtUtil.sign(accountString, currentTimeMillis);
		
		String clainString = JwtUtil.getClaim(compareTokenString, "account");
//		assertThat(compareTokenString).contains("chen");
		assertThat(clainString).contains("chen");
	  }
	
	@Test
	void verifytoken() throws Exception {
		String accountString = "admin";
		// 获取当前最新时间戳
		String currentTimeMillis = String.valueOf(System.currentTimeMillis());
				
		String compareTokenString = JwtUtil.sign(accountString, currentTimeMillis);
		Thread.sleep(1000*5);
		boolean vb = JwtUtil.verify(compareTokenString);
//		assertThat(compareTokenString).contains("chen");
		assertThat(vb).isTrue();
	  }
	
//	@BeforeClass
//	public static void setUpBeforeClass() throws Exception {
//	}
//
//	@AfterClass
//	public static void tearDownAfterClass() throws Exception {
//	}
//
//	@Before
//	public void setUp() throws Exception {
//	}
//
//	@After
//	public void tearDown() throws Exception {
//	}

}
