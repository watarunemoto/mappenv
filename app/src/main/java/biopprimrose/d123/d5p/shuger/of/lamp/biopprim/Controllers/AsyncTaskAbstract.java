package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Controllers;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public abstract class AsyncTaskAbstract<D> extends AsyncTaskLoader<D>{
    private D mResult;
    private boolean mIsStarted = false;

    public AsyncTaskAbstract(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (mResult != null) {
            deliverResult(mResult);
            return;
        }
        if (!mIsStarted || takeContentChanged()) {
            forceLoad();
        }
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        mIsStarted = true;
    }

    @Override
    public void deliverResult(D data) {
        mResult = data;
        super.deliverResult(data);
    }
}