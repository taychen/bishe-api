package com.bishe;

import com.bishe.common.util.AesCbcUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class AesCbcUtilsTests {
  @Test
  public void testEncrypt() {
    AesCbcUtils.getInstance().setSKey("CtM434c38m0X353F");
    AesCbcUtils.getInstance().setIvParameter("CtM434c38m0X353F");
    log.info(
        "username:{}\npassword:{}",
        AesCbcUtils.getInstance().encrypt("18588206541"),
        AesCbcUtils.getInstance().encrypt("123456"));
  }
}
