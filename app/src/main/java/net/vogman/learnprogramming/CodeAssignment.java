package net.vogman.learnprogramming;

import android.util.Log;

import org.luaj.vm2.Globals;
import org.luaj.vm2.Lua;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;


public class CodeAssignment {
  private static final String TAG = "Assignment";
  private final String preText;
  private final String postText;
  private final LuaValue test;
  private final String template;

  public CodeAssignment(String preText, String postText, String template, LuaValue test) {
    this.preText = preText;
    this.postText = postText;
    this.test = test;
    this.template = template;
  }

  public CodeAssignment(String[] row) {
    Globals env = JsePlatform.standardGlobals();
    LuaValue func = null;
    try {
      env.load(row[3]);
      func = env.get("test");
    } catch (LuaError e) {
      String emsg = e.getLocalizedMessage();
      Log.e(TAG, emsg);
      func = new LuaReturnError(emsg);
    }
    this.preText = row[0];
    this.postText = row[1];
    this.template = row[2];
    this.test = func;
  }

  public String getPreText() {
    return preText;
  }

  public String getPostText() {
    return postText;
  }

  public LuaValue getTest() {
    return test;
  }

  public String getTemplate() {
    return template;
  }

  public Result<Boolean, LuaError> runTest(String luaCode) {
    Globals env = JsePlatform.standardGlobals();
    try {
      env.load(luaCode);
      env.load(test);
      return new Result<Boolean, LuaError>().ok(env.get("test").call().toboolean());
    } catch (LuaError e) {
      return new Result<Boolean, LuaError>().err(e);
    }
  }
}
