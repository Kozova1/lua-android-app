package net.vogman.learnprogramming;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.Bit32Lib;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.StringLib;
import org.luaj.vm2.lib.TableLib;
import org.luaj.vm2.lib.jse.JseBaseLib;
import org.luaj.vm2.lib.jse.JseIoLib;
import org.luaj.vm2.lib.jse.JseMathLib;
import org.luaj.vm2.lib.jse.JseOsLib;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReplFragment extends Fragment implements AutoCloseable {

  private final Globals globalEnv;
  private final ByteArrayOutputStream stdout;
  private final PrintStream pOStream;
  private final PipedInputStream stdin;
  private final PipedOutputStream stdinWriteStream;
  private EditText inputBox;
  private TextView textView;

  public ReplFragment() throws IOException {
    stdin = new PipedInputStream();
    stdinWriteStream = new PipedOutputStream();
    stdin.connect(stdinWriteStream);
    stdout = new ByteArrayOutputStream();
    try {
      pOStream = new PrintStream(stdout, true, "utf-8");
    } catch (UnsupportedEncodingException e) {
      throw new IllegalStateException();
    }
    this.globalEnv = JsePlatform.standardGlobals();
    globalEnv.STDOUT = pOStream;
    globalEnv.STDIN = stdin;
    globalEnv.set("java", LuaValue.NIL);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_repl, container, false);

    textView = view.findViewById(R.id.repl_history_target);
    inputBox = view.findViewById(R.id.repl_code_in);

    view.findViewById(R.id.repl_send).setOnClickListener(v -> {
      String inputCode = inputBox.getText().toString();
      inputBox.setText("");
      try {
        LuaValue res = globalEnv.load(inputCode).call();
        globalEnv.set("it", res);
        if (!res.isnil()) stdout.write(("it = " + res.tojstring()).getBytes(StandardCharsets.UTF_8));
        stdout.write('\n');
      } catch (LuaError | IOException e) {
        try {
          stdout.write(e.getMessage().getBytes(StandardCharsets.UTF_8));
        } catch (IOException ioException) {
          ioException.printStackTrace();
        }
      } finally {
        stdout.write('\n');
        textView.setText(new String(stdout.toByteArray(), StandardCharsets.UTF_8));
      }
    });

    return view;
  }

  @Override
  public void close() throws Exception {
    stdout.close();
    pOStream.close();
    stdin.close();
    stdinWriteStream.close();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    try {
      close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}