// Generated code from Butter Knife. Do not modify!
package cn.mstar.store.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ShareQcodeActivity$$ViewBinder<T extends cn.mstar.store.activity.ShareQcodeActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131559139, "field 'idLaySharweixin'");
    target.idLaySharweixin = finder.castView(view, 2131559139, "field 'idLaySharweixin'");
    view = finder.findRequiredView(source, 2131559140, "field 'idLaySharweixinfriend'");
    target.idLaySharweixinfriend = finder.castView(view, 2131559140, "field 'idLaySharweixinfriend'");
    view = finder.findRequiredView(source, 2131559170, "field 'share'");
    target.share = finder.castView(view, 2131559170, "field 'share'");
  }

  @Override public void unbind(T target) {
    target.idLaySharweixin = null;
    target.idLaySharweixinfriend = null;
    target.share = null;
  }
}
