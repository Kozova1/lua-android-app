package net.vogman.learnprogramming;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            String luaCode = ((EditText) findViewById(R.id.luaCodeIn)).getText().toString();
            runLuaCode(luaCode);
          }
        });
    }

    private void runLuaCode(String luaCode) {
      Globals globals = JsePlatform.standardGlobals();
      LuaValue chunk = globals.load(luaCode);
      chunk.call();
      ((TextView) findViewById(R.id.luaCodeOut)).setText(globals.get("out").tojstring());
    }
}