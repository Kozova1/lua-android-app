package net.vogman.learnprogramming;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

public class LuaReturnError extends OneArgFunction {
  private final String errmsg;
  LuaReturnError(String errmsg) {
    super();
    this.errmsg = errmsg;
  }
  @Override
  public LuaValue call(LuaValue arg) {
    return LuaValue.valueOf("Error: " + errmsg);
  }
}
