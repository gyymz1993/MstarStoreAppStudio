// Generated code from Butter Knife. Do not modify!
package cn.mstar.store.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ProductDetailsActivity$$ViewBinder<T extends cn.mstar.store.activity.ProductDetailsActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558663, "field 'share'");
    target.share = finder.castView(view, 2131558663, "field 'share'");
  }

  @Override public void unbind(T target) {
    target.share = null;
  }
}
