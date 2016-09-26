package biopprimrose.d123.d5p.shuger.of.lamp.biopprim;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import java.util.ArrayList;

/**
 * Created by amemiyaY on 2016/05/07.
 */
public class

TitleRecycleFragment extends Fragment implements OnRecycleListener  {

    private Activity activity = null;
    private RecycleFragmentListener fragmentListener = null;
    private View mview;

    private RecyclerView recyclerView = null;
    private RecycleAdapter adapter = null;


    public interface RecycleFragmentListener {
        void onRecycleEvent();
    }

    @Override
    public void onRecycleClicked(View v, int Position) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof RecycleFragmentListener)) {
            throw new UnsupportedOperationException(
                    "Listener is not Implementation");
        } else {
                fragmentListener = (RecycleFragmentListener) activity;
        }
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mview = inflater.inflate(R.layout.fragment_title_recycle, container, false);

        final LinearLayoutManager linearlayoutmanager = new LinearLayoutManager(activity);
        linearlayoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) mview.findViewById(R.id.recycle_view_title);
        recyclerView.setLayoutManager(linearlayoutmanager);
        return mview;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayList<String> array = new ArrayList<>();
        array.add("A");
        array.add("B");
        array.add("C");

        adapter = new RecycleAdapter(activity,array,this);
        recyclerView.setAdapter(adapter);
    }
}
