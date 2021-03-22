package net.vogman.learnprogramming;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

public class AddContentFragment extends Fragment {
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_add_content, container, false);

    FragmentManager childFM = getChildFragmentManager();
    childFM.beginTransaction().replace(R.id.fragment_target, new AddArticleFragment()).commit();

    RadioGroup rg = view.findViewById(R.id.radio_buttons);
    rg.setOnCheckedChangeListener((group, checkedId) -> {
      if (checkedId == R.id.add_article_radio) {
        childFM.beginTransaction().replace(R.id.fragment_target, new AddArticleFragment()).commit();
      } else if (checkedId == R.id.add_exercise_radio){
        childFM.beginTransaction().replace(R.id.fragment_target, new AddExerciseFragment()).commit();
      } else { // UNREACHABLE
        throw new IllegalStateException();
      }
    });

    return view;
  }
}